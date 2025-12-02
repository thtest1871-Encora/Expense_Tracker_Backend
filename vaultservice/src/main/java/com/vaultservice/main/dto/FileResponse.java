package com.vaultservice.main.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FileResponse {

    private Long id;
    private String filename;
    private String fileUrl;
    private String description;

   
    private LocalDate date;

    private Long categoryId;

    
    private LocalDateTime createdAt;

    public FileResponse(Long id, String filename, String fileUrl,
                        String description, LocalDate date, Long categoryId,
                        LocalDateTime createdAt) {
        this.id = id;
        this.filename = filename;
        this.fileUrl = fileUrl;
        this.description = description;
        this.date = date;
        this.categoryId = categoryId;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getFilename() { return filename; }
    public String getFileUrl() { return fileUrl; }
    public String getDescription() { return description; }

    public LocalDate getDate() { return date; }

    public Long getCategoryId() { return categoryId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
