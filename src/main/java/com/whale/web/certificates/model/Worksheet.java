package com.whale.web.certificates.model;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class Worksheet {
	
	private MultipartFile worksheet;

	public MultipartFile getWorksheet() {
		return worksheet;
	}

	public void setWorksheet(MultipartFile worksheet) {
		this.worksheet = worksheet;
	}
	
}
