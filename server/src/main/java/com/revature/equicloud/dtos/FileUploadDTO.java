package com.revature.equicloud.dtos;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadDTO {
    private String fileName;
    private String description;
    private String path;
    private MultipartFile file;

    public FileUploadDTO(String fileName, String description, String path, MultipartFile file) {
        this.fileName = fileName;
        this.description = description;
        this.path = path;
        this.file = file;
    }

    public FileUploadDTO() {
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
