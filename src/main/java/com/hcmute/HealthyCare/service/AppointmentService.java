package com.hcmute.HealthyCare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcmute.HealthyCare.entity.Appointment;
import com.hcmute.HealthyCare.repository.AppointmentRepository;

@Service
public class AppointmentService {
    
    @Autowired
    AppointmentRepository appointmentRepository;

    public Appointment createAppointment(Appointment newAppointment) {
        return appointmentRepository.save(newAppointment);
    }
}
