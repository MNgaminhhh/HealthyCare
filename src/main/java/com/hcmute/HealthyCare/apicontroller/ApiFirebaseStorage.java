package com.hcmute.HealthyCare.apicontroller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hcmute.HealthyCare.service.FirebaseStorageService;

@RestController
@RequestMapping("/api")
public class ApiFirebaseStorage {
    
    @Autowired
    private FirebaseStorageService firebaseStorageService;
    
    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        return firebaseStorageService.uploadImage(file);
    }
    @PostMapping("/uploadavatar")
    public String uploadAvatar(@RequestParam("file") MultipartFile file) throws IOException{
        return firebaseStorageService.uploadImage(file);
    }
    
}
