package com.revature.equicloud.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.equicloud.entities.Upload;

@Repository
public interface UploadRepository extends JpaRepository<Upload, String> {
    public Upload findByFileName(String fileName);

    public List<Upload> findByFileNameContainingIgnoreCase(String fileName);

    public Upload findByPath(String filePath);

}
