package com.hcmute.HealthyCare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ManagePersonalController {
    @GetMapping("/setting")
    public String showRegister(Model model) {
        return "user/profile";
    }
    @GetMapping("/profile")
    public String showProfile(Model model) {
        return "user/person";
    }
}
