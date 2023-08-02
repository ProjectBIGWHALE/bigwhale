package com.whale.web.colors.service;

import com.whale.web.colors.configuration.CustomMultipartFile;
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
    public byte[] convertImageFormat(String outputFormat, MultipartFile imageFile) throws Exception {

        // Upload da imagem anexada pelo usuário. Contém nome e tipo original e os bytes (inputstream) da imagem
        MultipartFile uploadedImage = uploadImageColorService.uploadImage(imageFile);

        // O método ImageIO.read() é responsável por ler o InputStream fornecido e criar um
        // objeto BufferedImage correspondente à imagem.
        BufferedImage image = ImageIO.read(uploadedImage.getInputStream());

        // Método Grava a imagem convertida em um ByteArrayOutputStream
        ByteArrayOutputStream convertedImage = new ByteArrayOutputStream();
        ImageIO.write(image, outputFormat, convertedImage);
        convertedImage.flush();


        // Obtém os bytes da imagem convertida
        byte[] convertedImageBytes = convertedImage.toByteArray();
        convertedImage.close();

        return convertedImageBytes;
    }
}

