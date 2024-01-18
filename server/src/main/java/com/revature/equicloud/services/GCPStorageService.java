package com.revature.equicloud.services;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
import com.revature.equicloud.exceptions.StorageOperationException;

@Service
public class GCPStorageService {

    private final Storage storage;

    private final String BUCKET_NAME;

    @Autowired
    public GCPStorageService(@Value("${gcp.bucket.name}") String bucketName) {
        this.BUCKET_NAME = bucketName;
        Credentials credentials = null;
        try {
            credentials = GoogleCredentials.fromStream(new FileInputStream("equicloud-08122c442b64.json"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
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
}
