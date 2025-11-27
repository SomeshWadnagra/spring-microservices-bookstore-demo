package com.chrisbaileydeveloper.bookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookImageStorageService {

    private final S3Client s3Client;

    @Value("${app.book.image.bucket}")
    private String bucketName;

    @Value("${app.book.image.base-url}")
    private String baseUrl;

    @Value("${app.book.image.folder:books}")
    private String folder;

    public String uploadBookImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String originalName = file.getOriginalFilename();
        String extension = "";
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf('.'));
        }

        String key = folder + "/" + UUID.randomUUID() + extension;

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file content", e);
        } catch (S3Exception e) {
            throw new RuntimeException("Failed to upload image to S3: " + e.awsErrorDetails().errorMessage(), e);
        }

        // Public URL – works if bucket objects are public or served via CDN
        return baseUrl + "/" + key;
    }
}
