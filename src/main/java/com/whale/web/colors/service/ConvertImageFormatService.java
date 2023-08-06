package com.whale.web.colors.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


@Service
public class ConvertImageFormatService {

    @Autowired
    UploadImageColorService uploadImageColorService;
    public byte[] convertImageFormat(String outputFormat, MultipartFile imageFile) {

        try {
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


