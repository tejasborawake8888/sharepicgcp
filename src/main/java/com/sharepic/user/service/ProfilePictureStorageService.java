package com.sharepic.user.service;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class ProfilePictureStorageService {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    private final Storage storage;
    private final String bucketName;

    public ProfilePictureStorageService(
            @Value("${gcp.storage.bucket-name}") String bucketName){
        this.storage = StorageOptions.getDefaultInstance().getService();
        this.bucketName = bucketName;
    }

    public String uploadProfilePic(MultipartFile profilePic){
        if(profilePic == null || profilePic.isEmpty()){
            throw new IllegalArgumentException("Profile picture cannot be empty");
        }
        if(profilePic.getSize() > MAX_FILE_SIZE){
            throw new IllegalArgumentException(
                    "Profile picture must not exceed 5 MB");
        }
        String contentType = profilePic.getContentType();
        String extension = switch (contentType == null ? "": contentType.toLowerCase()){
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            default -> throw new IllegalArgumentException("Only JPEG,PNG images are allowed");
        };
        String objectName = "profile-pictures/" + UUID.randomUUID() + extension;
        try{
            BlobInfo blobInfo = BlobInfo.newBuilder(bucketName,objectName).setContentType(contentType).build();
            storage.create(blobInfo,profilePic.getBytes());
            return "https://storage.googleapis.com/" + bucketName + "/" + objectName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
