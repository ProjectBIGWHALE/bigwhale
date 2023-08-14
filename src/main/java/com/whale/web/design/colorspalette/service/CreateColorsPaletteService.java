package com.whale.web.design.colorspalette.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/*
 * Classe para criar uma lista de cores em hexadecimal com base em uma imagem.
 * Para fazer isso, o codigo se baseia na predominancia de cada cor.
 * Podemos determinar o numero de cores que desejamos na lista. Por exemplo: pegar as
 * 3 cores mais frequentes.
 * Ainda devemos otimizar esse codigo. Dependendo da distancia configurada entre as cores,
 * pode resultar em uma paleta de cores que destoa da nossa percepcao da imagem
 */

@Service
public class CreateColorsPaletteService {
	
	public List<Color> createColorPalette(MultipartFile multipartFile) throws Exception {
    	
    	
        int numColors = 10; // Number of predominant colors to be extracted
        int maxColorDistance = 70; // Maximum allowed distance between colors

        try {
            BufferedImage image = ImageIO.read(multipartFile.getInputStream());
            int width = image.getWidth();
            int height = image.getHeight();

            // Color count
            Map<Integer, Integer> colorCount = new HashMap<>();

            // Iterate through all pixels of the image
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = image.getRGB(x, y);
                    int alpha = (rgb >> 24) & 0xFF;

                    // Consider only fully opaque pixels
                    if (alpha == 255) {
                        int red = (rgb >> 16) & 0xFF;
                        int green = (rgb >> 8) & 0xFF;
                        int blue = rgb & 0xFF;
                        int pixel = (red << 16) | (green << 8) | blue;

                        // Check distance to existing colors in the palette
                        boolean isDuplicate = colorCount.keySet().stream()
                                .anyMatch(existingColor -> getColorDistance(existingColor, pixel) <= maxColorDistance);

                        if (!isDuplicate) {
                            // Increment the color count
                            int count = colorCount.getOrDefault(pixel, 0);
                            colorCount.put(pixel, count + 1);
                        }
                    }
                }
            }

            // Sort the colors by count in descending order
            List<Color> colorPalette = colorCount.entrySet().stream()
                    .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                    .limit(numColors)
                    .map(entry -> new Color(entry.getKey()))
                    .collect(Collectors.toList());

            return colorPalette;

        } catch (Exception e) {
            throw new Exception();
        }
    }

    private double getColorDistance(int color1, int color2) {
        int rDiff = (color1 >> 16 & 0xFF) - (color2 >> 16 & 0xFF);
        int gDiff = (color1 >> 8 & 0xFF) - (color2 >> 8 & 0xFF);
        int bDiff = (color1 & 0xFF) - (color2 & 0xFF);
        return Math.sqrt(rDiff * rDiff + gDiff * gDiff + bDiff * bDiff);
    }
	
}
