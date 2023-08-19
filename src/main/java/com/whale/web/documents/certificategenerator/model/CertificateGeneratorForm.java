package com.whale.web.documents.certificategenerator.model;

import org.springframework.stereotype.Component;

@Component
public class CertificateGeneratorForm {
	
	private Worksheet worksheet;
	private Certificate certificate;
	
	public Worksheet getWorksheet() {
		return worksheet;
	}
	public void setWorksheet(Worksheet worksheet) {
		this.worksheet = worksheet;
	}
	public Certificate getCertificate() {
		return certificate;
	}
	public void setCertificate(Certificate certificate) {
		this.certificate = certificate;
	}
	
}
