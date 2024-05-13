package com.hcmute.HealthyCare.service;

import java.util.List;
import java.util.Optional;

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

    public List<Appointment> getAppointmentByDoctor(Long dId) {
        List<Appointment> appointments = appointmentRepository.getAppointmentOfDoctor(dId);
        return appointments;
    }

    public List<Appointment> getAppointmentByPatient(Long pid) {
        List<Appointment> appointments = appointmentRepository.getAppointmentOfPatient(pid);
        return appointments;
    }

    public void deleteAppointment(Long id) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        if (appointment.isPresent()) {
            appointmentRepository.delete(appointment.get());
        }
    } 

    public Appointment getById(Long id) {
        return appointmentRepository.findById(id).get();
    }

    public Appointment editAppointment(Appointment newAppointment) {
        return appointmentRepository.save(newAppointment);
    }
}
