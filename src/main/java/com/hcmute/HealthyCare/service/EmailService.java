package com.hcmute.HealthyCare.service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.hcmute.HealthyCare.entity.Account;
import com.hcmute.HealthyCare.entity.Doctor;
import com.hcmute.HealthyCare.entity.EmailToken;
import com.hcmute.HealthyCare.entity.Patient;
import com.hcmute.HealthyCare.repository.AccountRepository;
import com.hcmute.HealthyCare.repository.DoctorRepository;
import com.hcmute.HealthyCare.repository.EmailTokenRepository;
import com.hcmute.HealthyCare.repository.PatientRepository;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private EmailTokenRepository emailTokenRepository;
    
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;

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
                + "HealthyCare";
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
                Account account = emailToken.getAccount();
                emailTokenRepository.delete(emailToken);
                if (account != null) {
                    Doctor doctor = account.getDoctor();
                    if (doctor != null) {
                        doctorRepository.delete(doctor);
                    }
                    Patient patient = account.getPatient();
                    if (patient != null) {
                        patientRepository.delete(patient);
                    }
                    accountRepository.delete(account);
                    return false;
                }
            } else {
                return true;
            }
        }
        return false;
    }
    public void deleteEmailToken(EmailToken emailToken) {
        emailTokenRepository.delete(emailToken);
    }
    
    public String getEmailFromToken(String token) {
        EmailToken emailToken = emailTokenRepository.findByToken(token);
        if (emailToken != null) {
            LocalDateTime expiryDate = emailToken.getExpiryDate();
            if (LocalDateTime.now().isAfter(expiryDate)) {
                emailTokenRepository.delete(emailToken);
            } else {
                return emailToken.getEmail();
            }
        }
        return null;
    }
    
    
    public boolean resendVerificationCode(String token){
        EmailToken emailToken = emailTokenRepository.findByToken(token);
        if (emailToken != null) {
            LocalDateTime expiryDate = emailToken.getExpiryDate();
            if (LocalDateTime.now().isBefore(expiryDate)) {
                Account account = emailToken.getAccount();
                if (account != null) {
                    String newCode = generateRandomCode();
                    emailToken.setCode(newCode);
                    emailToken.setExpiryDate(LocalDateTime.now().plusMinutes(10));
                    emailTokenRepository.save(emailToken);
                    String newSubject = "[HealthyCare] Mã xác nhận mới";
                    String newEmailText = "Xin chào,\n\n"
                            + "Mã xác nhận mới của bạn là:\n\n"
                            + newCode + "\n\n"
                            + "Mã này chỉ có hiệu lực trong vòng 10 phút kể từ thời điểm nhận email này.\n\n"
                            + "Trân trọng,\n"
                            + "HealthyCare";
                    sendEmail(account.getEmail(), newSubject, newEmailText);
                    return true;
                }
            }
        }
        return false;
    }
    public String createPasswordResetToken(Account account) {
        String token = generateRandomToken();
        EmailToken passwordResetToken = new EmailToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setEmail(account.getEmail());
        passwordResetToken.setAccount(account);
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(10);
        passwordResetToken.setExpiryDate(expiryDate);
        emailTokenRepository.save(passwordResetToken);
        sendPasswordResetEmail(account.getEmail(), token);
    
        return token;
    }
    
    private void sendPasswordResetEmail(String toEmail, String token) {
        String subject = "[HealthyCare] Đặt lại mật khẩu";
        String text = "Xin chào,\n\n"
                + "Bạn đã yêu cầu đặt lại mật khẩu cho tài khoản của mình trên HealthyCare.\n\n"
                + "Nhấp vào liên kết sau để đặt lại mật khẩu:\n"
                + "http://localhost:1999/reset-password?token=" + token + "\n\n"
                + "Liên kết này chỉ có hiệu lực trong vòng 10 phút kể từ thời điểm nhận email này.\n\n"
                + "Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email này.\n\n"
                + "Trân trọng,\n"
                + "HealthyCare";
    
        sendEmail(toEmail, subject, text);
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
            }else{
                accountRepository.delete(emailToken.getAccount());
            }
        }
        return false;
    }
}
