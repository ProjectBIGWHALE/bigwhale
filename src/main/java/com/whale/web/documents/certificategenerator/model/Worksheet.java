package com.whale.web.documents.certificategenerator.model;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class Worksheet {
	
	private MultipartFile worksheet;

	public MultipartFile getWorksheet() {
		return worksheet;
	}

	public void setWorksheet(MultipartFile worksheet) {
		this.worksheet = worksheet;
	}
	
}
