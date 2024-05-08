package com.hcmute.HealthyCare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hcmute.HealthyCare.entity.Account;
import com.hcmute.HealthyCare.entity.Doctor;
import com.hcmute.HealthyCare.entity.Patient;
import com.hcmute.HealthyCare.entity.User;
import com.hcmute.HealthyCare.enums.Rolee;
import com.hcmute.HealthyCare.repository.AccountRepository;
import com.hcmute.HealthyCare.repository.DoctorRepository;
import com.hcmute.HealthyCare.repository.PatientRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
@Service
public class UserService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    public User addNewUser(User user) {
        String encoder = passwordEncoder.encode(user.getPassword()); 
        Account account = new Account(user.getEmail(), encoder, user.getAvatar(), user.getRole());
        Account savedAccount = accountRepository.save(account);

        if (user.getRole() == Rolee.ROLE_DOCTOR) {
            Doctor doctor = new Doctor(user.getName(), user.getAddress(), user.getPhone(), user.getBirthday(), user.getGender(), user.getEducation(), user.getWorkplace(), user.getIntroduction(), user.getSpecially(), user.getNumberofyear(), savedAccount);
            doctorRepository.save(doctor);
        } else if (user.getRole() == Rolee.ROLE_PATIENT) {
            Patient patient = new Patient(user.getName(), user.getAddress(), user.getPhone(), user.getBirthday(), user.getGender(), user.getUnderlyingDisease(), savedAccount);
            patientRepository.save(patient);
        }
        return user;
    }

    public Account loadAccount(String email) {
        return accountRepository.findByEmail(email);
    }
    

    public String loginUser(Account account, HttpServletResponse response) { 
        Account existingAccount = accountRepository.findByEmail(account.getEmail());
        if(existingAccount != null) {
            if (!existingAccount.isVerified()) {
                return "Account is not verified";
            }
            if (passwordEncoder.matches(account.getPassword(), existingAccount.getPassword())) {
                String token = jwtService.generateToken(existingAccount.getEmail());
                response.setHeader("Authorization", "Bearer " + token);
                Cookie jwtCookie = new Cookie("jwtToken", token);
                jwtCookie.setHttpOnly(true);
                response.addCookie(jwtCookie);

                return "Success";
            } else {
                return "Password incorrect";
            }
        } else {
            return "Account not found";
        }
    }
}
