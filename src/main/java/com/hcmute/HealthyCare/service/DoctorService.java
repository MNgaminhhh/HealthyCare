package com.hcmute.HealthyCare.service;

import com.hcmute.HealthyCare.entity.Doctor;
import com.hcmute.HealthyCare.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;

    public List<Doctor> find(String keyword) {
        try {
            return doctorRepository.findByNameContainingIgnoreCaseOrSpeciallyContainingIgnoreCase(keyword, keyword);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
