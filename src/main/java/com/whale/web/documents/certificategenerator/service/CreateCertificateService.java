package com.whale.web.documents.certificategenerator.service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.whale.web.configurations.UploadImage;
import com.whale.web.documents.certificategenerator.model.Certificate;

@Service
public class CreateCertificateService {
	
	@Autowired
	UploadImage uploadImage;
	
	@Autowired
	EditImageService editImageService;
	
	public byte[] createCertificates(Certificate certificate, List<String> names) throws Exception {
		
	    Integer x = certificate.getX();
	    Integer y = certificate.getY();
	    Integer fontSize = certificate.getFontSize();
	    
	    MultipartFile imageLayoult = uploadImage.uploadImage(certificate.getimageLayout());
	    
	    if(x == null || y == null || fontSize == null || imageLayoult == null || imageLayoult.isEmpty()) {
	    	throw new Exception();
	    }
	    
	    List<byte[]> imagesWithText = editImageService.editImage(imageLayoult, names, x, y, fontSize);

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
