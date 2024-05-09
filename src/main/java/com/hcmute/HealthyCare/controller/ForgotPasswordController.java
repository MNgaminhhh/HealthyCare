package com.hcmute.HealthyCare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ForgotPasswordController {
    @GetMapping("/forgot-password")
    public String showLogin(Model model) {
        return "login/forgotpassword";
    }
    @GetMapping("/reset-password")
    public String showVerificationPage(@RequestParam(name = "token", required = false) String token, Model model) {
        model.addAttribute("token", token);
        return "login/resetpassword";    
    }
}
