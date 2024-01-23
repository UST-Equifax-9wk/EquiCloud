package com.revature.equicloud.services;

import com.revature.equicloud.entities.Upload;
import com.revature.equicloud.repositories.UploadRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
@Transactional(Transactional.TxType.REQUIRED)
public class UploadService {
    private UploadRepository uploadRepository;

    @Autowired
    UploadService(UploadRepository uploadRepository){
        this.uploadRepository=uploadRepository;
    }

    public Upload save(Upload upload){
        upload.setUploadDate(Instant.now());
        return uploadRepository.save(upload);
    }

    public List<Upload> findAll(){
        return uploadRepository.findAll();
    }

    public Upload saveMetadata(Upload upload) {
        upload.setUploadDate(Instant.now());
        return this.uploadRepository.save(upload);
    }

    public ArrayList<String> findFoldersByUsername(String username) {
        String modifiedString = username + "/";
        Set<String> paths = uploadRepository.findAllPathsByUser(modifiedString);
        Pattern pattern = Pattern.compile("/(\\S+)(/)");

        ArrayList<String> results = new ArrayList<>();
        for(String path : paths) {
            Matcher matcher = pattern.matcher(path);
            if(matcher.find()) {
                results.add(matcher.group(1));
            }
        }

        results = (ArrayList<String>)results.stream().distinct().collect(Collectors.toList());
        return results;
    }
    public List<Upload> findContaining(String containing){
        return uploadRepository.findByFileNameContainingIgnoreCase(containing);
    }

    public Upload findByFilePath(String filePath) {
        return uploadRepository.findByPath(filePath);
    }

    public void delete(Upload upload) {
        uploadRepository.delete(upload);
    }

}
