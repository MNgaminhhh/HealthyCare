package com.hcmute.HealthyCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hcmute.HealthyCare.entity.Paragraph;

public interface ParagraphRepository extends JpaRepository<Paragraph, Long> {

    @Query("SELECT p FROM Paragraph p WHERE p.blog.id = :id")
    public Paragraph getParagraphByBlogId(@Param("id") Long id); 
}