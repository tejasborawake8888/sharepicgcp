package com.sharepic.post.service;


import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostImageStorageService {
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    private final Storage storage;
    private final String bucketName;

    public PostImageStorageService(
            @Value("${gcp.storage.bucket-name}") String bucketName){
        this.storage = StorageOptions.getDefaultInstance().getService();
        this.bucketName = bucketName;
    }

    public String uploadPostImage(MultipartFile imageFile){
        if(imageFile == null || imageFile.isEmpty()){
            throw new IllegalArgumentException("Profile picture cannot be empty");
        }
        if(imageFile.getSize() > MAX_FILE_SIZE){
            throw new IllegalArgumentException(
                    "Profile picture must not exceed 5 MB");
        }
        String contentType = imageFile.getContentType();
        String extension = switch (contentType == null ? "": contentType.toLowerCase()){
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            default -> throw new IllegalArgumentException("Only JPEG,PNG images are allowed");
        };
        String objectName = "post-images/" + UUID.randomUUID() + extension;
        try{
            BlobInfo blobInfo = BlobInfo.newBuilder(bucketName,objectName).setContentType(contentType).build();
            storage.create(blobInfo,imageFile.getBytes());
            return "https://storage.googleapis.com/" + bucketName + "/" + objectName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload post image", e);
        }
    }

}
