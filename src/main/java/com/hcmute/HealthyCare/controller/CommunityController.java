package com.hcmute.HealthyCare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.websocket.server.PathParam;

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

    @GetMapping(value = "/community/viewBlog")
    public String viewBlogById(@PathParam("blogId") Long id) {
        return "comunity/viewblog";
    }

    @GetMapping(value = "/community/editBlog")
    public String eidtBlogById(@PathParam("blogId") Long id) {
        return "comunity/blog";
    }
}
