package com.revature.equicloud.services;

import java.io.IOException;
import java.nio.ByteBuffer;

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

    private final Storage storage = StorageOptions.getDefaultInstance().getService();

    private static final String BUCKET_NAME = "equicloud-storage";

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
