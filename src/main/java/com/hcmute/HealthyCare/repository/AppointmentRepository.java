package com.hcmute.HealthyCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcmute.HealthyCare.entity.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>{
    
}
