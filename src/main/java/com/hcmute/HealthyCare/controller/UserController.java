package com.hcmute.HealthyCare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@Controller
public class UserController {
    @GetMapping("/doctor/{email}")
    public String showLogin(@PathVariable String email, Model model) {
        return "user/doctorprofile";
    }
}
