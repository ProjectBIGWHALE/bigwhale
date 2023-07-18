package com.whale.web.certificates.service;

import java.io.ByteArrayInputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.whale.web.certificates.configuration.CustomMultipartFile;

@Service
public class UploadImagemService {
	
    public MultipartFile uploadImage(MultipartFile file) throws Exception {
        
        byte[] bytes = file.getBytes();
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        
        MultipartFile multipartFile = new CustomMultipartFile(file.getOriginalFilename(), file.getContentType(), inputStream);
        
        return multipartFile;
        
    }
	
}
