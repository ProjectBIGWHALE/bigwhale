package com.whale.web.documents.imageconverter.model;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageConversionForm {
	
    private MultipartFile image;
    private String outputFormat;

	public MultipartFile getImage() {
	    return image;
	}
	
	public void setImage(MultipartFile image) {
	    this.image = image;
	}
	
	public String getOutputFormat() {
	    return outputFormat;
	}
	
	public void setOutputFormat(String outputFormat) {
	    this.outputFormat = outputFormat;
	}
	
}
