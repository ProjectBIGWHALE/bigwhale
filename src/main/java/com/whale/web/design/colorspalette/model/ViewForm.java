package com.whale.web.design.colorspalette.model;

import java.awt.Color;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ViewForm {
	
	private List<Color> listOfColors;
	private String imageBase64;
	
	public List<Color> getListOfColors() {
		return listOfColors;
	}
	public void setListOfColors(List<Color> listOfColors) {
		this.listOfColors = listOfColors;
	}
	public String getImageBase64() {
		return imageBase64;
	}
	public void setImageBase64(String imageBase64) {
		this.imageBase64 = imageBase64;
	}
	
	
}
