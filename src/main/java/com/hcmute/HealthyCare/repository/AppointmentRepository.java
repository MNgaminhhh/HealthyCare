package com.hcmute.HealthyCare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hcmute.HealthyCare.entity.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>{
    
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :id")
    List<Appointment> getAppointmentOfDoctor(@Param("id") Long dId);

    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :id")
    List<Appointment> getAppointmentOfPatient(@Param("id") Long pId);
}
