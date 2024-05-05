package com.hcmute.HealthyCare.apicontroller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hcmute.HealthyCare.entity.Account;
import com.hcmute.HealthyCare.service.EmailService;

@RestController
@RequestMapping("/api/email")
public class ApiEmailController {

    private final EmailService emailService;

    @Autowired
    public ApiEmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> sendVerificationEmail(@RequestBody Account account) {
        String token = emailService.generateRandomToken();
        emailService.createCodeEmail(account, account.getEmail(), token);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping("/check")
    public ResponseEntity<String> verifyCode(@RequestParam("token") String token, @RequestParam("code") String code) {
        boolean isValid = emailService.verifyCode(token, code);
        
        if (isValid) {
            return ResponseEntity.ok("Xác thực thành công!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Xác thực thất bại. Vui lòng kiểm tra lại mã xác thực.");
        }
    }

    @GetMapping("/resend")
    public ResponseEntity<String> verifyCode(@RequestParam("token") String token) {
        boolean isValid = emailService.resendVerificationCode(token);
        if (isValid) {
            return ResponseEntity.ok("Gửi lại mã xác thực thành công!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Gửi lại mã xác thực thất bại. Vui lòng kiểm tra lại");
        }
    }
    
    @GetMapping("/checktoken")
    public ResponseEntity<String> checkToken(@RequestParam(name = "token") String token) {
        boolean tokenExists = emailService.existsByToken(token);
        if (tokenExists) {
            return new ResponseEntity<>("Token exists", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Token does not exist", HttpStatus.NOT_FOUND);
        }
    }

}
