package com.whale.web.documents.qrcodegenerator.service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@Service
public class QRCodeGeneratorService {
	
	 public byte[] generateQRCode(String link, String pixelColor) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(link, BarcodeFormat.QR_CODE, 350, 350, hints);
            BufferedImage qrCodeImage = new BufferedImage(350, 350, BufferedImage.TYPE_INT_RGB);
            qrCodeImage.createGraphics();

            int pixelColorValue = Color.decode(pixelColor).getRGB();
            for (int x = 0; x < 350; x++) {
                for (int y = 0; y < 350; y++) {
                    qrCodeImage.setRGB(x, y, bitMatrix.get(x, y) ? pixelColorValue : 0xFFFFFFFF);
                }
            }

            // Convert BufferedImage to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(qrCodeImage, "png", bos);

            bos.flush();
            byte[] imageBytes = bos.toByteArray();
            bos.close();

            return imageBytes;
        } catch (WriterException | IOException e) {
            throw new Exception();
        }
    }



    public byte[] generateWhatsAppLinkQRCode(String phoneNumber, String text, String pixelColor) throws Exception {
        if (phoneNumber == null || text == null) {
            throw new IllegalArgumentException("Os argumentos não podem ser nulos.");
        }
        String whatsappLink = "https://wa.me/" + phoneNumber + "/?text=" + text.replace(" ", "+");
        return generateQRCode(whatsappLink, pixelColor);
    }

    public byte[] generateEmailLinkQRCode(String email, String titleEmail, String textEmail, String pixelColor) throws Exception {
        if (email == null || titleEmail == null || textEmail == null) {
            throw new IllegalArgumentException("Os argumentos não podem ser nulos.");
        }
        String emailLink = "mailto:" + email + "?subject=" + textEmail + "&body=" + titleEmail;
        return generateQRCode(emailLink, pixelColor);
    }
    
	
}
