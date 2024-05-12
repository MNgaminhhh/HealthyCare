package com.hcmute.HealthyCare.apicontroller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcmute.HealthyCare.entity.Account;
import com.hcmute.HealthyCare.service.UserService;

@RestController
@RequestMapping("/api")
public class ApiLoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody Account account, HttpServletResponse response, HttpServletRequest request) {
        String loginStatus = userService.loginUser(account, response, request);
        if(loginStatus.equals("Success")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("Retrieved Authentication from SecurityContext: " + authentication);
            String email = authentication.getName();
            System.out.println(email);
            return new ResponseEntity<>("Login successful", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Login failed", HttpStatus.UNAUTHORIZED);
        }
    }
}