package com.hcmute.HealthyCare.apicontroller;

import com.hcmute.HealthyCare.entity.Blog;
import com.hcmute.HealthyCare.entity.Doctor;
import com.hcmute.HealthyCare.entity.User;
import com.hcmute.HealthyCare.service.BlogService;
import com.hcmute.HealthyCare.service.DoctorService;
import com.hcmute.HealthyCare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/search")
public class ApiSearchController {
    @Autowired
    private BlogService blogService;

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private UserService userService;
    @GetMapping
    public ResponseEntity<List<Object>> search(@RequestParam("q") String query) {
        List<Blog> symptoms = blogService.find(query);
        List<User> doctors = userService.findDoctorsByNameOrSpecially(query);
        List<Object> results = new ArrayList<>();
        results.addAll(symptoms);
        results.addAll(doctors);

        return ResponseEntity.ok(results);
    }
}
