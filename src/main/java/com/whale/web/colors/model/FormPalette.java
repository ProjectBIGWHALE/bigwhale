package com.whale.web.colors.model;

import java.awt.Color;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FormPalette {
	
	private MultipartFile image;
	private List<Color> listOfColors;

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}
	public List<Color> getListOfColors() {
		return listOfColors;
	}

	public void setListOfColors(List<Color> listOfColors) {
		this.listOfColors = listOfColors;
	}
	
	
}
