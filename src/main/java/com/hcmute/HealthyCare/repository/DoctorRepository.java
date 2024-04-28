package com.hcmute.HealthyCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hcmute.HealthyCare.entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    
}
