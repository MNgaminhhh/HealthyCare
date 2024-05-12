package com.hcmute.HealthyCare.controller;

import com.hcmute.HealthyCare.entity.Account;
import com.hcmute.HealthyCare.entity.Doctor;
import com.hcmute.HealthyCare.entity.Patient;
import com.hcmute.HealthyCare.repository.DoctorRepository;
import com.hcmute.HealthyCare.repository.PatientRepository;
import com.hcmute.HealthyCare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @GetMapping("/profile")
    public ModelAndView showProfile() {
        ModelAndView mav = new ModelAndView("profile");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Account account = userService.loadAccount(email);

        Doctor doctor = doctorRepository.findByAccount(account);
        if (doctor != null) {
            mav.addObject("profileInfo", "Bác Sĩ: " + doctor.getName());
            return mav;
        }

        Patient patient = patientRepository.findByAccount(account);
        if (patient != null) {
            mav.addObject("profileInfo", "Bệnh nhân: " + patient.getName());
            return mav;
        }

        mav.addObject("profileInfo", "Chưa đăng nhập tài khoản vào hệ thống");
        return mav;
    }
    @GetMapping("/updateProfile")
    public ModelAndView showUpdateProfile() {
        ModelAndView mav = new ModelAndView("updateProfile");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Account account = userService.loadAccount(email);

        Doctor doctor = doctorRepository.findByAccount(account);
        if (doctor != null) {
            mav.addObject("doctor", doctor);
            return mav;
        }

        Patient patient = patientRepository.findByAccount(account);
        if (patient != null) {
            mav.addObject("patient", patient);
            return mav;
        }

        return mav;
    }
    @PostMapping("/updateProfile")
    public String updateProfile(@ModelAttribute Doctor doctor, @ModelAttribute Patient patient) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Account account = userService.loadAccount(email);

        Doctor existingDoctor = doctorRepository.findByAccount(account);
        if (existingDoctor != null) {
            // Update fields of existingDoctor with the data from doctor
            existingDoctor.setName(doctor.getName());
            existingDoctor.setAddress(doctor.getAddress());
            existingDoctor.setPhone(doctor.getPhone());
            existingDoctor.setBirthday(doctor.getBirthday());
            existingDoctor.setGender(doctor.getGender());
            existingDoctor.setEducation(doctor.getEducation());
            existingDoctor.setWorkplace(doctor.getWorkplace());
            existingDoctor.setIntroduction(doctor.getIntroduction());
            existingDoctor.setSpecially(doctor.getSpecially());
            existingDoctor.setNumberofyear(doctor.getNumberofyear());
            // ...
            doctorRepository.save(existingDoctor);
            return "redirect:/profile";
        }

        Patient existingPatient = patientRepository.findByAccount(account);
        if (existingPatient != null) {
            // Update fields of existingPatient with the data from patient
            existingPatient.setName(patient.getName());
            existingPatient.setAddress(patient.getAddress());
            existingPatient.setPhone(patient.getPhone());
            existingPatient.setBirthday(patient.getBirthday());
            existingPatient.setGender(patient.getGender());
            existingPatient.setUnderlyingDisease(patient.getUnderlyingDisease());
            // ...
            patientRepository.save(existingPatient);
            return "redirect:/profile";
        }

        return "redirect:/profile";
    }
}