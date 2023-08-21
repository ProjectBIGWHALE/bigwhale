package com.whale.web.documents.imageconverter.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.whale.web.documents.imageconverter.exception.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageConverterService {
	
    public byte[] convertImageFormat(String outputFormat, MultipartFile imageFile) throws IOException {

        if (imageFile == null || imageFile.isEmpty()) {
            throw new InvalidUploadedFileException("An invalid file was sent or the image format is not accepted.");
        }

        String contentType = imageFile.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new UnexpectedFileFormatException("Invalid file type. Only images are allowed.");
        }

        byte[] bytes = imageFile.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        BufferedImage image = ImageIO.read(inputStream);
        if (image == null) {
            throw new InvalidUploadedFileException("An invalid file was sent or the image format is not accepted.");
        }

        ByteArrayOutputStream convertedImage = new ByteArrayOutputStream();

        boolean successfullyConverted = ImageIO.write(image, outputFormat, convertedImage);
        convertedImage.flush();

        String imageType = contentType.substring(6);
        if (imageType.isEmpty()) {
            throw new UnableToReadImageFormatException("Unable to verify the format of the attached image.");
        } else {
            if (!successfullyConverted) {
                throw new UnableToConvertImageToOutputFormatException(
                        "Could not convert an image "
                                + imageType
                                + " for format "
                                + outputFormat
                                + ". Please try again or choose another output format.");

            } else {
                byte[] convertedImageBytes = convertedImage.toByteArray();
                convertedImage.close();
                return convertedImageBytes;
            }
        }
    }
	
}
