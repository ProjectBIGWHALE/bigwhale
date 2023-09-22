package com.whale.web.documents.qrcodegenerator.model;

import org.springframework.stereotype.Component;

@Component
public class QRCodeGeneratorForm {
	

	private String dataType;

	private String link;

	private String phoneNumber;

	private String text;

	private String email;

	private String titleEmail;

	private String textEmail;

	private String pixelColor;

	public String getPixelColor() {
		return pixelColor;
	}

	public void setPixelColor(String pixelColor) {
		this.pixelColor = pixelColor;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTitleEmail() {
		return titleEmail;
	}

	public void setTitleEmail(String titleEmail) {
		this.titleEmail = titleEmail;
	}

	public String getTextEmail() {
		return textEmail;
	}

	public void setTextEmail(String textEmail) {
		this.textEmail = textEmail;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
		
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getLink() {
		return this.link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
	
}
