package com.revature.equicloud.repositories;

import com.revature.equicloud.entities.Upload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UploadRepository extends JpaRepository<Upload, String> {
    public Upload findByFileName(String fileName);

    @Query(value = "SELECT path FROM uploads WHERE path LIKE ?1%", nativeQuery = true)
    public Set<String> findAllPathsByUser(String username);
}
