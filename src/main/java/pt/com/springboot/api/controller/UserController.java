package pt.com.springboot.api.controller;

import io.swagger.annotations.Api;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pt.com.springboot.api.error.BadRequestException;
import pt.com.springboot.api.model.User;
import pt.com.springboot.api.service.impl.CustomUserDetailServiceImpl;
import pt.com.springboot.api.util.HttpHeadersUtil;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("api/v1/user")
@Api(value = "User Endpoint", description = "A REST API for users", tags = {"User Endpoint"})
public class UserController {

    private final CustomUserDetailServiceImpl userService;

    public UserController(CustomUserDetailServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> listAll() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("transactionId", MDC.get("transactionId"));
        return new ResponseEntity<>(userService.listAll() ,HttpHeadersUtil.setHttpHeaders(headers), HttpStatus.OK);
    }

    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> save(@Valid @RequestBody User user) {
        boolean userSaved = userService.saveUser(user);
        if (!userSaved ) {
            throw new BadRequestException("Student not saved: " + userSaved);
        }
        HashMap<String, String> headers = new HashMap<>();
        headers.put("transactionId", MDC.get("transactionId"));
        return new ResponseEntity<>(HttpHeadersUtil.setHttpHeaders(headers), HttpStatus.CREATED);
    }

    @PutMapping
    public String update() {
        return "UserController";
    }

    @DeleteMapping
    public String delete() {
        return "UserController";
    }

    @GetMapping("/recover-password")
    public ResponseEntity<String> requestPasswordRecovery(@RequestParam String email) {
        userService.sendRecoveryEmail(email);
        return ResponseEntity.ok("Recovery email sent successfully");
    }

    @GetMapping("/recover/{hash}")
    public ResponseEntity<String> validateRecoveryHash(@PathVariable String hash) {
        if (userService.validateRecoveryHash(hash)) {
            return ResponseEntity.ok("Valid recovery link");
        }
        return ResponseEntity.badRequest().body("Invalid or expired recovery link");
    }

    @PostMapping("/reset-password/{hash}")
    public ResponseEntity<String> resetPassword(
            @PathVariable String hash,
            @RequestParam String newPassword) {
        if (userService.updatePassword(hash, newPassword)) {
            return ResponseEntity.ok("Password updated successfully");
        }
        return ResponseEntity.badRequest().body("Invalid or expired recovery link");
    }

}
