package com.whale.web.colors.service;

import com.whale.web.certificates.configuration.CustomMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class UploadImageColorService {

    public MultipartFile uploadImage(MultipartFile file) throws Exception {
        // Aqui, o método extrai os bytes da imagem enviada.
        byte[] bytes = file.getBytes();

        // Verifica o formato da imagem
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Tipo de arquivo inválido. Apenas imagens são permitidas.");
        }

        // Converte a imagem para GIF
        byte[] gifBytes = convertToGif(bytes);

        // Cria-se uma instância personalizada em memória por meio dos dados obtidos da imagem convertida
        MultipartFile gifFile = new CustomMultipartFile("converted_" + file.getOriginalFilename(),
                "image/gif",
                new ByteArrayInputStream(gifBytes));

        // Retorna o upload da imagem convertida com os dados originais
        return gifFile;
    }

    private byte[] convertToGif(byte[] imageBytes) throws IOException {
        // Carrega a imagem original em um BufferedImage
        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
        BufferedImage originalImage = ImageIO.read(inputStream);

        // Cria um ByteArrayOutputStream para armazenar a imagem GIF resultante
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Escreve a imagem original no formato GIF no ByteArrayOutputStream
        ImageIO.write(originalImage, "gif", outputStream);

        return outputStream.toByteArray();
    }
}
