package com.whale.web.certificates.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public class CustomMultipartFile implements MultipartFile {

	 	private final String name;
	    private final String contentType;
	    private final InputStream inputStream;

	    public CustomMultipartFile(String name, String contentType, InputStream inputStream) {
	        this.name = name;
	        this.contentType = contentType;
	        this.inputStream = inputStream;
	    }

	    @Override
	    public String getName() {
	        return this.name;
	    }

	    @Override
	    public String getOriginalFilename() {
	        return this.name;
	    }

	    @Override
	    public String getContentType() {
	        return this.contentType;
	    }

	    @Override
	    public boolean isEmpty() {
	        return false;
	    }

	    @Override
	    public long getSize() {
	        return -1;
	    }

	    @Override
	    public byte[] getBytes() throws IOException {
	        return new byte[0];
	    }

	    @Override
	    public InputStream getInputStream() throws IOException {
	        return this.inputStream;
	    }

	    @Override
	    public void transferTo(File file) throws IOException, IllegalStateException {
	        
	    }

}
