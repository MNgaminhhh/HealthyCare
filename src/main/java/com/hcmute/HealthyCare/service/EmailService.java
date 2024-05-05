package com.hcmute.HealthyCare.service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.hcmute.HealthyCare.entity.Account;
import com.hcmute.HealthyCare.entity.EmailToken;
import com.hcmute.HealthyCare.repository.AccountRepository;
import com.hcmute.HealthyCare.repository.EmailTokenRepository;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private EmailTokenRepository emailTokenRepository;
    
    @Autowired
    private AccountRepository accountRepository;

    public void createCodeEmail(Account account, String toEmail, String tokenn) {
        String subject = "[HealthyCare] Xác nhận đăng ký tài khoản";
        String code = generateRandomCode();
        String token = tokenn;
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(10);
        String text = "Xin chào,\n\n"
                + "Chúng tôi rất vui mừng khi chào đón bạn đến với HealthyCare!\n\n"
                + "Để hoàn tất quá trình đăng ký tài khoản của bạn, vui lòng sử dụng mã xác nhận sau đây:\n\n"
                + "Mã xác nhận: " + code + "\n\n"
                + "Lưu ý: Mã xác nhận này chỉ có hiệu lực trong vòng 10 phút kể từ thời điểm nhận email này.\n\n"
                + "Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email này.\n\n"
                + "Trân trọng,\n"
                + "Đội ngũ HealthyCare";
        sendEmail(toEmail, subject, text);
        saveVerificationCode(account, token, code, toEmail, expiryDate);
    }

    private String generateRandomCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public String generateRandomToken() {
        return UUID.randomUUID().toString();
    }

    private void sendEmail(String toEmail, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    private void saveVerificationCode(Account account, String token, String code, String email, LocalDateTime expiryDate) {
        EmailToken verificationCode = new EmailToken();
        verificationCode.setToken(token);
        verificationCode.setCode(code);
        verificationCode.setEmail(email);
        verificationCode.setExpiryDate(expiryDate);
        verificationCode.setAccount(account);
        emailTokenRepository.save(verificationCode);
    }
    public EmailToken findByToken(String token) {
        return emailTokenRepository.findByToken(token);
    }
    public boolean existsByToken(String token) {
        EmailToken emailToken = emailTokenRepository.findByToken(token);
        if (emailToken != null) {
            LocalDateTime expiryDate = emailToken.getExpiryDate();
            if (LocalDateTime.now().isAfter(expiryDate)) {
                emailTokenRepository.delete(emailToken);
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public boolean verifyCode(String token, String code) {
        EmailToken emailToken = emailTokenRepository.findByToken(token);
        if (emailToken != null && emailToken.getCode().equals(code)) {
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(emailToken.getExpiryDate())) {
                Account account = emailToken.getAccount();
                if (account != null) {
                    account.setVerified(true);
                    accountRepository.save(account);
                    return true;
                }
            }
        }
        return false;
    }
}
