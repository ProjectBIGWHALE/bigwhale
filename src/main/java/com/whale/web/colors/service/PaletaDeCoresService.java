package com.whale.web.colors.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
public class PaletaDeCoresService {

    public List<Color> criarPaletaDeCores(MultipartFile imagem) throws Exception {
        int numColors = 10; // Número de cores predominantes a serem extraídas
        int maxColorDistance = 70; // Distância máxima permitida entre cores

        try {
            BufferedImage image = ImageIO.read(imagem.getInputStream());
            int width = image.getWidth();
            int height = image.getHeight();

            // Contagem das cores
            Map<Integer, Integer> colorCount = new HashMap<>();

            // Percorre todos os pixels da imagem
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = image.getRGB(x, y);
                    int alpha = (rgb >> 24) & 0xFF;

                    // Considera apenas os pixels completamente opacos
                    if (alpha == 255) {
                        int red = (rgb >> 16) & 0xFF;
                        int green = (rgb >> 8) & 0xFF;
                        int blue = rgb & 0xFF;
                        int pixel = (red << 16) | (green << 8) | blue;

                        // Verifica a distância para as cores existentes na paleta
                        boolean isDuplicate = colorCount.keySet().stream()
                                .anyMatch(existingColor -> getColorDistance(existingColor, pixel) <= maxColorDistance);

                        if (!isDuplicate) {
                            // Incrementa a contagem da cor
                            int count = colorCount.getOrDefault(pixel, 0);
                            colorCount.put(pixel, count + 1);
                        }
                    }
                }
            }

            // Ordena as cores pela contagem em ordem decrescente
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
