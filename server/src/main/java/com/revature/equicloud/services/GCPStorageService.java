package com.revature.equicloud.services;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Service
public class GCPStorageService {

    private final Storage storage = StorageOptions.getDefaultInstance().getService();

    public String uploadFile(MultipartFile file) throws IOException {
        BlobId blobId = BlobId.of("equicloud-storage", file.getOriginalFilename());
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        storage.create(blobInfo, file.getInputStream());
        return "File uploaded successfully: " + file.getOriginalFilename();
    }

    public Blob downloadFile(String fileName) {
        return storage.get(BlobId.of("equicloud-storage", fileName));
    }
}
