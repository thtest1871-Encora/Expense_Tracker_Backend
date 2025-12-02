package com.vaultservice.main.controller;

import com.vaultservice.main.dto.FileResponse;
import com.vaultservice.main.model.VaultFile;
import com.vaultservice.main.service.VaultService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bills")
public class VaultController {

    private final VaultService vaultService;

    public VaultController(VaultService vaultService) {
        this.vaultService = vaultService;
    }

    @PostMapping("/upload")
    public ResponseEntity<FileResponse> upload(
            @RequestHeader("X-User-Id") Long userId,
            @RequestPart("file") MultipartFile file,
            @RequestParam("billDescription") String description,
            @RequestParam(value = "date", required = false) String date,
            @RequestParam("categoryId") Long categoryId
    ) throws Exception {

        LocalDate parsedDate;

        // ⭐ If user provides date → parse it
        // ⭐ If not → use today's date (same day as createdAt)
        if (date == null || date.isBlank()) {
            parsedDate = LocalDate.now();
        } else {
            parsedDate = LocalDate.parse(date);
        }

        FileResponse response = vaultService.uploadFile(
                userId, file, description, parsedDate, categoryId
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<VaultFile>> listFiles(
            @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(vaultService.listFiles(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> downloadFile(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id) throws MalformedURLException {

        VaultFile metadata = vaultService.getVaultFile(id, userId);
        Resource resource = vaultService.downloadFile(id, userId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(metadata.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + metadata.getOriginalName() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id) {
        vaultService.deleteFile(id, userId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/filter")
    public ResponseEntity<List<VaultFile>> filter(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to
    ) {

        // date range must always exist
        if (from == null || to == null) {
            return ResponseEntity.badRequest().build();
        }

        LocalDate startDate = LocalDate.parse(from);
        LocalDate endDate = LocalDate.parse(to);

        List<VaultFile> result;

        // CASE 2: date range + category
        if (categoryId != null) {
            result = vaultService.filterByCategoryAndDateRange(
                    userId, categoryId, startDate, endDate
            );
        }
        // CASE 1: only date range
        else {
            result = vaultService.filterByDateRange(
                    userId, startDate, endDate
            );
        }

        return ResponseEntity.ok(result);
    }

}
