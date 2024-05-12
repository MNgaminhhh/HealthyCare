package com.hcmute.HealthyCare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcmute.HealthyCare.entity.Comment;
import com.hcmute.HealthyCare.repository.CommentRepository;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;
    
    public Comment addComment(Comment newComment) {
        return commentRepository.save(newComment);
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public List<Comment> getCommentsByBlog(Long blogId) {
        return commentRepository.findCommentByBlog(blogId);
    }
}
