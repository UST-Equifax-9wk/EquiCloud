package com.revature.equicloud.services;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.WriteChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;
import com.revature.equicloud.entities.Upload;
import com.revature.equicloud.exceptions.StorageOperationException;

@Service
public class GCPStorageService {

    private Storage storage;

    private final UploadService uploadService;
    private final String BUCKET_NAME;

    private final String filePath;

    @Autowired
    public GCPStorageService(@Value("${gcp.bucket.name}") String bucketName, @Value("${gcp.config.file.path}") String filePath, ResourceLoader resourceLoader, UploadService uploadService) {
        this.BUCKET_NAME = bucketName;
        this.uploadService = uploadService;
        this.filePath = filePath;
        try{
            Resource resource = resourceLoader.getResource("classpath:" + filePath);
            Credentials credentials = GoogleCredentials.fromStream(resource.getInputStream());
            this.storage = StorageOptions.newBuilder().setProjectId("equicloud").setCredentials(credentials).build().getService();
        } catch (IOException e) {
            this.storage = StorageOptions.newBuilder().build().getService();
            System.out.println("Failed to load GCP credentials file. Using default credentials." + e.getMessage());
        }
    }

    

    public String uploadFile(MultipartFile file, String filePath, String description) throws IOException {
        try {
            BlobId blobId = BlobId.of(BUCKET_NAME, filePath);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

            try(WriteChannel writer = storage.writer(blobInfo)) {
                writer.write(ByteBuffer.wrap(file.getBytes()));
            }
            String fileName = filePath.split("/")[filePath.split("/").length - 1];
            Upload upload = new Upload(fileName, description, filePath, null);
            uploadService.save(upload);
            return "File uploaded successfully: " + fileName;
        } catch (StorageException e) {
            throw new IOException("GCP Storage exception during file upload: " +  e.getMessage(), e);
        }
    }

    public Blob downloadFile(String filePath) throws IOException {
        Blob blob = storage.get(BlobId.of(BUCKET_NAME, filePath));
        if (blob == null || !blob.exists()) {
            throw new IOException("File not found in GCP Storage: " + filePath);
        }
        return blob;
    }
    

    public String createFolder(String folderPath) throws IOException {
        try {
            String folderName = folderPath.endsWith("/") ? folderPath : folderPath + "/";
            BlobId blobId = BlobId.of(BUCKET_NAME, folderName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
            storage.create(blobInfo);
            return "Folder created successfully: " + folderName;
        } catch (StorageException e) {
            throw new IOException("GCP Storage exception during folder creation: " + e.getMessage(), e);
        }
    }

    public void deleteFile(String filePath) throws StorageOperationException{
        try{
            BlobId blobId = BlobId.of(BUCKET_NAME, filePath);
            boolean deleted = storage.delete(blobId);
            if(!deleted){
                throw new StorageOperationException("File not found in GCP Storage: " + filePath);
            }
        } catch (StorageException e) {
            throw new StorageOperationException("GCP Storage exception during file deletion: " + e.getMessage(), e);
        }
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }
    
}
