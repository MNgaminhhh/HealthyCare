package com.hcmute.HealthyCare.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hcmute.HealthyCare.entity.Account;
import com.hcmute.HealthyCare.entity.Doctor;
import com.hcmute.HealthyCare.entity.Patient;
import com.hcmute.HealthyCare.entity.User;
import com.hcmute.HealthyCare.entity.UserInfoDetails;
import com.hcmute.HealthyCare.enums.Rolee;
import com.hcmute.HealthyCare.repository.AccountRepository;
import com.hcmute.HealthyCare.repository.DoctorRepository;
import com.hcmute.HealthyCare.repository.EmailTokenRepository;
import com.hcmute.HealthyCare.repository.PatientRepository;

@Service
public class UserService implements UserDetailsService{
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private EmailTokenRepository emailTokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    public Doctor findDoctorByEmail(String email) {
        Account account = accountRepository.findByEmail(email);
        if (account != null && account.getRole() == Rolee.ROLE_DOCTOR) {
            return doctorRepository.findByAccount(account);
        }
        return null;
    }

    public Patient findPatientByEmail(String email) {
        Account account = accountRepository.findByEmail(email);
        if (account != null && account.getRole() == Rolee.ROLE_PATIENT) {
            return patientRepository.findByAccount(account);
        }
        return null;
    }
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
    public Account getUserByEmail(String email) {
       return accountRepository.findByEmail(email);
    }
    
    public String loginUser(String email, String password) {
        Account account = accountRepository.findByEmail(email);
        if (account != null && passwordEncoder.matches(password, account.getPassword())) {
            return jwtService.generateToken(email);
        }
        return null;
    }

    public UserInfoDetails loadUserByUsername(String email) {
        Account account = accountRepository.findByEmail(email);
        if (account != null) {
            return new UserInfoDetails(account);
        }
        return null;
    }
    public void resetPassword(String email, String newPassword) {
        Account account = accountRepository.findByEmail(email);
        if (account != null) {
            account.setPassword(passwordEncoder.encode(newPassword));
            accountRepository.save(account);
        }else {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
    }
    
}
