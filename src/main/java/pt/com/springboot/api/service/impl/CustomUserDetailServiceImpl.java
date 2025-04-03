package pt.com.springboot.api.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import pt.com.springboot.api.PasswordEncoder;
import pt.com.springboot.api.error.InternalServerErrorException;
import pt.com.springboot.api.model.User;
import pt.com.springboot.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CustomUserDetailServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailServiceImpl.class.getName());

    private final UserRepository userRepository;

    private final JavaMailSender mailSender;

    // Add a map to store recovery tokens with expiration
    private final Map<String, RecoveryToken> recoveryTokens = new ConcurrentHashMap<>();

    public CustomUserDetailServiceImpl(UserRepository userRepository, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
            logger.info("Starting Load User By Username service");
            logger.debug("Username: {}", username);
            User user = Optional.ofNullable(userRepository.findByUsername(username))
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            List<GrantedAuthority> authorityListAdmin = AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN");
            List<GrantedAuthority> authorityListUser = AuthorityUtils.createAuthorityList("ROLE_USER");
            return new org.springframework.security.core.userdetails.User
                    (user.getUsername(), user.getPassword(), user.isAdmin() ? authorityListAdmin : authorityListUser);
        } catch (Exception e) {
            logger.error("Error trying to load user by username: {}", e.getMessage());
            throw new UsernameNotFoundException("User not found");
        }
    }

    /**
     * Save user to database
     * @param user
     * @return
     */
    public boolean saveUser(User user) {

        try {
            logger.info("Starting Save User service");
            logger.debug("User to save: {}", user);
            // Update lectures to have managed references
            // Encrypt password
            user.setPassword(PasswordEncoder.encoder(user.getPassword()));
            userRepository.save(user);

            logger.info("Finishing Save user service");
            return true;
        } catch (Exception e) {
            logger.error("Error trying to save user: {}", e.getMessage());
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    /**
     * List all users
     * @return List of users
     */
    public List<User> listAll() {
        // Do no return password
        List<User> users = userRepository.findAll();
        //Hide password
        users.forEach(user -> user.setPassword("************"));
        return users;
    }

    public User findByUsername(String username) {

        return userRepository.findByUsername(username);
    }

    /**
     * Find user by email
     * @param email
     * @return User
     */
    public User findByEmail(String email) {

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return userRepository.findByEmail(email);
    }

    /**
     * Send email to user with a link to recover the password
     * @param email
     */
    public void sendRecoveryEmail(String email) {
        // Send email to user with a link to recover the password
        // Use a library like JavaMailSender to send the email
        User user = findByEmail(email);
        String recoveryHash = UUID.randomUUID().toString();

        // Store the recovery token
        recoveryTokens.put(recoveryHash, new RecoveryToken(email));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("password_recovery@school.com");
        message.setTo(email);
        message.setSubject("Password Recovery");
        message.setText("Click here to recover your password: http://localhost:8080/api/v1/user/recover/" + recoveryHash);
        mailSender.send(message);
        logger.info("Recovery email sent to: {}", email);
    }

    /**
     * Validate recovery hash
     * @param hash
     * @return true if hash is valid, false otherwise
     */
    public boolean validateRecoveryHash(String hash) {
        RecoveryToken token = recoveryTokens.get(hash);
        if (token != null && token.isValid()) {
            // Token is valid
            return true;
        }
        // Remove invalid token
        recoveryTokens.remove(hash);
        return false;
    }
    /**
     * Update user password
     * @param hash
     * @param newPassword
     * @return true if password was updated, false otherwise
     */
    public boolean updatePassword(String hash, String newPassword) {
        RecoveryToken token = recoveryTokens.get(hash);
        if (token != null && token.isValid()) {
            User user = findByEmail(token.userEmail);
            user.setPassword(PasswordEncoder.encoder(newPassword));
            userRepository.save(user);
            // Remove used token
            recoveryTokens.remove(hash);
            return true;
        }
        return false;
    }
   /**
     * Inner class to store recovery token
     */

    private static class RecoveryToken {
        String userEmail;
        LocalDateTime expirationTime;

        RecoveryToken(String userEmail) {
            this.userEmail = userEmail;
            this.expirationTime = LocalDateTime.now().plusHours(1); // Token expires in 1 hour
        }

        boolean isValid() {
            return LocalDateTime.now().isBefore(expirationTime);
        }
    }

}
