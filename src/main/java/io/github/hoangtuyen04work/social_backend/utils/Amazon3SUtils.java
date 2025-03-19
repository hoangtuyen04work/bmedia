package io.github.hoangtuyen04work.social_backend.utils;

import io.github.hoangtuyen04work.social_backend.repositories.AmazonS3Client;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class Amazon3SUtils {
    @Autowired
    AmazonS3Client amazonS3Client;
    public String addImageS3(MultipartFile multipartFile){
        return amazonS3Client.uploadFile(multipartFile);
    }
}