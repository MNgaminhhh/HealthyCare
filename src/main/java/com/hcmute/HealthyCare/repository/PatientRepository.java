package com.hcmute.HealthyCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcmute.HealthyCare.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long>  {

    
}
