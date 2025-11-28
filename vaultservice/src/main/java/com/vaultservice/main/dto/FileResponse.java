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

    private String category;

    
    private LocalDateTime createdAt;

    public FileResponse(Long id, String filename, String fileUrl,
                        String description, LocalDate date, String category,
                        LocalDateTime createdAt) {
        this.id = id;
        this.filename = filename;
        this.fileUrl = fileUrl;
        this.description = description;
        this.date = date;
        this.category = category;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getFilename() { return filename; }
    public String getFileUrl() { return fileUrl; }
    public String getDescription() { return description; }

    public LocalDate getDate() { return date; }

    public String getCategory() { return category; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
