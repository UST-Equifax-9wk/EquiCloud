package com.revature.equicloud.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.equicloud.entities.Upload;
import com.revature.equicloud.repositories.UploadRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional(Transactional.TxType.REQUIRED)
public class UploadService {
    private UploadRepository uploadRepository;

    @Autowired
    UploadService(UploadRepository uploadRepository){
        this.uploadRepository=uploadRepository;
    }

    public Upload save(Upload upload){
        return uploadRepository.save(upload);
    }

    public List<Upload> findAll(){
        return uploadRepository.findAll();
    }

    public List<Upload> findContaining(String containing){
        return uploadRepository.findByFileNameContainingIgnoreCase(containing);
    }

    public Upload findByFilePath(String filePath) {
        return uploadRepository.findByFilePath(filePath);
    }

    public void delete(Upload upload) {
        uploadRepository.delete(upload);
    }

}
