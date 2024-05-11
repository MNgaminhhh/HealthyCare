package com.hcmute.HealthyCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcmute.HealthyCare.entity.Blog;

public interface BlogRepository extends JpaRepository<Blog, Long>{
    
}
