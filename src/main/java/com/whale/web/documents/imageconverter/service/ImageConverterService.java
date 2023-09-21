package com.whale.web.documents.imageconverter.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Objects;

import javax.imageio.ImageIO;

import com.whale.web.documents.imageconverter.exception.*;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageConverterService {
	
    public byte[] convertImageFormat(String outputFormat, MultipartFile imageFile) throws Exception {

        String message = "Uploaded image file is null, empty or image format is not supported";
        if (imageFile == null || imageFile.isEmpty()) {
            throw new InvalidUploadedFileException(message);
        }

        String[] allowedFormats = { "bmp", "jpg", "jpeg", "gif" };
        String fileExtension = FilenameUtils.getExtension(imageFile.getOriginalFilename());

        assert fileExtension != null;
        if (!Arrays.asList(allowedFormats).contains(fileExtension.toLowerCase())) {
            throw new InvalidFileFormatException("Unsupported file format. Please choose a BMP, JPG, JPEG or GIF file.");
        }

        BufferedImage image = ImageIO.read(imageFile.getInputStream());
        if (image == null) {
            throw new InvalidUploadedFileException(message);
        }

        ByteArrayOutputStream convertedImage = new ByteArrayOutputStream();
        boolean successfullyConverted = ImageIO.write(image, outputFormat, convertedImage);
        convertedImage.flush();

        String imageType = Objects.requireNonNull(imageFile.getContentType()).substring(6);

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
