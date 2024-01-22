package com.revature.equicloud.controllers;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import com.revature.equicloud.services.GCPStorageService;

class GCPStorageControllerTest<CloudStorageService> {

    @Mock
    private GCPStorageService cloudStorageService;

    private GCPStorageController storageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        storageController = new GCPStorageController(cloudStorageService);
    }

    @Test
    void testDownloadFile() throws IOException {
        // Arrange
        String filePath = "test.txt";
        byte[] content = "Hello World".getBytes();

        // Act
        ResponseEntity<?> response = storageController.downloadFile(filePath);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("attachment; filename=\"example.txt\"", response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
        assertEquals(new ByteArrayResource(content), response.getBody());

        // Verify
        verify(cloudStorageService, times(1)).downloadFile(filePath);
    }

    @Test
    void testDownloadFile_IOException() throws IOException {
        // Arrange
        String filePath = "example.txt";
        IOException exception = new IOException("File not found");
        when(cloudStorageService.downloadFile(filePath)).thenThrow(exception);

        // Act
        ResponseEntity<?> response = storageController.downloadFile(filePath);

        // Assert
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Failed to download file: File not found", response.getBody());

        // Verify
        verify(cloudStorageService, times(1)).downloadFile(filePath);
    }
}