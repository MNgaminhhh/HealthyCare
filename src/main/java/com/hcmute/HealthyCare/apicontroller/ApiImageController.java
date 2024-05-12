package com.hcmute.HealthyCare.apicontroller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcmute.HealthyCare.entity.Image;
import com.hcmute.HealthyCare.entity.Paragraph;
import com.hcmute.HealthyCare.service.ImageService;
import com.hcmute.HealthyCare.service.ParagraphService;

import jakarta.websocket.server.PathParam;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiImageController {

    @Autowired
    private ImageService imageService;
    @Autowired
    private ParagraphService paragraphService;
    
    @GetMapping("/getImages")
    public List<Image> getImageByPid(@PathParam("pId") Long pId) {
        try {
            List<Image> listImages = imageService.getAllImageOfParagraph(pId);
            return listImages;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }
}
