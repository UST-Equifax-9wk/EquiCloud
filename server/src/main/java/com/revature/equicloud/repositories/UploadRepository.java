package com.revature.equicloud.repositories;

import com.revature.equicloud.entities.Upload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UploadRepository extends JpaRepository<Upload, String> {
    public Upload findByFileName(String fileName);

    public List<Upload> findByFileNameContainingIgnoreCase(String fileName);

}
