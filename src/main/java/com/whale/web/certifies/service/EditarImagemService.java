package com.whale.web.certifies.service;

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
public class EditarImagemService {
	
	public List<byte[]> editarImagem(MultipartFile layoultPadrao, List<String> nomes, Integer x, Integer y, Integer tamanhoDaFonte) throws IOException {
	    
		List<byte[]> imagensComTexto = new ArrayList<>();

	    BufferedImage imagem = ImageIO.read(layoultPadrao.getInputStream());

	    for (String nome : nomes) {
	    	
	        //criar uma imagem em branco com o mesmo tamanho da original
	        BufferedImage imagemComTexto = new BufferedImage(imagem.getWidth(), imagem.getHeight(), BufferedImage.TYPE_INT_RGB);
	        
	        Graphics2D g2d = imagemComTexto.createGraphics();
	        g2d.drawImage(imagem, 0, 0, null);

	        g2d.setColor(Color.BLACK);
	        g2d.setFont(new Font("Arial", Font.BOLD, tamanhoDaFonte));
	        g2d.drawString(nome, x, y); // coordenadas x=100, y=100

	        ByteArrayOutputStream armazenamentoTemporario = new ByteArrayOutputStream();
	        ImageIO.write(imagemComTexto, "png", armazenamentoTemporario);

	        //adicionar a imagem com texto na lista
	        imagensComTexto.add(armazenamentoTemporario.toByteArray());
	    }

	    return imagensComTexto;
	}
	
}
