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

            int pixelColorValue = convertColor(pixelColor);

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
        if (phoneNumber == null || text == null || pixelColor == null) {
            throw new IllegalArgumentException("Os argumentos não podem ser nulos.");
        }
        String whatsappLink = "https://wa.me/" + phoneNumber + "/?text=" + text.replace(" ", "+");
        return generateQRCode(whatsappLink, pixelColor);
    }

    public byte[] generateEmailLinkQRCode(String email, String titleEmail, String textEmail, String pixelColor) throws Exception {
        if (email == null || titleEmail == null || textEmail == null || pixelColor == null) {
            throw new IllegalArgumentException("Os argumentos não podem ser nulos.");
        }
        String emailLink = "mailto:" + email + "?subject=" + textEmail + "&body=" + titleEmail;
        return generateQRCode(emailLink, pixelColor);
    }

    public int convertColor(String colorString) throws Exception {
        try {
            Color color = Color.decode(colorString);
            return color.getRGB();
        } catch (NumberFormatException hexException) {
            try {
                Color colorByName = getColorByName(colorString);
                if (colorByName != null) {
                    return colorByName.getRGB();
                }
            } catch (Exception e) {
                throw new Exception("Não foi possível converter para rgb");
            }

            try {
                // Tenta analisar a cor no formato "rgb(r,g,b)"
                if (colorString.startsWith("rgb(") && colorString.endsWith(")")) {
                    String[] components = colorString.substring(4, colorString.length() - 1).split(",");
                    if (components.length == 3) {
                        int r = Integer.parseInt(components[0].trim());
                        int g = Integer.parseInt(components[1].trim());
                        int b = Integer.parseInt(components[2].trim());
                        return new Color(r, g, b).getRGB();
                    }
                }
            } catch (NumberFormatException rgbException) {
                throw new Exception("Não foi possível converter oara rgb");
            }
        }
        return Color.BLACK.getRGB();
    }


    public static Color getColorByName(String name) {
        name = name.toLowerCase();
        return switch (name) {
            case "violet" -> Color.MAGENTA;
            case "red" -> Color.RED;
            case "blue" -> Color.BLUE;
            case "green" -> Color.GREEN;
            case "yellow" -> Color.YELLOW;
            case "orange" -> Color.ORANGE;
            case "pink" -> Color.PINK;
            case "cyan" -> Color.CYAN;
            case "black" -> Color.BLACK;
            case "magenta" -> Color.MAGENTA;
            case "dark_gray" -> Color.DARK_GRAY;
            case "light_gray" -> Color.LIGHT_GRAY;
            default -> null;
        };
    }



}
