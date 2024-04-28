package com.hcmute.HealthyCare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String showLogin(Model model) {
        return "login/login";
    }
    @GetMapping("/forgot-password")
    public String forgotLogin(Model model) {
        return "login/forgotpassword";
    }
    @GetMapping("/reset-password")
    public String showResetPassword(@RequestParam(name = "token", required = false) String token, Model model) {
        model.addAttribute("token", token);
        return "login/resetpassword";
    }
    @GetMapping("/register")
    public String showRegister(Model model) {
        return "login/register";
    }
}
