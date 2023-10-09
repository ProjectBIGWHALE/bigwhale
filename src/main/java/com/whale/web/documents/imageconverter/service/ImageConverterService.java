package com.whale.web.documents.imageconverter.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import javax.imageio.ImageIO;

import com.whale.web.documents.imageconverter.exception.*;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageConverterService {

    String message = "Uploaded image file is null, empty or image format is not supported";
	
    public byte[] convertImageFormat(String outputFormat, MultipartFile imageFile) throws Exception {

        isValidImageFormat(imageFile);
        isValidOutputFormat(outputFormat);

        BufferedImage image = ImageIO.read(imageFile.getInputStream());

        ByteArrayOutputStream convertedImage = new ByteArrayOutputStream();
        boolean successfullyConverted = ImageIO.write(image, outputFormat, convertedImage);
        convertedImage.flush();

        if (!successfullyConverted) {
            throw new UnableToConvertImageToOutputFormatException( "Could not convert an image.");
        } else {
            byte[] convertedImageBytes = convertedImage.toByteArray();
            convertedImage.close();
            return convertedImageBytes;
        }

    }


    private void isValidImageFormat(MultipartFile imageFile){
        if (imageFile == null || imageFile.isEmpty()) {
            throw new InvalidUploadedFileException(message);
        }

        String[] allowedFormats = { "bmp", "jpg", "jpeg", "gif" };
        String fileExtension = FilenameUtils.getExtension(imageFile.getOriginalFilename());

        assert fileExtension != null;
        if (!Arrays.asList(allowedFormats).contains(fileExtension.toLowerCase())) {
            throw new InvalidFileFormatException("Unsupported file format. Please choose a BMP, JPG, JPEG or GIF file.");
        }
    }

    private void isValidOutputFormat(String outputFOrmat){

        String[] allowedFormats = { "bmp", "jpg", "jpeg", "gif" };

        if (!Arrays.asList(allowedFormats).contains(outputFOrmat)) {
            throw new InvalidFileFormatException("Unsupported file format. Please choose a BMP, JPG, JPEG or GIF file.");
        }
    }
	
}
