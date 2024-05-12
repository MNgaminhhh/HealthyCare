package com.hcmute.HealthyCare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.websocket.server.PathParam;

@Controller
public class AppointmentController {
    
    @GetMapping(value = "/schedule")
    public String viewSchedule(@PathParam("email") String email) {
        return "appointment/scheduled";
    }
}
