package com.hcmute.HealthyCare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    @GetMapping("/doctor/{email}")
    public String showLogin(@PathVariable String email, Model model) {
        return "user/doctorprofile";
    }
    @GetMapping("/search")
    public String showVerificationPage(@RequestParam(name = "q", required = false) String q, Model model) {
        model.addAttribute("q", q);
        return "search/searchall";
    }
}
