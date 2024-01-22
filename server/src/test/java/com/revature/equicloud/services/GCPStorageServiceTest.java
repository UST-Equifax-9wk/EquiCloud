package com.revature.equicloud.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.revature.equicloud.entities.Upload;
import com.revature.equicloud.exceptions.StorageOperationException;

class GCPStorageServiceTest {

    @Mock
    private Storage storage;

    @Mock
    private UploadService uploadService;

    @Mock
    private ResourceLoader resourceLoader;

    private GCPStorageService storageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        storageService = new GCPStorageService("test-bucket", "test-file-path", resourceLoader, uploadService);
    }

    @Test
    void testUploadFile_Success() throws IOException {
        // Arrange
        MultipartFile file = new MockMultipartFile("test.txt", "Hello World".getBytes(StandardCharsets.UTF_8));
        String filePath = "test-folder/test.txt";
        String description = "Test file";

        // Act
        String result = storageService.uploadFile(file, filePath, description);

        // Assert
        assertEquals("File uploaded successfully: test.txt", result);
        verify(storage, times(1)).writer(any(BlobInfo.class));
        verify(uploadService, times(1)).save(any(Upload.class));
    }

    @Test
    void testUploadFile_StorageException() throws IOException {
        // Arrange
        MultipartFile file = new MockMultipartFile("test.txt", "Hello World".getBytes(StandardCharsets.UTF_8));
        String filePath = "test-folder/test.txt";
        String description = "Test file";
        StorageException exception = new StorageException(500, "Internal Server Error");

        when(storage.writer(any(BlobInfo.class))).thenThrow(exception);

        // Act
        IOException thrownException = assertThrows(IOException.class, () -> {
            storageService.uploadFile(file, filePath, description);
        });

        // Assert
        assertEquals("GCP Storage exception during file upload: Internal Server Error", thrownException.getMessage());
        verify(storage, times(1)).writer(any(BlobInfo.class));
        verify(uploadService, never()).save(any(Upload.class));
    }

    @Test
    void testDownloadFile_Success() throws IOException {
        // Arrange
        String filePath = "test-folder/test.txt";
        Blob blob = mock(Blob.class);
        when(storage.get(any(BlobId.class))).thenReturn(blob);

        // Act
        Blob result = storageService.downloadFile(filePath);

        // Assert
        assertEquals(blob, result);
        verify(storage, times(1)).get(any(BlobId.class));
    }

    @Test
    void testDownloadFile_FileNotFound() throws IOException {
        // Arrange
        String filePath = "test-folder/test.txt";
        when(storage.get(any(BlobId.class))).thenReturn(null);

        // Act
        IOException thrownException = assertThrows(IOException.class, () -> {
            storageService.downloadFile(filePath);
        });

        // Assert
        assertEquals("File not found in GCP Storage: test-folder/test.txt", thrownException.getMessage());
        verify(storage, times(1)).get(any(BlobId.class));
    }

    @Test
    void testCreateFolder_Success() throws IOException {
        // Arrange
        String folderPath = "test-folder/";

        // Act
        String result = storageService.createFolder(folderPath);

        // Assert
        assertEquals("Folder created successfully: test-folder/", result);
        verify(storage, times(1)).create(any(BlobInfo.class));
    }

    @Test
    void testCreateFolder_StorageException() throws IOException {
        // Arrange
        String folderPath = "test-folder/";
        StorageException exception = new StorageException(500, "Internal Server Error");

        when(storage.create(any(BlobInfo.class))).thenThrow(exception);

        // Act
        IOException thrownException = assertThrows(IOException.class, () -> {
            storageService.createFolder(folderPath);
        });

        // Assert
        assertEquals("GCP Storage exception during folder creation: Internal Server Error", thrownException.getMessage());
        verify(storage, times(1)).create(any(BlobInfo.class));
    }

    @Test
    void testDeleteFile_Success() throws StorageOperationException {
        // Arrange
        String filePath = "test-folder/test.txt";
        BlobId blobId = BlobId.of("test-bucket", filePath);
        Blob blob = mock(Blob.class);
        Upload upload = mock(Upload.class);

        when(storage.delete(blobId)).thenReturn(true);
        when(uploadService.findByFilePath(filePath)).thenReturn(upload);

        // Act
        storageService.deleteFile(filePath);

        // Assert
        verify(storage, times(1)).delete(blobId);
        verify(uploadService, times(1)).delete(upload);
    }

    @Test
    void testDeleteFile_FileNotFound() {
        // Arrange
        String filePath = "test-folder/test.txt";
        BlobId blobId = BlobId.of("test-bucket", filePath);

        when(storage.delete(blobId)).thenReturn(false);

        // Act
        StorageOperationException thrownException = assertThrows(StorageOperationException.class, () -> {
            storageService.deleteFile(filePath);
        });

        // Assert
        assertEquals("File not found in GCP Storage: test-folder/test.txt", thrownException.getMessage());
        verify(storage, times(1)).delete(blobId);
        verify(uploadService, never()).delete(any(Upload.class));
    }
}