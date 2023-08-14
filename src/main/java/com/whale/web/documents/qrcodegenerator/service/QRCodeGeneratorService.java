package com.whale.web.documents.qrcodegenerator.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
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
public class QRCodeGeneratorService {
	
	public byte[] generateQRCode(String link) throws Exception {
		
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
	
}
