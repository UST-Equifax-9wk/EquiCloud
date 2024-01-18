package com.revature.equicloud.repositories;

import com.revature.equicloud.entities.Upload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadRepository extends JpaRepository<Upload, String> {
    public Upload findByFileName(String fileName);
}
