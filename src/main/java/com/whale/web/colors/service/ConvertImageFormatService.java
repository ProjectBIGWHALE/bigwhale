package com.whale.web.colors.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


@Service
public class ConvertImageFormatService {

    @Autowired
    UploadImageColorService uploadImageColorService;
    public byte[] convertImageFormat(String outputFormat, MultipartFile imageFile) throws Exception {

        String contentType = imageFile.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Tipo de arquivo inválido. Apenas imagens são permitidas.");
        }

        byte[] bytes = imageFile.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        BufferedImage image = ImageIO.read(inputStream);

        ByteArrayOutputStream convertedImage = new ByteArrayOutputStream();
        ImageIO.write(image, outputFormat, convertedImage);
        convertedImage.flush();

        byte[] convertedImageBytes = convertedImage.toByteArray();
        convertedImage.close();
        return convertedImageBytes;
    }
    

//    private byte[] convertToGif(byte[] imageBytes) throws IOException {
//        // Carrega a imagem original em um BufferedImage
//        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
//        BufferedImage originalImage = ImageIO.read(inputStream);
//
//        // Cria um ByteArrayOutputStream para armazenar a imagem GIF resultante
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//
//        // Escreve a imagem original no formato GIF no ByteArrayOutputStream
//        ImageIO.write(originalImage, "gif", outputStream);
//
//        return outputStream.toByteArray();
//    }

}
