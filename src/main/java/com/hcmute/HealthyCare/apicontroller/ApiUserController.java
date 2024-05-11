package com.hcmute.HealthyCare.apicontroller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.hcmute.HealthyCare.entity.Account;
import com.hcmute.HealthyCare.entity.Doctor;
import com.hcmute.HealthyCare.entity.Patient;
import com.hcmute.HealthyCare.entity.User;
import com.hcmute.HealthyCare.entity.UserInfoDetails;
import com.hcmute.HealthyCare.enums.Rolee;
import com.hcmute.HealthyCare.service.EmailService;
import com.hcmute.HealthyCare.service.UserService;

import jakarta.validation.constraints.Email;

@RestController
@RequestMapping("/api")
public class ApiUserController {

    private final UserService userService;
    private final EmailService emailService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    public ApiUserController(UserService userService, EmailService emailService) { 
        this.userService = userService;
        this.emailService = emailService; 
    }
    
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> addUser(@RequestBody User user) {
        User savedUser = userService.addNewUser(user);
        Account account = userService.loadAccount(user.getEmail());
        ResponseEntity<String> emailResponse = restTemplate.postForEntity("http://localhost:1999/api/email/add", account, String.class);
        if (emailResponse.getStatusCode() == HttpStatus.OK) {
            String token = emailResponse.getBody();
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("token", token);
            return ResponseEntity.ok().body(responseMap);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    } 

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserInfoDetails) {
            UserInfoDetails userInfo = (UserInfoDetails) authentication.getPrincipal();
            String userEmail = userInfo.getUsername();
            Optional<User> user = Optional.ofNullable(userService.findUserByEmail(userEmail));

            return user.map(value -> {
                Map<String, Object> userInfoMap = new HashMap<>();
                userInfoMap.put("email", value.getEmail());
                userInfoMap.put("avatar", value.getAvatar());
                userInfoMap.put("name", value.getName());
                userInfoMap.put("address", value.getAddress());
                userInfoMap.put("phone", value.getPhone());
                userInfoMap.put("birthday", value.getBirthday());
                userInfoMap.put("gender", value.getGender());
                userInfoMap.put("role", value.getRole());
                userInfoMap.put("education", value.getEducation());
                userInfoMap.put("numberofyear", value.getNumberofyear());
                userInfoMap.put("workplace", value.getWorkplace());
                userInfoMap.put("introduction", value.getIntroduction());
                userInfoMap.put("specially", value.getSpecially());
                userInfoMap.put("underlyingDisease", value.getUnderlyingDisease());

                return ResponseEntity.ok().body(userInfoMap);
            }).orElse(ResponseEntity.notFound().build());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User userUpdateRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserInfoDetails) {
            UserInfoDetails userInfoDetails = (UserInfoDetails) authentication.getPrincipal();
            String userEmail = userInfoDetails.getUsername();
            Optional<User> userOptional = Optional.ofNullable(userService.findUserByEmail(userEmail));

            return userOptional.map(user -> {
                user.setName(userUpdateRequest.getName());
                user.setAddress(userUpdateRequest.getAddress());
                user.setPhone(userUpdateRequest.getPhone());
                user.setBirthday(userUpdateRequest.getBirthday());
                user.setGender(userUpdateRequest.getGender());

                if (user.getRole() == Rolee.ROLE_DOCTOR) {
                    user.setSpecially(userUpdateRequest.getSpecially());
                    user.setWorkplace(userUpdateRequest.getWorkplace());
                    user.setNumberofyear(userUpdateRequest.getNumberofyear());
                    user.setEducation(userUpdateRequest.getEducation());
                    user.setIntroduction(userUpdateRequest.getIntroduction());
                } else if (user.getRole() == Rolee.ROLE_PATIENT) {
                    user.setUnderlyingDisease(userUpdateRequest.getUnderlyingDisease());
                }

                userService.saveUser(user);
                return ResponseEntity.ok().body("Thông tin người dùng đã được cập nhật thành công.");
            }).orElse(ResponseEntity.notFound().build());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
    }



}
