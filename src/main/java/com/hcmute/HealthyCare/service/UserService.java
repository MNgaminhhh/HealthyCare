package com.hcmute.HealthyCare.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import com.hcmute.HealthyCare.entity.Account;
import com.hcmute.HealthyCare.entity.Doctor;
import com.hcmute.HealthyCare.entity.Patient;
import com.hcmute.HealthyCare.entity.User;
import com.hcmute.HealthyCare.enums.Rolee;
import com.hcmute.HealthyCare.repository.AccountRepository;
import com.hcmute.HealthyCare.repository.DoctorRepository;
import com.hcmute.HealthyCare.repository.PatientRepository;

import java.io.IOException;
import java.net.Authenticator;
import java.util.ArrayList;

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

//    @Autowired
//    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;

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


    public String loginUser(Account account, HttpServletResponse response, HttpServletRequest request) {
        Account existingAccount = accountRepository.findByEmail(account.getEmail());
        if(existingAccount != null) {
            System.out.println("Found account: " + existingAccount);
            if (!existingAccount.isVerified()) {
                System.out.println("Account is not verified");
                return "Account is not verified";
            }
            if (passwordEncoder.matches(account.getPassword(), existingAccount.getPassword())) {
                System.out.println("Login success");

                // Tạo một đối tượng Authentication mới
                Authentication auth = new UsernamePasswordAuthenticationToken(existingAccount.getEmail(), existingAccount.getPassword(), new ArrayList<>());

                // Đặt đối tượng Authentication mới vào SecurityContext
                SecurityContextHolder.getContext().setAuthentication(auth);

                // Lưu SecurityContext vào HttpSession
                HttpSession session = request.getSession(true);
                session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

                return "Success";
            } else {
                System.out.println("Password incorrect");
                return "Password incorrect";
            }
        } else {
            System.out.println("Account not found");
            return "Account not found";
        }
    }
}
