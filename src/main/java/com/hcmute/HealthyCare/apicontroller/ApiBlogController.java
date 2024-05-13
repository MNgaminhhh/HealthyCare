package com.hcmute.HealthyCare.apicontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.hcmute.HealthyCare.entity.Account;
import com.hcmute.HealthyCare.entity.Blog;
import com.hcmute.HealthyCare.entity.Comment;
import com.hcmute.HealthyCare.entity.Image;
import com.hcmute.HealthyCare.entity.Paragraph;
import com.hcmute.HealthyCare.enums.Rolee;
import com.hcmute.HealthyCare.service.BlogService;
import com.hcmute.HealthyCare.service.CommentService;
import com.hcmute.HealthyCare.service.ImageService;
import com.hcmute.HealthyCare.service.ParagraphService;
import com.hcmute.HealthyCare.service.UserService;

import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/api")
public class ApiBlogController {
    
    @Autowired
    private BlogService blogService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private ParagraphService paragraphService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private CommentService commentService;

    @PostMapping("/createNewBlog")
    public ResponseEntity<Blog> processJson(@RequestBody JsonNode jsonNode) {
            
            List<String> listImage = new ArrayList<>();
            JsonNode files = jsonNode.get("files");
            if (files != null && files.isArray()) {
                for (JsonNode file: files) {
                    String name = file.get("name").asText();
                    listImage.add(name);
                }
            }

            String title = jsonNode.get("title").asText();
            String email = jsonNode.get("email").asText().trim();
            String content = jsonNode.get("content").asText();
            
            Paragraph paragraph = new Paragraph();
            Blog blog = new Blog();
            
            blog.setName(title);
            Account account = userService.loadAccount(email);
            blog.setAccount(account);
            
            paragraph.setContent(content);
            Paragraph newParagraph = paragraphService.addParagraphToBlog(paragraph, blog);
            
            for (String imageUrl: listImage) {
                Image newImage = new Image();
                newImage.setUrl(imageUrl);
                newImage.setCaption(null);
                @SuppressWarnings("unused")
                final Image image = imageService.addImageToParagraph(newImage, newParagraph);
            }
            
            blogService.addBlog(blog);

            return new ResponseEntity<>(blog, HttpStatus.CREATED);
    }

    @GetMapping("/getBlogBy")
    public ResponseEntity<?> getBlog(@PathParam("blogId") Long blogId) {
        Blog blog = blogService.findBlogById(blogId);
        Map<String, Object> item = new HashMap<>();
        if (blog != null) {
            String tite = blog.getName();
            String email = blog.getAccount().getEmail();
            Account account = userService.loadAccount(email);
            Rolee role = account.getRole();
            String name = null;
            if (role == Rolee.ROLE_DOCTOR) {
                name = account.getDoctor().getName();
            } else if (role == Rolee.ROLE_PATIENT) {
                name = account.getPatient().getName();
            }

            Paragraph paragraph = paragraphService.findParagraphByBlog(blogId);
            String content = paragraph.getContent();

            List<Image> listImages = imageService.getAllImageOfParagraph(paragraph.getId());
            List<String> urlList = new ArrayList<>();
            for (Image image: listImages) {
                urlList.add(image.getUrl());
            }

            item.put("title", tite);
            item.put("content", content);
            item.put("name", name);
            item.put("avt", account.getAvatar());
            item.put("files", urlList);

            return ResponseEntity.ok().body(item);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/getAllBlog")
    public ResponseEntity<?> getAllBlog() {
        List<Blog> listBlog = blogService.findAll();
        List<Map<String, Object>> listResult = new ArrayList<>();
        for (Blog blog: listBlog) {
            Map<String, Object> item = new HashMap<>();
            item.put("blogId", blog.getId());
            item.put("title", blog.getName());
            
            if (blog.getAccount() != null) {
                item.put("email", blog.getAccount().getEmail());
            }
            else {
                item.put("email", null);
            }

            Paragraph paragraph = paragraphService.findParagraphByBlog(blog.getId());
            String content = null;
            Long pId = null;
            if (paragraph!= null) {
                content = paragraph.getContent();
                pId = paragraph.getId();
            }
            item.put("pId", pId);
            item.put("content", content);

            List<Image> listImages = imageService.getAllImageOfParagraph(pId);
            Image image = listImages.get(0);
            item.put("imageHeader", image.getUrl());

            listResult.add(item);
        }
        return ResponseEntity.ok().body(listResult);
    }

    @PostMapping("/editblog")
    public ResponseEntity<?> updateBlog(@RequestBody JsonNode jsonNode) {
        Long id = jsonNode.get("id").asLong();

        Blog blog = blogService.findBlogById(id);

        String title = jsonNode.get("title").asText();
        String content = jsonNode.get("content").asText();
            
        Paragraph paragraph = paragraphService.findParagraphByBlog(id);
            
        paragraph.setContent(content);

        @SuppressWarnings("unused")
        Paragraph newParagraph = paragraphService.updateParagraph(paragraph);
        blog.setName(title);
            
        Blog newBlog = blogService.updateBlog(blog);
        if (newBlog != null) {
            return ResponseEntity.ok().body("Chỉnh sửa bài viết thành công!");
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/deleteBlog")
    public ResponseEntity<?> deleteBlog(@PathParam("blogId") Long blogId) {
        Paragraph paragraph = paragraphService.findParagraphByBlog(blogId);
        List<Image> listImages = imageService.getAllImageOfParagraph(paragraph.getId());
        for (Image image: listImages) {
            imageService.deleteImage(image);
        }
        List<Comment> comments = commentService.getCommentByBlog(blogId);
        for (Comment comment: comments) {
            commentService.deleteComment(comment);
        }
        paragraphService.deleteParagraph(paragraph);
        blogService.deleteBlog(blogId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
