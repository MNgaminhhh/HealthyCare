package com.hcmute.HealthyCare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MessageController {

    @GetMapping("/message/{email}")
    public String showRegister(@PathVariable("email") String email, Model model) {
        model.addAttribute("email", email);
        return "message/all";
    }
    @GetMapping("/message")
    public String showRegister(Model model) {
        return "message/all";
    }

}
