package com.hcmute.HealthyCare.apicontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcmute.HealthyCare.entity.Paragraph;
import com.hcmute.HealthyCare.service.ParagraphService;

import jakarta.websocket.server.PathParam;

import java.util.List;;

@RestController
@RequestMapping("/api")
public class ApiParagraphController {
    private final ParagraphService paragraphService;

    @Autowired
    public ApiParagraphController(ParagraphService paragraphService) {
        this.paragraphService = paragraphService;
    }
    @PostMapping("/addParagraph")
    public ResponseEntity<Paragraph> addParagraph(@RequestBody Paragraph paragraph) {
        Paragraph saveParagraph = paragraphService.addNewParagraph(paragraph);
        return new ResponseEntity<>(saveParagraph, HttpStatus.CREATED);
    }

    @GetMapping("/getParagraphBy")
    public Paragraph getParagraphs(@PathParam("blogId") Long blogId) {
        try {
            Paragraph paragraph = paragraphService.findParagraphByBlog(blogId);
            return paragraph;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }
}
