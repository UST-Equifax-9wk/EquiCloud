package com.revature.equicloud.controllers;

import com.revature.equicloud.entities.Upload;
import com.revature.equicloud.services.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    @PostMapping("/uploadMetadata")
    public ResponseEntity<Upload> uploadMetadata(@RequestBody Upload upload) {
        Upload savedUpload = uploadService.saveMetadata(upload);
        return new ResponseEntity<>(savedUpload, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{username}/folders")
    public ResponseEntity<ArrayList<String>> getFolders(@PathVariable String username) {
        ArrayList<String> result = uploadService.findFoldersByUsername(username);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
