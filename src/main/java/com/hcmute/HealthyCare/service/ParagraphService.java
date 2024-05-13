package com.hcmute.HealthyCare.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcmute.HealthyCare.entity.Blog;
import com.hcmute.HealthyCare.entity.Disease;
import com.hcmute.HealthyCare.entity.Paragraph;
import com.hcmute.HealthyCare.repository.BlogRepository;
import com.hcmute.HealthyCare.repository.ParagraphRepository;

@Service
public class ParagraphService {
    
    @Autowired
    private ParagraphRepository paragraphRepository;

    @Autowired
    private BlogRepository blogRepository;
    public Paragraph addNewParagraph(Paragraph paragraph) {
        String content = paragraph.getContent();
        String title = paragraph.getTitle();
        Blog blog = null;
        Disease disease = null;
        Paragraph newParagraph = new Paragraph();
        newParagraph.setContent(content);
        newParagraph.setBlog(blog);
        newParagraph.setContent(content);
        newParagraph.setDisease(disease);
        newParagraph.setTitle(title);
        return paragraphRepository.save(newParagraph);
    }

    public Paragraph addParagraphToBlog(Paragraph paragraph, Blog blog) {
        Blog newBlog = blogRepository.save(blog);
        paragraph.setBlog(newBlog);
        return paragraphRepository.save(paragraph);
    }

    public Paragraph findParagraphById(Long id) {
        Optional<Paragraph> newParagraph = paragraphRepository.findById(id);
        if (newParagraph.isPresent()) {
            return newParagraph.get();
        }
        return null;
    }

    public Paragraph findParagraphByBlog(Long blogId) {
        Paragraph paragraph = paragraphRepository.getParagraphByBlogId(blogId);
        return paragraph;
    }

    public Paragraph updateParagraph(Paragraph newParagraph) {
        return paragraphRepository.save(newParagraph);
    }
}
