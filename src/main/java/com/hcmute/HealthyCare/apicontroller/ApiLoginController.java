package com.hcmute.HealthyCare.apicontroller;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcmute.HealthyCare.entity.Account;
import com.hcmute.HealthyCare.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class ApiLoginController {
    
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody Account account, HttpServletResponse response) {
        String loginStatus = userService.loginUser(account, response);
        if (loginStatus.equals("Success")) {
            String token = response.getHeader("Authorization").replace("Bearer ", "");
            
            // Create JWT Token Cookie
            Cookie jwtCookie = new Cookie("jwtToken", token);
            jwtCookie.setPath("/");
            jwtCookie.setHttpOnly(true); 
            jwtCookie.setMaxAge((int) TimeUnit.HOURS.toSeconds(2));
            response.addCookie(jwtCookie);
            
            // Create Email Cookie
            Cookie emailCookie = new Cookie("email", account.getEmail());
            emailCookie.setPath("/");
            emailCookie.setMaxAge((int) TimeUnit.HOURS.toSeconds(2));
            response.addCookie(emailCookie);
            
            return new ResponseEntity<>("Login successful", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Login failed", HttpStatus.UNAUTHORIZED); 
        }
    }
}
