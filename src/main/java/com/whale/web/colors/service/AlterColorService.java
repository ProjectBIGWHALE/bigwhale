package com.whale.web.colors.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AlterColorService {
	
public File alterColor(MultipartFile imageForm, String colorOfImage, String replacementColor, Integer margin) throws IOException {
	    
	    // Carrega imagem
	    BufferedImage img = ImageIO.read(imageForm.getInputStream());

	    // Define a cor marcada
	    Color markedColor = Color.decode(colorOfImage);

	    // Define a margem superior e inferior para cada componente RGB
	    int delta = (int) Math.round(255 * (margin / 100.0)); // Porcentagem de margem
	    int r = markedColor.getRed();
	    int g = markedColor.getGreen();
	    int b = markedColor.getBlue();
	    int newRmin = Math.max(0, r - delta); // Limite inferior para o componente R
	    int newRmax = Math.min(255, r + delta); // Limite superior para o componente R
	    int newGmin = Math.max(0, g - delta); // Limite inferior para o componente G
	    int newGmax = Math.min(255, g + delta); // Limite superior para o componente G
	    int newBmin = Math.max(0, b - delta); // Limite inferior para o componente B
	    int newBmax = Math.min(255, b + delta); // Limite superior para o componente B

	    // Define a cor atual e a nova cor
	    Color oldColor = new Color(r, g, b);
	    Color newColor;
	    
	    if(replacementColor == null || replacementColor.isEmpty()) {
	    	newColor = new Color(0, 0, 0, 0);
	    } else {
	    	Color color = Color.decode(replacementColor);
	    	newColor = new Color(color.getRed(), color.getGreen(), color.getBlue());
	    }
	    // Cria uma nova imagem com transparência
	    BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);

	    // Percorre todos os pixels da imagem
	    for (int x = 0; x < img.getWidth(); x++) {
	        for (int y = 0; y < img.getHeight(); y++) {
	            // Verifica a cor atual do pixel
	            Color pixelColor = new Color(img.getRGB(x, y));
	            int pixelR = pixelColor.getRed();
	            int pixelG = pixelColor.getGreen();
	            int pixelB = pixelColor.getBlue();
	            if (pixelR >= newRmin && pixelR <= newRmax &&
	                pixelG >= newGmin && pixelG <= newGmax &&
	                pixelB >= newBmin && pixelB <= newBmax) {
	                // Define o valor RGBA completo usando o método getRGB()
	                int newPixelValue = newColor.getRGB();
	                // Define o novo valor do pixel na nova imagem
	                newImg.setRGB(x, y, newPixelValue);
	            } else {
	                // Copia o pixel original para a nova imagem
	                newImg.setRGB(x, y, img.getRGB(x, y));
	            }
	        }
	    }

	    // Salva a nova imagem com transparência em um arquivo
	    File ProcessedImage = new File("ColorTransparent.png");
	    ImageIO.write(newImg, "png", ProcessedImage);

	    return ProcessedImage;
	}
	
}
