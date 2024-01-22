package com.revature.equicloud.services;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.WriteChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;

@Service
public class GCPStorageService {

    private Storage storage;
    private final String filePath;
    private final String BUCKET_NAME;

    @Autowired
    public GCPStorageService(@Value("${gcp.bucket.name}") String bucketName, @Value("${gcp.config.file.path}") String filePath, ResourceLoader resourceLoader) {
        this.BUCKET_NAME = bucketName;
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

    public String uploadFile(MultipartFile file) throws IOException {
        try {
            BlobId blobId = BlobId.of(BUCKET_NAME, file.getOriginalFilename());
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

            try(WriteChannel writer = storage.writer(blobInfo)) {
                writer.write(ByteBuffer.wrap(file.getBytes()));
            }
            return "File uploaded successfully: " + file.getOriginalFilename();
        } catch (StorageException e) {
            throw new IOException("GCP Storage exception during file upload: " + e.getMessage(), e);
        }
    }

    public Blob downloadFile(String fileName) throws IOException {
        try {
            Blob blob = storage.get(BlobId.of(BUCKET_NAME, fileName));
            if (blob == null || !blob.exists()) {
                throw new IOException("File not found in GCP Storage: " + fileName);
            }
            return blob;
        } catch (StorageException e) {
            throw new IOException("GCP Storage exception during file download: " + e.getMessage(), e);
        }
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
}
