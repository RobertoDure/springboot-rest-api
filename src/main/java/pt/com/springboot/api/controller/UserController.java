package pt.com.springboot.api.controller;

import io.swagger.annotations.Api;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pt.com.springboot.api.error.BadRequestException;
import pt.com.springboot.api.model.User;
import pt.com.springboot.api.service.impl.CustomUserDetailService;
import pt.com.springboot.api.util.HttpHeadersUtil;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("api/v1/user")
@Api(value = "User Endpoint", description = "A REST API for users", tags = {"User Endpoint"})
public class UserController {

    private CustomUserDetailService userService;

    public UserController(CustomUserDetailService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> listAll() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("transactionId", MDC.get("transactionId"));
        return new ResponseEntity<>(userService.listAll() ,HttpHeadersUtil.setHttpHeaders(headers), HttpStatus.OK);
    }
    // Email recovery
    @GetMapping("/{email}")
    public String getEmailRecovery(@PathVariable("email") String email) {

        userService.sendRecoveryEmail(email);

        return "Email sent";
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


}
