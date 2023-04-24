package com.whale.web.colors.service;

import java.io.ByteArrayInputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadImagemServiceColors {
	
    public MultipartFile uploadImagem(MultipartFile file) throws Exception {
        
        byte[] bytes = file.getBytes();
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        
        MultipartFile multipartFile = new com.whale.web.colors.configuration.CustomMultipartFile(file.getOriginalFilename(), file.getContentType(), inputStream);
        
        return multipartFile;
        
    }
	
}
