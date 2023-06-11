package com.whale.web.codes.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@Service
public class QRCodeService {
	
	public File gerarQRCode(String link) throws Exception {
		
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(link, BarcodeFormat.QR_CODE, 350, 350, hints);
            BufferedImage qrCodeImage = new BufferedImage(350, 350, BufferedImage.TYPE_INT_RGB);
            qrCodeImage.createGraphics();

            for (int x = 0; x < 350; x++) {
                for (int y = 0; y < 350; y++) {
                    qrCodeImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }

            File qrCodeFile = new File("qrcode.png");
            ImageIO.write(qrCodeImage, "png", qrCodeFile);
            
            return qrCodeFile;
        } catch (WriterException | IOException e) {
            throw new Exception();
        }
		
	}
	
}
