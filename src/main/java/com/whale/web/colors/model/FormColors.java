package com.whale.web.colors.model;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FormColors {
	
	private MultipartFile image;
	private String colorOfImage;
	private String colorForAlteration;
	private Integer margin;
	
	
	public MultipartFile getImage() {
		return image;
	}
	public void setImage(MultipartFile image) {
		this.image = image;
	}
	public String getColorOfImage() {
		return colorOfImage;
	}
	public void setColorOfImage(String colorOfImage) {
		this.colorOfImage = colorOfImage;
	}
	public String getColorForAlteration() {
		return colorForAlteration;
	}
	public void setColorForAlteration(String colorForAlteration) {
		this.colorForAlteration = colorForAlteration;
	}
	public Integer getMargin() {
		return margin;
	}
	public void setMargin(Integer margin) {
		this.margin = margin;
	}
	
}
