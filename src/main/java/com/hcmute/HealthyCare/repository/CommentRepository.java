package com.hcmute.HealthyCare.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hcmute.HealthyCare.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{
    
    @Query("SELECT c FROM Comment c WHERE c.blog.id =  :id")
    List<Comment> findCommentByBlog(@Param("id") Long blogId);
}
