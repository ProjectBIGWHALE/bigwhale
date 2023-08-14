package com.whale.web.documents.imageconverter.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageConverterService {
	
    public byte[] convertImageFormat(String outputFormat, MultipartFile imageFile) {

        try {

            if (imageFile == null || imageFile.isEmpty()) {
                throw new IllegalArgumentException("Erro ao processar arquivo: image == null!");
            }

            String contentType = imageFile.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("Tipo de arquivo inválido. Apenas imagens são permitidas.");
            }

            byte[] bytes = imageFile.getBytes();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            BufferedImage image = ImageIO.read(inputStream);

            ByteArrayOutputStream convertedImage = new ByteArrayOutputStream();

            boolean successfullyConverted = ImageIO.write(image, outputFormat, convertedImage);
            convertedImage.flush();

            if (!successfullyConverted) {
                throw new IllegalArgumentException("Conversão não realizada: o formato de saída especificado não é suportado.");
            }

            byte[] convertedImageBytes = convertedImage.toByteArray();
            convertedImage.close();
            return convertedImageBytes;

        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar a image: " + e.getMessage());
        }
    }
	
}
