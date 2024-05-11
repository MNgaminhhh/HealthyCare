package com.hcmute.HealthyCare.apicontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.hcmute.HealthyCare.entity.Account;
import com.hcmute.HealthyCare.entity.Blog;
import com.hcmute.HealthyCare.entity.Image;
import com.hcmute.HealthyCare.entity.Paragraph;
import com.hcmute.HealthyCare.service.BlogService;
import com.hcmute.HealthyCare.service.ImageService;
import com.hcmute.HealthyCare.service.ParagraphService;
import com.hcmute.HealthyCare.service.UserService;

import jakarta.websocket.server.PathParam;

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
            
            List<String> listImage = new ArrayList<>();
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

    @GetMapping("/getBlogBy")
    public Blog getBlog(@PathParam("blogId") Long blogId) {
        try {
            Blog blog = blogService.findBlogById(blogId);
            return blog;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/getAllBlog")
    public ResponseEntity<?> getAllBlog() {
        List<Blog> listBlog = blogService.findAll();
        List<Map<String, Object>> listResult = new ArrayList<>();
        for (Blog blog: listBlog) {
            Map<String, Object> item = new HashMap<>();
            item.put("title", blog.getName());
            
            if (blog.getAccount() != null) {
                item.put("email", blog.getAccount().getEmail());
            }
            else {
                item.put("email", null);
            }

            Paragraph paragraph = paragraphService.findParagraphByBlog(blog.getId());
            String content = null;
            if (paragraph!= null) {
                content = paragraph.getContent();
            }
            item.put("content", content);

            listResult.add(item);
        }
        return ResponseEntity.ok().body(listResult);
    }
}
