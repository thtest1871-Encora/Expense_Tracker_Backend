package com.vaultservice.main.service;

import com.vaultservice.main.dto.FileResponse;
import com.vaultservice.main.exception.FileNotFoundException;
import com.vaultservice.main.model.VaultFile;
import com.vaultservice.main.repository.VaultRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class VaultService {

    private final S3Client s3Client;
    private final VaultRepository vaultRepo;

    @Value("${aws.s3.bucket-name}")
    private String bucket;

    @Value("${aws.s3.prefix}")
    private String prefix;

    @Value("${aws.region}")
    private String region;

    public VaultService(S3Client s3Client, VaultRepository vaultRepo) {
        this.s3Client = s3Client;
        this.vaultRepo = vaultRepo;
    }

    public FileResponse uploadFile(Long userId, MultipartFile file,
                                   String description, LocalDate date,
                                   String category) throws IOException {

        String cleanedFilename = file.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-]", "_");

        String key = prefix + "/" + userId + "/" + UUID.randomUUID() + "_" + cleanedFilename;

        PutObjectRequest putReq = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putReq,
                software.amazon.awssdk.core.sync.RequestBody.fromInputStream(
                        file.getInputStream(),
                        file.getSize()
                )
        );

        String s3Url = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;

        VaultFile v = new VaultFile();
        v.setUserId(userId);
        v.setFilename(cleanedFilename);
        v.setFileUrl(s3Url);
        v.setDescription(description);
        v.setDate(date);       // <-- UPDATED
        v.setCategory(category);
        v.setCreatedAt(LocalDateTime.now());

        VaultFile saved = vaultRepo.save(v);

        return new FileResponse(
                saved.getId(),
                saved.getFilename(),
                saved.getFileUrl(),
                saved.getDescription(),
                saved.getDate(),
                saved.getCategory(),
                saved.getCreatedAt()
        );
    }

    public List<VaultFile> listFiles(Long userId) {
        return vaultRepo.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public void deleteFile(Long id) {

        VaultFile f = vaultRepo.findById(id)
                .orElseThrow(() -> new FileNotFoundException("File not found"));

        String key = f.getFileUrl().split(".amazonaws.com/")[1];

        DeleteObjectRequest delReq = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        s3Client.deleteObject(delReq);
        vaultRepo.delete(f);
    }
}
