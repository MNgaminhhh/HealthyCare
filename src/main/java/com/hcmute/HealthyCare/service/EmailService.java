package com.hcmute.HealthyCare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void createCodeEmail(String toEmail, String token) {
        String subject = "Xác nhận đăng ký tài khoản";
        String text = "Chào mừng bạn đến với HealthyCare!\n\n"
                    + "Vui lòng sử dụng mã sau để xác nhận tài khoản của bạn:\n\n"
                    + token;
        sendEmail(toEmail, subject, text);
    }

    private void sendEmail(String toEmail, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}
