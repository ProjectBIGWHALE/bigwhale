package com.whale.web.documents.filecompressor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileCompressorService {

    public byte[] compressFile(MultipartFile multipartFile){
    	
    	if(multipartFile == null || multipartFile.isEmpty()) {
    		throw new RuntimeException();
    	}
    	
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOut = new ZipOutputStream(byteArrayOutputStream)) {
            zipOut.setLevel(Deflater.BEST_COMPRESSION);
            ZipEntry zipEntry = new ZipEntry(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            zipOut.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int length;
            InputStream fileInputStream = multipartFile.getInputStream();
            while ((length = fileInputStream.read(buffer)) > 0) {
                zipOut.write(buffer, 0, length);
            }

            fileInputStream.close();
            zipOut.closeEntry();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return byteArrayOutputStream.toByteArray();
    }
	
}
