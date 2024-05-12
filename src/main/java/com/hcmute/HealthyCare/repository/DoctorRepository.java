package com.hcmute.HealthyCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcmute.HealthyCare.entity.Account;
import com.hcmute.HealthyCare.entity.Doctor;
import com.hcmute.HealthyCare.entity.Patient;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Doctor findByAccount(Account account);
}
