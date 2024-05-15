package com.hcmute.HealthyCare.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.hcmute.HealthyCare.entity.Image;
import com.hcmute.HealthyCare.entity.Paragraph;
import com.hcmute.HealthyCare.repository.ImageRepository;
import com.hcmute.HealthyCare.repository.ParagraphRepository;

import java.util.List;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ParagraphRepository paragraphRepository;

    public Image addImageToParagraph(Image image, Paragraph paragraph) {
        Paragraph newParagraph = paragraphRepository.save(paragraph);
        image.setParagraph(newParagraph);
        return imageRepository.save(image);
    }

    public List<Image> getAllImageOfParagraph(Long id) {
        List<Image> listImgaes = imageRepository.findByParagraphId(id);
        return listImgaes;
    }

    public void deleteImage(Image image) {
        if (image != null) {
            imageRepository.delete(image);
        }
    }
}
