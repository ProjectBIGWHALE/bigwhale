package com.whale.web.documents.textextract;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@Service
public class TextExtractService {
	
    private static final String PATCH = "../bigwhale/src/main/resources/static/tessdata";

    public String extractTextFromImage(MultipartFile multipartFile) {

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(PATCH);
        tesseract.setLanguage("por");

        try {
            return tesseract.doOCR(convertMutlpartFileToBufferedImage(multipartFile));
        } catch (TesseractException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    private BufferedImage convertMutlpartFileToBufferedImage(MultipartFile multipartFile){
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(multipartFile.getBytes());
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e.getCause());
        }
    }
	
}
