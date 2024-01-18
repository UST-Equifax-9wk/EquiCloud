package com.revature.equicloud.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.google.cloud.storage.Blob;
import com.revature.equicloud.services.GCPStorageService;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class GCPStorageController {

    @Autowired
    private GCPStorageService cloudStorageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String response = cloudStorageService.uploadFile(file);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            // Log the exception details (consider using a logger)
            return ResponseEntity.internalServerError().body("Failed to upload file: " + e.getMessage());
        }
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<?> downloadFile(@PathVariable String fileName) {
        try {
            Blob blob = cloudStorageService.downloadFile(fileName);
            ByteArrayResource resource = new ByteArrayResource(blob.getContent());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (IOException e) {
            // Log the exception details (consider using a logger)
            return ResponseEntity.internalServerError().body("Failed to download file: " + e.getMessage());
        }
    }

    @PostMapping("/create-folder")
    public ResponseEntity<String> createFolder(@RequestParam("folderPath") String folderPath) {
        try {
            String response = cloudStorageService.createFolder(folderPath);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            // Log the exception details (consider using a logger)
            return ResponseEntity.internalServerError().body("Failed to create folder: " + e.getMessage());
        }
    }
}

