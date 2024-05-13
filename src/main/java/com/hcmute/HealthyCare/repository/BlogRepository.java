package com.hcmute.HealthyCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcmute.HealthyCare.entity.Blog;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long>{
    List<Blog> findByNameContainingIgnoreCase(String keyword);
}
