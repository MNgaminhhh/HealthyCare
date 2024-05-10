package com.hcmute.HealthyCare.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.firebase.cloud.StorageClient;

import java.io.*;

@Service
public class FirebaseStorageService {
    public String uploadImage(MultipartFile multipartFile) throws IOException {
        String fileName = "images/"+multipartFile.getOriginalFilename();
        String contentType = multipartFile.getContentType();
        BlobId blobId = BlobId.of("healthycare-16dac.appspot.com", fileName);
        BlobInfo bloInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();
        Storage storage = StorageClient.getInstance().bucket("healthycare-16dac.appspot.com").getStorage();
        Blob blob = storage.create(bloInfo, multipartFile.getBytes());
        return String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media", blob.getBucket(), blob.getName());
    }
}