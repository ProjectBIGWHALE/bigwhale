package com.whale.web.colors.model;

import java.awt.Color;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FormularioPaleta {
	
	private MultipartFile imagem;
	private List<Color> listaDeCores;

	public MultipartFile getImagem() {
		return imagem;
	}

	public void setImagem(MultipartFile imagem) {
		this.imagem = imagem;
	}
	public List<Color> getListaDeCores() {
		return listaDeCores;
	}

	public void setListaDeCores(List<Color> listaDeCores) {
		this.listaDeCores = listaDeCores;
	}
	
	
}
