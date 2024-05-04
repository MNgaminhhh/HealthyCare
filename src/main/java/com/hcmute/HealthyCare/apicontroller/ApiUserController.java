package com.hcmute.HealthyCare.apicontroller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcmute.HealthyCare.entity.Account;
import com.hcmute.HealthyCare.entity.User;
import com.hcmute.HealthyCare.service.EmailService;
import com.hcmute.HealthyCare.service.UserService;

import jakarta.validation.constraints.Email;

@Controller
@RestController
@RequestMapping("/api")
public class ApiUserController {

    private final UserService userService;
    private final EmailService emailService;

    @Autowired
    public ApiUserController(UserService userService, EmailService emailService) { 
        this.userService = userService;
        this.emailService = emailService; 
    }
    
    @PostMapping("/register")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User savedUser = userService.addNewUser(user);
        String email = user.getEmail();
        String token = UUID.randomUUID().toString();
        emailService.createCodeEmail(email, token);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody Account account) {
        String loginStatus = userService.loginUser(account);
        if(loginStatus.equals("Success")) {
            return new ResponseEntity<>("Login successful", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Login failed", HttpStatus.UNAUTHORIZED);
        }
    }
}
