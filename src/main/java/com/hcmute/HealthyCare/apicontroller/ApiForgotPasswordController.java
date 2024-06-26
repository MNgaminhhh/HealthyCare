package com.hcmute.HealthyCare.apicontroller;

import com.hcmute.HealthyCare.entity.Account;
import com.hcmute.HealthyCare.entity.EmailToken;
import com.hcmute.HealthyCare.entity.ResetPasswordRequest;
import com.hcmute.HealthyCare.entity.UserInfoDetails;
import com.hcmute.HealthyCare.service.EmailService;
import com.hcmute.HealthyCare.service.UserService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiForgotPasswordController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam("email") String email) {
        try {
            Account acc = userService.getUserByEmail(email);
            if (!acc.isVerified()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vui lòng xác minh tài khoản trước khi đặt lại mật khẩu.");
            }
            emailService.createPasswordResetToken(acc);
            return ResponseEntity.ok("Yêu cầu đặt lại mật khẩu đã được gửi thành công.");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Người dùng không tồn tại.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi khi thực hiện yêu cầu đặt lại mật khẩu.");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam(name = "token") String token, @RequestBody ResetPasswordRequest request) {
        try {
            String newPassword = request.getNewPassword();
            EmailToken emailToken = emailService.findByToken(token);
            if (emailToken != null && emailToken.getAccount() != null) {
                LocalDateTime expiryDate = emailToken.getExpiryDate();
                LocalDateTime now = LocalDateTime.now();
                if (now.isBefore(expiryDate)) { 
                    Account account = emailToken.getAccount();
                    userService.resetPassword(account.getEmail(), newPassword);
                    emailService.deleteEmailToken(emailToken);
                    return ResponseEntity.ok("Mật khẩu đã được đặt lại thành công.");
                } else {
                    emailService.deleteEmailToken(emailToken);
                    return ResponseEntity.badRequest().body("Token đã hết hạn.");
                }
            } else {
                return ResponseEntity.badRequest().body("Token không hợp lệ.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi khi đặt lại mật khẩu.");
        }
    }

    @PostMapping("/changepassword")
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody ResetPasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserInfoDetails) {
            UserInfoDetails userInfoDetails = (UserInfoDetails) authentication.getPrincipal();
            String userEmail = userInfoDetails.getUsername();
            try {
                String newPassword = request.getNewPassword();
                String oldString = userInfoDetails.getPassword();
                userService.changePassword(userEmail, oldString, newPassword);
                Map<String, String> response = new HashMap<>();
                response.put("message", "Mật khẩu đã được thay đổi thành công.");
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Đã xảy ra lỗi khi thay đổi mật khẩu.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
        } else {
            Map<String, String> unauthorizedResponse = new HashMap<>();
            unauthorizedResponse.put("error", "User not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(unauthorizedResponse);
        }
    }



}   
