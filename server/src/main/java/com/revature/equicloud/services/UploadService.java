package com.revature.equicloud.services;

import com.revature.equicloud.entities.Upload;
import com.revature.equicloud.repositories.UploadRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional(Transactional.TxType.REQUIRED)
public class UploadService {
    private UploadRepository uploadRepository;

    @Autowired
    UploadService(UploadRepository uploadRepository){
        this.uploadRepository=uploadRepository;
    }

    public List<Upload> findAll(){
        return uploadRepository.findAll();
    }

}
