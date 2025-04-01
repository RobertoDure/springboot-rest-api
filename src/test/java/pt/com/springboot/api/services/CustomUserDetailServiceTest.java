package pt.com.springboot.api.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pt.com.springboot.api.model.User;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import pt.com.springboot.api.error.InternalServerErrorException;
import pt.com.springboot.api.repository.UserRepository;
import pt.com.springboot.api.service.impl.CustomUserDetailService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private CustomUserDetailService customUserDetailService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("johndoe");
        testUser.setPassword("password");
        testUser.setEmail("john@example.com");
        testUser.setAdmin(false);
    }

    @Test
    void loadUserByUsername_existingUser_returnsUserDetails() {
        when(userRepository.findByUsername("johndoe")).thenReturn(testUser);
        UserDetails userDetails = customUserDetailService.loadUserByUsername("johndoe");
        assertNotNull(userDetails);
        assertEquals("johndoe", userDetails.getUsername());
        // Check that the roles are as expected for non-admin
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        verify(userRepository, times(1)).findByUsername("johndoe");
    }

    @Test
    void loadUserByUsername_nonExistingUser_throwsException() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailService.loadUserByUsername("nonexistent");
        });
    }

    @Test
    void saveUser_validUser_returnsTrue() {
        // Given a valid user, when saveUser is called, it encodes password before saving
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        boolean result = customUserDetailService.saveUser(testUser);
        // The password should have been encoded. Here we assume that PasswordEncoder.encoder returns a different value.
        assertNotEquals("password", testUser.getPassword());
        assertTrue(result);
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void saveUser_exceptionThrown_throwsInternalServerErrorException() {
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("DB error"));
        InternalServerErrorException ex = assertThrows(InternalServerErrorException.class,
            () -> customUserDetailService.saveUser(testUser));
        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void listAll_usersPasswordsHidden() {
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("secret1");
        users.add(user1);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = customUserDetailService.listAll();
        assertEquals(1, result.size());
        // Password should be replaced with ************
        assertEquals("************", result.get(0).getPassword());
    }

    @Test
    void findByUsername_existingUser_returnsUser() {
        when(userRepository.findByUsername("johndoe")).thenReturn(testUser);
        User user = customUserDetailService.findByUsername("johndoe");
        assertNotNull(user);
        assertEquals("johndoe", user.getUsername());
    }

    @Test
    void findByEmail_existingUser_returnsUser() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(testUser);
        User user = customUserDetailService.findByEmail("john@example.com");
        assertNotNull(user);
        assertEquals("john@example.com", user.getEmail());
    }

    @Test
    void findByEmail_nonExistingUser_throwsException() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailService.findByEmail("nonexistent@example.com");
        });
    }

    @Test
    void sendRecoveryEmail_andValidateRecoveryHash() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(testUser);
        // Call sendRecoveryEmail. This will generate a random recovery token and call mailSender.send.
        customUserDetailService.sendRecoveryEmail("john@example.com");

        // Capture the SimpleMailMessage argument that was sent to the mailSender
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(messageCaptor.capture());
        SimpleMailMessage message = messageCaptor.getValue();

        assertEquals("password_recovery@school.com", message.getFrom());
        assertEquals("john@example.com", message.getTo()[0]);
        assertEquals("Password Recovery", message.getSubject());
        // Extract recovery hash from the text. The URL is http://localhost:8080/api/v1/user/recover/{recoveryHash}
        String text = message.getText();
        assertTrue(text.contains("http://localhost:8080/api/v1/user/recover/"));
        String recoveryHash = text.substring(text.lastIndexOf("/") + 1);
        // Validate that the recovery hash is a valid UUID
        assertDoesNotThrow(() -> UUID.fromString(recoveryHash));

        // Now, validate the recovery hash using validateRecoveryHash
        boolean isValid = customUserDetailService.validateRecoveryHash(recoveryHash);
        assertTrue(isValid);
    }

    @Test
    void updatePassword_validToken_updatesPassword() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(testUser);
        // Send recovery email to add a token
        customUserDetailService.sendRecoveryEmail("john@example.com");

        // Capture the recovery hash from the sent email.
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(messageCaptor.capture());
        SimpleMailMessage message = messageCaptor.getValue();
        String recoveryHash = message.getText().substring(message.getText().lastIndexOf("/") + 1);

        // Update password using the valid token
        String newRawPassword = "newpassword";
        boolean updateResult = customUserDetailService.updatePassword(recoveryHash, newRawPassword);
        assertTrue(updateResult);
        // Verify that save was called on the repository with an updated password.
        verify(userRepository, times(1)).save(any(User.class));
        // As the password was set via PasswordEncoder.encoder, it should not equal the raw password.
        assertNotEquals(newRawPassword, testUser.getPassword());
    }

    @Test
    void updatePassword_invalidToken_returnsFalse() {
        // If token does not exist, updatePassword should return false.
        boolean updateResult = customUserDetailService.updatePassword("invalid-hash", "newpassword");
        assertFalse(updateResult);
    }
}
