package com.hcmute.HealthyCare.apicontroller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.hcmute.HealthyCare.entity.Account;
import com.hcmute.HealthyCare.entity.Blog;
import com.hcmute.HealthyCare.entity.Image;
import com.hcmute.HealthyCare.entity.Paragraph;
import com.hcmute.HealthyCare.service.BlogService;
import com.hcmute.HealthyCare.service.ImageService;
import com.hcmute.HealthyCare.service.ParagraphService;
import com.hcmute.HealthyCare.service.UserService;

@RestController
@RequestMapping("/api")
public class ApiBlogController {
    
    @Autowired
    private BlogService blogService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private ParagraphService paragraphService;

    @Autowired
    private ImageService imageService;

    @PostMapping("/createNewBlog")
    public ResponseEntity<Blog> processJson(@RequestBody JsonNode jsonNode) {
            
            ArrayList<String> listImage = new ArrayList<>();
            JsonNode files = jsonNode.get("files");
            if (files != null && files.isArray()) {
                for (JsonNode file: files) {
                    String name = file.get("name").asText();
                    listImage.add(name);
                }
            }

            String title = jsonNode.get("title").asText();
            String email = jsonNode.get("email").asText().trim();
            String content = jsonNode.get("content").asText();
            
            System.out.println(email);
            
            Paragraph paragraph = new Paragraph();
            Blog blog = new Blog();
            
            blog.setName(title);
            Account account = userService.loadAccount(email);
            blog.setAccount(account);
            
            paragraph.setContent(content);
            Paragraph newParagraph = paragraphService.addParagraphToBlog(paragraph, blog);
            
            for (String imageUrl: listImage) {
                Image newImage = new Image();
                newImage.setUrl(imageUrl);
                newImage.setCaption(null);
                @SuppressWarnings("unused")
                final Image image = imageService.addImageToParagraph(newImage, newParagraph);
            }
            
            blogService.addBlog(blog);

            return new ResponseEntity<>(blog, HttpStatus.CREATED);
    }
}
