package com.hcmute.HealthyCare.apicontroller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.hcmute.HealthyCare.entity.Appointment;
import com.hcmute.HealthyCare.entity.Doctor;
import com.hcmute.HealthyCare.entity.Patient;
import com.hcmute.HealthyCare.enums.AppointmentStatus;
import com.hcmute.HealthyCare.service.AppointmentService;
import com.hcmute.HealthyCare.service.UserService;

@RestController
@RequestMapping("/api")
public class ApiAppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserService userService;

    @PostMapping("/createAppointment")
    public ResponseEntity<?> createAppointment(@RequestBody JsonNode jsonData) {
        String notes = jsonData.get("notes").asText();
        AppointmentStatus status = AppointmentStatus.SCHEDULED;
        String patient_email = jsonData.get("patient").asText();
        String doctor_email = jsonData.get("doctor").asText();
        String date = jsonData.get("date").asText();
        String time = jsonData.get("time").asText();
        String datetime = date+" "+time;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime localDateTime = null;
        try {
            localDateTime = LocalDateTime.parse(datetime, formatter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Patient pId = userService.loadAccount(patient_email).getPatient();
        Doctor dId = userService.loadAccount(doctor_email).getDoctor();


        Appointment newAppointment = new Appointment();
        newAppointment.setDatetime(localDateTime);
        newAppointment.setNotes(notes);
        newAppointment.setStatus(status);
        newAppointment.setDoctor(dId);
        newAppointment.setPatient(pId);

        Appointment createdAppointment = appointmentService.createAppointment(newAppointment);
        
        if (createdAppointment!= null) {
            Map<String, Object> map = new HashMap<>();
            map.put("date", createdAppointment.getDatetime());
            map.put("notes", createdAppointment.getNotes());
            map.put("status", createdAppointment.getStatus());
            map.put("doctor", createdAppointment.getDoctor().getId());
            map.put("patient", createdAppointment.getPatient().getId());
            return ResponseEntity.ok().body(map);
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }
}
