package com.whale.web.security.model;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class CryptographyFormSecurity {
	
	private MultipartFile file;
	private String key;
	private Boolean action;
	
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Boolean getAction() {
		return action;
	}
	public void setAction(Boolean action) {
		this.action = action;
	}
	
}
