package pt.com.springboot.api.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.util.List;
import java.util.Optional;

@Component
public class CustomUserDetailService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailService.class.getName());

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = Optional.ofNullable(userRepository.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<GrantedAuthority> authorityListAdmin = AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN");
        List<GrantedAuthority> authorityListUser = AuthorityUtils.createAuthorityList("ROLE_USER");
        return new org.springframework.security.core.userdetails.User
                (user.getUsername(), user.getPassword(), user.isAdmin() ? authorityListAdmin : authorityListUser);

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

    public List<User> listAll() {
        // Do no return password
        List<User> users = userRepository.findAll();
        //Hide password
        users.forEach(user -> user.setPassword("************"));
        return users;
    }
}
