package com.hcmute.HealthyCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hcmute.HealthyCare.entity.Image;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long>{
    
    @Query("SELECT i FROM Image i WHERE i.paragraph.id = :id")
    List<Image> findByParagraphId(@Param("id") Long id);
    
}
