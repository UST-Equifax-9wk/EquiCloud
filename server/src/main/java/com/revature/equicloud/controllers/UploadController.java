package com.revature.equicloud.controllers;

import com.revature.equicloud.entities.Upload;
import com.revature.equicloud.services.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class UploadController {
    private UploadService uploadService;

    @Autowired
    UploadController(UploadService uploadService){
        this.uploadService=uploadService;
    }

    @GetMapping("/files")
    public ResponseEntity<List<Upload>> getAllFiles(){
        List<Upload> list = uploadService.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
