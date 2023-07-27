package com.whale.web.documents.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.whale.web.documents.model.FormDocumentsConverter;

@Service
public class ConverterService {
	
    public byte[] convertToZip(FormDocumentsConverter formDocumentsConverter) throws IOException {
    	
    	MultipartFile document = formDocumentsConverter.getFile();
    	
    	// Lê o conteúdo do MultipartFile
        try (InputStream input = document.getInputStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOut = new ZipOutputStream(outputStream)) {

            // Define o nome do arquivo no zip
            String zipEntryName = document.getOriginalFilename().substring(0, document.getOriginalFilename().lastIndexOf(".")) + ".zip";
            ZipEntry zipEntry = new ZipEntry(zipEntryName);
            zipOut.putNextEntry(zipEntry);

            // Copia o conteúdo do arquivo original para o arquivo zip
            IOUtils.copy(input, zipOut);
            zipOut.closeEntry();

            // Cria e retorna um novo MultipartFile representando o arquivo zip
            byte[] zipBytes = outputStream.toByteArray();
            return zipBytes;
        } catch(Exception e) {
        	return null;
        }
    }
	
}
