package pt.com.springboot.api;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class PasswordEncoder {

    /**
     * Encode password
     * @param password
     * @return
     */
    public static String encoder(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
}

