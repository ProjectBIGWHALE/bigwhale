package com.whale.web.colors.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;


@Service
public class ConvertImageFormatService {

    @Autowired
    UploadImageColorService uploadImageColorService;
    public byte[] convertImageFormat(String outputFormat, MultipartFile file) throws Exception {

        // Upload da imagem anexada pelo usuário. Contém nome e tipo original e os bytes (inputstream) da imagem
        MultipartFile uploadedImage = uploadImageColorService.uploadImage(file);

        // O método ImageIO.read() é responsável por ler o InputStream fornecido e criar um
        // objeto BufferedImage correspondente à imagem.
        BufferedImage image = ImageIO.read(uploadedImage.getInputStream());

        // Grava a imagem convertida em um ByteArrayOutputStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, outputFormat, baos);
        baos.flush();

        // Obtém os bytes da imagem convertida
        byte[] convertedImageBytes = baos.toByteArray();
        baos.close();

        return convertedImageBytes;
    }
}

