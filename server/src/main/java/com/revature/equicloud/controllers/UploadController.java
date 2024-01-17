package com.revature.equicloud.controllers;

import com.revature.equicloud.services.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UploadController {
    private UploadService uploadService;

    @Autowired
    UploadController(UploadService uploadService){
        this.uploadService=uploadService;
    }
}
