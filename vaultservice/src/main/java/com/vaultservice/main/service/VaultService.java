package com.vaultservice.main.service;

import com.vaultservice.main.dto.FileResponse;
import com.vaultservice.main.exception.FileNotFoundException;
import com.vaultservice.main.model.VaultFile;
import com.vaultservice.main.repository.VaultRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class VaultService {

    private final VaultRepository vaultRepo;
    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    private static final List<String> ALLOWED_MIME_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "image/gif", "application/pdf", "text/plain"
    );

    public VaultService(VaultRepository vaultRepo, S3Client s3Client) {
        this.vaultRepo = vaultRepo;
        this.s3Client = s3Client;
    }

    public FileResponse uploadFile(Long userId, MultipartFile file,
                                   String description, LocalDate date,
                                   Long categoryId) throws IOException {

        if (file.isEmpty()) {
            throw new IOException("Failed to store empty file.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_MIME_TYPES.contains(contentType)) {
            throw new IOException("Invalid file type. Allowed: images, pdf, text.");
        }

        String originalFilename = file.getOriginalFilename();
        String billId = UUID.randomUUID().toString(); // Generate UUID for S3 path
        String keyPath = "bills/" + userId + "/" + billId + "/" + originalFilename;

        PutObjectRequest putOb = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(keyPath)
                .contentType(contentType)
                .build();

        s3Client.putObject(putOb, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        VaultFile v = new VaultFile();
        v.setUserId(userId);
        v.setBillId(billId);
        v.setFilename(originalFilename); // Storing original filename in filename field too for compatibility
        v.setKeyPath(keyPath);
        v.setOriginalName(originalFilename);
        v.setSize(file.getSize());
        v.setType(contentType);
        v.setFileUrl("s3://" + bucketName + "/" + keyPath);
        v.setDescription(description);
        v.setDate(date);
        v.setCategoryId(categoryId);
        v.setCreatedAt(LocalDateTime.now());

        VaultFile saved = vaultRepo.save(v);

        return new FileResponse(
                saved.getId(),
                saved.getOriginalName(),
                saved.getFileUrl(),
                saved.getDescription(),
                saved.getDate(),
                saved.getCategoryId(),
                saved.getCreatedAt()
        );
    }

    public VaultFile getVaultFile(Long id, Long userId) {
        VaultFile f = vaultRepo.findById(id)
                .orElseThrow(() -> new FileNotFoundException("File not found"));

        if (!f.getUserId().equals(userId)) {
             throw new SecurityException("Unauthorized access to file");
        }
        return f;
    }

    public Resource downloadFile(Long id, Long userId) {
        VaultFile f = vaultRepo.findById(id)
                .orElseThrow(() -> new FileNotFoundException("File not found"));

        if (!f.getUserId().equals(userId)) {
             throw new SecurityException("Unauthorized access to file");
        }

        GetObjectRequest getReq = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(f.getKeyPath())
                .build();

        return new InputStreamResource(s3Client.getObject(getReq));
    }

    public List<VaultFile> listFiles(Long userId) {
        return vaultRepo.findByUserIdOrderByCreatedAtDesc(userId);
    }
    
    public List<VaultFile> filterByDateRange(Long userId, LocalDate start, LocalDate end) {
        return vaultRepo.findByUserIdAndDateBetweenOrderByCreatedAtDesc(userId, start, end);
    }

    public List<VaultFile> filterByCategory(Long userId, Long categoryId) {
        return vaultRepo.findByUserIdAndCategoryIdOrderByCreatedAtDesc(userId, categoryId);
    }

    public List<VaultFile> filterByCategoryAndDateRange(Long userId, Long categoryId,
                                                        LocalDate start, LocalDate end) {
        return vaultRepo.findByUserIdAndCategoryIdAndDateBetweenOrderByCreatedAtDesc(
                userId, categoryId, start, end
        );
    }

    public void deleteFile(Long id, Long userId) {
        VaultFile f = vaultRepo.findById(id)
                .orElseThrow(() -> new FileNotFoundException("File not found"));
        
        if (!f.getUserId().equals(userId)) {
             throw new SecurityException("Unauthorized access to file");
        }

        DeleteObjectRequest delReq = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(f.getKeyPath())
                .build();

        s3Client.deleteObject(delReq);
        
        vaultRepo.delete(f);
    }
    
    public void deleteFile(Long id) {
         VaultFile f = vaultRepo.findById(id)
                .orElseThrow(() -> new FileNotFoundException("File not found"));
         
         DeleteObjectRequest delReq = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(f.getKeyPath())
                .build();

        s3Client.deleteObject(delReq);
        vaultRepo.delete(f);
    }
}
