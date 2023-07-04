package com.whale.web.certifies.model;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FormCertifies {
	
	private MultipartFile imageLayoult;
	private String personName;
	private Integer x;
	private Integer y;
	private Integer fontSize;
	
	public MultipartFile getImageLayoult() {
		return imageLayoult;
	}
	public void setImagemLayoult(MultipartFile imageLayoult) {
		this.imageLayoult = imageLayoult;
	}
	public String getNomeDaPessoa() {
		return personName;
	}
	public void setNomeDaPessoa(String personName) {
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
