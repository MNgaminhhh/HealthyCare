package com.hcmute.HealthyCare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MessageController {
    @GetMapping("/message")
    public String showRegister(Model model) {
        return "message/all";
    }
}
