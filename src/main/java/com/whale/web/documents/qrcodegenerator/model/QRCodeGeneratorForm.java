package com.whale.web.documents.qrcodegenerator.model;

import org.springframework.stereotype.Component;

@Component
public class QRCodeGeneratorForm {
	
	String link;
	
	public String getLink() {
		return this.link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
}
