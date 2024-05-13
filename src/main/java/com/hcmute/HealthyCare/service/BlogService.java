package com.hcmute.HealthyCare.service;

import java.util.Collections;
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
    public List<Blog> find(String keyword) {
        try {
            return blogRepository.findByNameContainingIgnoreCase(keyword);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Blog> findAll() {
        try {
            List<Blog> listBlog = blogRepository.findAll();
            if (listBlog != null) {
                Collections.reverse(listBlog);
                return listBlog;
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public Blog updateBlog(Blog newBlog) {
        return blogRepository.save(newBlog);
    }

    public void deleteBlog(Long id) {
        Optional<Blog> blog = blogRepository.findById(id);
        if (blog.isPresent()){
            blogRepository.delete(blog.get());
        }    
    }
}
