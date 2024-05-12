package com.hcmute.HealthyCare.service;

import com.hcmute.HealthyCare.entity.Account;
import com.hcmute.HealthyCare.entity.Doctor;
import com.hcmute.HealthyCare.entity.Patient;
import com.hcmute.HealthyCare.repository.AccountRepository;
import com.hcmute.HealthyCare.repository.DoctorRepository;
import com.hcmute.HealthyCare.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email);
        if (account == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        Patient patient = patientRepository.findByAccount(account);
        if (patient != null) {
            return new org.springframework.security.core.userdetails.User(
                    account.getEmail(), account.getPassword(), new ArrayList<>()
            );
        }

        Doctor doctor = doctorRepository.findByAccount(account);
        if (doctor != null) {
            return new org.springframework.security.core.userdetails.User(
                    account.getEmail(), account.getPassword(), new ArrayList<>()
            );
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}