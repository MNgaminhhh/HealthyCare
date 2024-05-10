package com.hcmute.HealthyCare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommunityController {
    
    @GetMapping(value = "/community")
    public String openCommunity() {
        return "comunity/community";
    }

    @GetMapping(value = "/community/addBlog")
    public String addBlog() {
        return "comunity/blog";
    }
}
