package com.hcmute.HealthyCare.apicontroller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.hcmute.HealthyCare.entity.Account;
import com.hcmute.HealthyCare.entity.Blog;
import com.hcmute.HealthyCare.entity.Comment;
import com.hcmute.HealthyCare.service.BlogService;
import com.hcmute.HealthyCare.service.CommentService;
import com.hcmute.HealthyCare.service.UserService;

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
    public ResponseEntity<Comment> createNewComment(@RequestBody JsonNode jsonNode) {
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

            return new ResponseEntity<>(commentService.addComment(newComment), HttpStatus.OK);
            } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
         }
        
    }
}
