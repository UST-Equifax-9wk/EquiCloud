package com.revature.equicloud.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity(name = "uploads")
public class Upload {

    @Id
    @Column(name = "file_name")
    private String fileName;

    @Column
    private String description;

    @Column
    private String path;

    @Column(name = "upload_date")
    @CreationTimestamp
    private Instant uploadDate;


    public Upload() {
    }

    public Upload(String fileName, String description, String path, Instant uploadDate) {
        this.fileName = fileName;
        this.description = description;
        this.path = path;
        this.uploadDate = uploadDate;
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

    public Instant getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Instant uploadDate) {
        this.uploadDate = uploadDate;
    }

    @Override
    public String toString() {
        return "Upload{" +
                "fileName='" + fileName + '\'' +
                ", description='" + description + '\'' +
                ", path='" + path + '\'' +
                ", uploadDate=" + uploadDate +
                '}';
    }

}
