package com.hcmute.HealthyCare.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    
    public User findUserByEmail(String email) {
        Account account = accountRepository.findByEmail(email);
        if (account != null) {
            if (account.getRole() == Rolee.ROLE_DOCTOR) {
                Doctor doctor = doctorRepository.findByAccount(account);
                if (doctor != null) {
                    return new User(account.getEmail(), account.getPassword(), account.getAvatar(), account.getRole(), doctor.getName(), doctor.getAddress(), doctor.getPhone(), doctor.getBirthday(), doctor.getGender(), doctor.getEducation(), doctor.getNumberofyear(), doctor.getWorkplace(), doctor.getIntroduction(), doctor.getSpecially(), null);
                }
            } else if (account.getRole() == Rolee.ROLE_PATIENT) {
                Patient patient = patientRepository.findByAccount(account);
                if (patient != null) {
                    return new User(account.getEmail(), account.getPassword(), account.getAvatar(), account.getRole(), patient.getName(), patient.getAddress(), patient.getPhone(), patient.getBirthday(), patient.getGender(), null, null, null, null, null, patient.getUnderlyingDisease());
                }
            }
        }
        return null;
    }
    public List<User> findDoctorsByNameOrSpecially(String query) {
        List<Doctor> doctors = doctorRepository.findByNameContainingIgnoreCaseOrSpeciallyContainingIgnoreCase(query, query);
        List<User> results = doctors.stream().map(doctor -> {
            Account account = doctor.getAccount();
            return new User(
                    account.getEmail(),
                    account.getPassword(),
                    account.getAvatar(),
                    account.getRole(),
                    doctor.getName(),
                    doctor.getAddress(),
                    doctor.getPhone(),
                    doctor.getBirthday(),
                    doctor.getGender(),
                    doctor.getEducation(),
                    doctor.getNumberofyear(),
                    doctor.getWorkplace(),
                    doctor.getIntroduction(),
                    doctor.getSpecially(),
                    null
            );
        }).collect(Collectors.toList());

        return results;
    }
    public Account saveAccount(Account account){
        accountRepository.save(account);
        return account;
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
    public void changePassword(String userEmail, String oldPassword, String newPassword) throws Exception {
        boolean isPasswordMatch = accountRepository.existsByEmailAndAndPassword(userEmail, oldPassword);
        if (!isPasswordMatch) {
            throw new Exception("Mật khẩu cũ không đúng.");
        }
        try {
            Account account = accountRepository.findByEmail(userEmail);
            account.setPassword(passwordEncoder.encode(newPassword));
            accountRepository.save(account);
        } catch (Exception e) {
            throw new Exception("Đã xảy ra lỗi khi thay đổi mật khẩu.");
        }
    }
    

    
    public Account findAccountByEmail(String email) {
        Optional<Account> account = accountRepository.findById(email);
        if (account.isPresent()) {
            return account.get();
        }
        return null;
    }
    public void saveAvatar(String userEmail, String avatarUrl) {
        Account account = accountRepository.findByEmail(userEmail);
        if (account != null) {
            account.setAvatar(avatarUrl);
            accountRepository.save(account);
        }
    }
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }
    public List<User> getAllUsers() {
        List<Doctor> doctors = doctorRepository.findAll();
        List<Patient> patients = patientRepository.findAll();

        List<User> users = doctors.stream()
            .map(doctor -> {
                Account account = accountRepository.findByEmail(doctor.getAccount().getEmail());
                return new User(account.getEmail(), account.getPassword(), account.getAvatar(), account.getRole(), doctor.getName(), doctor.getAddress(), doctor.getPhone(), doctor.getBirthday(), doctor.getGender(), doctor.getEducation(), doctor.getNumberofyear(), doctor.getWorkplace(), doctor.getIntroduction(), doctor.getSpecially(), null);
            })
            .collect(Collectors.toList());

        users.addAll(patients.stream()
            .map(patient -> {
                Account account = accountRepository.findByEmail(patient.getAccount().getEmail());
                return new User(account.getEmail(), account.getPassword(), account.getAvatar(), account.getRole(), patient.getName(), patient.getAddress(), patient.getPhone(), patient.getBirthday(), patient.getGender(), null, null, null, null, null, patient.getUnderlyingDisease());
            })
            .collect(Collectors.toList()));
        Collections.reverse(users);
        return users;
    }
    public User saveUser(User user) {
        Account account = accountRepository.findByEmail(user.getEmail());
        if (account != null) {
            accountRepository.save(account);
    
            if (user.getRole() == Rolee.ROLE_DOCTOR) {
                Doctor doctor = doctorRepository.findByAccount(account);
                if (doctor != null) {
                    doctor.setName(user.getName());
                    doctor.setPhone(user.getPhone());
                    doctor.setAddress(user.getAddress());
                    doctor.setBirthday(user.getBirthday());
                    doctor.setGender(user.getGender());
                    doctor.setSpecially(user.getSpecially());
                    doctor.setWorkplace(user.getWorkplace());
                    doctor.setNumberofyear(user.getNumberofyear());
                    doctor.setEducation(user.getEducation());
                    doctor.setIntroduction(user.getIntroduction());
                    doctorRepository.save(doctor);
                } else {
                    doctor = new Doctor(user.getName(), user.getAddress(), user.getPhone(), user.getBirthday(), user.getGender(), user.getEducation(), user.getWorkplace(), user.getIntroduction(), user.getSpecially(), user.getNumberofyear(), account);
                    doctorRepository.save(doctor);
                }
            } else if (user.getRole() == Rolee.ROLE_PATIENT) {
                Patient patient = patientRepository.findByAccount(account);
                if (patient != null) {
                    patient.setName(user.getName());
                    patient.setPhone(user.getPhone());
                    patient.setBirthday(user.getBirthday());
                    patient.setGender(user.getGender());
                    patient.setAddress(user.getAddress());
                    patient.setUnderlyingDisease(user.getUnderlyingDisease());
                    patientRepository.save(patient);
                } else {
                    patient = new Patient(user.getName(), user.getAddress(), user.getPhone(), user.getBirthday(), user.getGender(), user.getUnderlyingDisease(), account);
                    patientRepository.save(patient);
                }
            }
            return user;
        } else {
            return null;
        }
    }
    
    public Account getAccountByDoctor(Long dId) {
        Optional<Account> account = accountRepository.findAccountByDoctor(dId);
        if (account.isPresent()) {
            return account.get();
        }
        return null;
    } 

    public Account getAccountByPatient(Long pId) {
        Optional<Account> account = accountRepository.findAccountByPatient(pId);
        if (account.isPresent()) {
            return account.get();
        }
        return null;
    } 
}
