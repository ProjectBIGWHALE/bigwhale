package com.whale.web.colors.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AlterColorService {
	
	@Autowired
	UploadImageColorService uploadImageColorService;
	
	public byte[] alterColor(MultipartFile imageForm, String colorOfImage, String replacementColor, Integer margin) throws Exception {
	    
	    MultipartFile upload = uploadImageColorService.uploadImage(imageForm);
	    BufferedImage img = ImageIO.read(upload.getInputStream());

	    // Defines the marked color
	    Color markedColor = Color.decode(colorOfImage);

	    // Sets the top and bottom margin for each RGB component
	    int delta = (int) Math.round(255 * (margin / 100.0)); // Margin percentage
	    int r = markedColor.getRed();
	    int g = markedColor.getGreen();
	    int b = markedColor.getBlue();
	    int newRmin = Math.max(0, r - delta); // Lower limit for the R component
	    int newRmax = Math.min(255, r + delta); // Lower limit for the R component
	    int newGmin = Math.max(0, g - delta); // Lower limit for the G component
	    int newGmax = Math.min(255, g + delta); // Upper limit for the G component
	    int newBmin = Math.max(0, b - delta); // Lower limit for the B component 
	    int newBmax = Math.min(255, b + delta); // Upper limit for the B component

	    // Sets current color and new color
	    Color oldColor = new Color(r, g, b);
	    Color newColor;

	    if (replacementColor == null || replacementColor.isEmpty()) {
	        newColor = new Color(0, 0, 0, 0);
	    } else {
	        Color color = Color.decode(replacementColor);
	        newColor = new Color(color.getRed(), color.getGreen(), color.getBlue());
	    }

	    // Creates a new image with transparency
	    BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);

	    // Cycles through all pixels in the image
	    for (int x = 0; x < img.getWidth(); x++) {
	        for (int y = 0; y < img.getHeight(); y++) {
	            // Checks the current pixel color
	            Color pixelColor = new Color(img.getRGB(x, y));
	            int pixelR = pixelColor.getRed();
	            int pixelG = pixelColor.getGreen();
	            int pixelB = pixelColor.getBlue();
	            if (pixelR >= newRmin && pixelR <= newRmax &&
	                pixelG >= newGmin && pixelG <= newGmax &&
	                pixelB >= newBmin && pixelB <= newBmax) {
	                // Set the full RGBA value using the getRGB() method
	                int newPixelValue = newColor.getRGB();
	                // Sets the new pixel value in the new image
	                newImg.setRGB(x, y, newPixelValue);
	            } else {
	                // Copies the original pixel to the new image
	                newImg.setRGB(x, y, img.getRGB(x, y));
	            }
	        }
	    }

	    // Convert BufferedImage to byte array
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    ImageIO.write(newImg, "png", bos);
	    bos.flush();
	    byte[] imageBytes = bos.toByteArray();
	    bos.close();

	    return imageBytes;
	}

	
}
