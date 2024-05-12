package com.hcmute.HealthyCare.apicontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.hcmute.HealthyCare.service.AppointmentService;

@RestController
@RequestMapping("/api")
public class ApiAppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/createAppointment")
    public ResponseEntity<?> createAppointment(@RequestBody JsonNode jsonData) {
        return null;
    }
}
