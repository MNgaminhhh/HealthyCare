package com.hcmute.HealthyCare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcmute.HealthyCare.entity.Image;
import com.hcmute.HealthyCare.entity.Paragraph;
import com.hcmute.HealthyCare.repository.ImageRepository;
import com.hcmute.HealthyCare.repository.ParagraphRepository;

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
}
