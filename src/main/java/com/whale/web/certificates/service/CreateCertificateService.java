package com.whale.web.certificates.service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.whale.web.certificates.model.FormCertifies;

@Service
public class CreateCertificateService {
	
	@Autowired
	UploadImagemServiceCertifies uploadImageService;
	
	@Autowired
	EditImageService editImageService;
	
	public byte[] createCertificates(FormCertifies form, List<String> names) throws Exception {
		
	    Integer x = form.getX();
	    Integer y = form.getY();
	    Integer fontSize = form.getFontSize();
	    
	    MultipartFile imageLayoult = uploadImageService.uploadImage(form.getImageLayoult());
	    
	    if(x == null || y == null || fontSize == null || imageLayoult == null || imageLayoult.isEmpty()) {
	    	throw new Exception();
	    }
	    
	    List<byte[]> imagesWithText = editImageService.editImage(imageLayoult, names, y, x, fontSize);

	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    ZipOutputStream zos = new ZipOutputStream(bos);

	    for (int i = 0; i < imagesWithText.size(); i++) {
	        byte[] image = imagesWithText.get(i);
	        String personsName = names.get(i);

	        ZipEntry entry = new ZipEntry(personsName.replace(" ", "") + ".png");
	        zos.putNextEntry(entry);
	        zos.write(image);
	        zos.closeEntry();
	    }
	    zos.close();

	    return bos.toByteArray();
		

	}
	
}
