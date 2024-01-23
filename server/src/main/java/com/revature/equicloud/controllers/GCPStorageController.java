package com.revature.equicloud.controllers;

import com.revature.equicloud.dtos.FileUploadDTO;
import com.revature.equicloud.services.UploadService;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.Blob;
import com.revature.equicloud.exceptions.StorageOperationException;
import com.revature.equicloud.services.GCPStorageService;

/**
 * This class represents the controller for handling requests related to Google Cloud Platform (GCP) storage operations.
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class GCPStorageController {

    private GCPStorageService cloudStorageService;
    
    /**
     * Constructs a new GCPStorageController with the specified GCPStorageService.
     * 
     * @param cloudStorageService the GCPStorageService to be used for handling storage operations
     */
    @Autowired
    public GCPStorageController(GCPStorageService cloudStorageService) {
        this.cloudStorageService = cloudStorageService;
    }

    /**
     * Handles the file upload request.
     * 
     * @param file the file to be uploaded
     * @param fileName the name of the file
     * @param description the description of the file
     * @return a ResponseEntity containing the response message
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("fileName") String fileName, @RequestParam("description") String description) {
        try {
            String response = cloudStorageService.uploadFile(file, fileName, description);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            // Log the exception details (consider using a logger)
            return ResponseEntity.internalServerError().body("Failed to upload file: " + e.getMessage());
        }
    }

    /**
     * Handles the file download request.
     * 
     * @param filePath the path of the file to be downloaded
     * @return a ResponseEntity containing the file as a ByteArrayResource
     */
    @GetMapping("/download")
    public ResponseEntity<?> downloadFile(@RequestParam String filePath) {
        try {
            Blob blob = cloudStorageService.downloadFile(filePath);
            ByteArrayResource resource = new ByteArrayResource(blob.getContent());
    
            String fileName = extractFileName(filePath);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (IOException e) {
            // Log the exception details (consider using a logger)
            return ResponseEntity.internalServerError().body("Failed to download file: " + e.getMessage());
        }
    }
    

    /**
     * Handles the folder creation request.
     * 
     * @param folderPath the path of the folder to be created
     * @return a ResponseEntity containing the response message
     */
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

    /**
     * Handles the file deletion request.
     * 
     * @param filePath the path of the file to be deleted
     * @return a ResponseEntity containing the response message
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("filePath") String filePath) {
            try {
                cloudStorageService.deleteFile(filePath);
                return ResponseEntity.ok("File deleted successfully: " + filePath);
            } catch (StorageOperationException e) {
                // Log the exception details (consider using a logger)
                return ResponseEntity.internalServerError().body(e.getMessage());
            }
        }


    /**
     * Extracts the file name from the given file path.
     * 
     * @param filePath the file path
     * @return the file name
     */
    private String extractFileName(String filePath) {
        return filePath.contains("/") ? filePath.substring(filePath.lastIndexOf('/') + 1) : filePath;
    }
    
}

