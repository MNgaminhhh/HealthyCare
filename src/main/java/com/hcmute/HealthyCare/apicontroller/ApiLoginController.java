package com.hcmute.HealthyCare.apicontroller;

import com.hcmute.HealthyCare.entity.Account;
import com.hcmute.HealthyCare.entity.UserInfoDetails;
import com.hcmute.HealthyCare.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
public class ApiLoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody Account account, HttpServletResponse response, HttpServletRequest request) {
        String token = userService.loginUser(account.getEmail(), account.getPassword());
        if (token != null) {
            Cookie cookie = new Cookie("jwt", token);
            cookie.setMaxAge((int) TimeUnit.HOURS.toSeconds(2)); // Cookie tồn tại trong 2 giờ
            cookie.setPath("/");
            response.addCookie(cookie);
            return ResponseEntity.ok().body("Login successful!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }
}
