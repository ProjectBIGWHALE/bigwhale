package com.whale.web.certificates.service;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EditImageService {
	
	public List<byte[]> editImage(MultipartFile layoutPattern, List<String> names, Integer x, Integer y, Integer fontSize) throws IOException {
	    
		List<byte[]> imagesWithText = new ArrayList<>();

	    BufferedImage image = ImageIO.read(layoutPattern.getInputStream());

	    for (String name : names) {
	    	
	        //create a blank image the same size as the original
	        BufferedImage imageWithText = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
	        
	        Graphics2D g2d = imageWithText.createGraphics();
	        g2d.drawImage(image, 0, 0, null);

	        g2d.setColor(Color.BLACK);
	        g2d.setFont(new Font("Arial", Font.BOLD, fontSize));
	        g2d.drawString(name, x, y); // coordinates x=100, y=100

	        ByteArrayOutputStream temporaryStorage = new ByteArrayOutputStream();
	        ImageIO.write(imageWithText, "png", temporaryStorage);

	        //Add the image with text in the list
	        imagesWithText.add(temporaryStorage.toByteArray());
	    }

	    return imagesWithText;
	}
	
}
