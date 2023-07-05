package com.whale.web.certificates.model;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FormCertifies {
	
	private MultipartFile imageLayout;
	private String personName;
	private Integer x;
	private Integer y;
	private Integer fontSize;
	
	public MultipartFile getimageLayout() {
		return imageLayout;
	}
	public void setImageLayout(MultipartFile imageLayout) {
		this.imageLayout = imageLayout;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public Integer getX() {
		return x;
	}
	public void setX(Integer x) {
		this.x = x;
	}
	public Integer getY() {
		return y;
	}
	public void setY(Integer y) {
		this.y = y;
	}
	public Integer getFontSize() {
		return fontSize;
	}
	public void setFontSize(Integer fontSize) {
		this.fontSize = fontSize;
	}
	
}
