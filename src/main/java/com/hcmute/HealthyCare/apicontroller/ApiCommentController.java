package com.hcmute.HealthyCare.apicontroller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.hcmute.HealthyCare.entity.Account;
import com.hcmute.HealthyCare.entity.Blog;
import com.hcmute.HealthyCare.entity.Comment;
import com.hcmute.HealthyCare.enums.Rolee;
import com.hcmute.HealthyCare.service.BlogService;
import com.hcmute.HealthyCare.service.CommentService;
import com.hcmute.HealthyCare.service.UserService;

import jakarta.websocket.server.PathParam;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class ApiCommentController {
    
    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @PostMapping("/createNewComment")
    public ResponseEntity<?> createNewComment(@RequestBody JsonNode jsonNode) {
        try {
            String content = jsonNode.get("content").asText().trim();
            String email = jsonNode.get("email").asText().trim();
            Long blogId = jsonNode.get("blogId").asLong();
            Blog blog = blogService.findBlogById(blogId);
            Account account = userService.loadAccount(email);
            
            Comment newComment = new Comment();
            newComment.setContent(content);
            newComment.setAccount(account);
            newComment.setBlog(blog);
            newComment.setCreatedAt(LocalDateTime.now());
            
            Comment addedComment = commentService.addComment(newComment);
            Map<String, Object> responesMap = new HashMap<>();
            LocalDateTime localDateTime = addedComment.getCreatedAt();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String formattedDateTime = localDateTime.format(formatter);
            responesMap.put("content", content);
            responesMap.put("time", formattedDateTime);
            responesMap.put("avt", account.getAvatar());
            return new ResponseEntity<>(responesMap, HttpStatus.CREATED);
        } catch (Exception e) { 
            e.printStackTrace();
            return null;
        }  
    }

    @GetMapping("/getCommentByBlog")
    public ResponseEntity<?> getCommentByBlog(@PathParam("blogId") Long blogId) {
        List<Map<String, Object>> listMaps = new ArrayList<>();
        List<Comment> comments = commentService.getCommentByBlog(blogId);
        try {
            for (Comment c: comments) {
                Map<String, Object> item = new HashMap<>();
                String content = c.getContent();
                Account account = c.getAccount();
                String name = null;
                if (account.getRole() == Rolee.ROLE_DOCTOR) {
                    account.getDoctor().getName();
                }
                else if (account.getRole() == Rolee.ROLE_PATIENT) {
                    account.getPatient().getName();
                }
                String urlAvt = account.getAvatar();
                LocalDateTime localDateTime = c.getCreatedAt();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                String formattedDateTime = localDateTime.format(formatter);

                item.put("content", content);
                item.put("name", name);
                item.put("time", formattedDateTime);
                item.put("avt", urlAvt);

                listMaps.add(item);
            }
            return ResponseEntity.ok().body(listMaps);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().build();
        }
    }
}
