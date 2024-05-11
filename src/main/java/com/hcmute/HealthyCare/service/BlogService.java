package com.hcmute.HealthyCare.service;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hcmute.HealthyCare.entity.Blog;
import com.hcmute.HealthyCare.repository.BlogRepository;

@Service
public class BlogService {
    
    @Autowired
    private BlogRepository blogRepository;

    public Blog addBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    public Blog findBlogById(Long id) {
        Optional<Blog> blog = blogRepository.findById(id);
        if (blog.isPresent()) {
            return blog.get();
        }
        return null;
    }

    public List<Blog> findAll() {
        List<Blog> lsitBlog = blogRepository.findAll();
        return lsitBlog;
    }
}
