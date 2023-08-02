package com.whale.web.security.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.whale.web.security.model.CryptographyFormSecurity;
import com.whale.web.security.service.EncryptService;

@Controller
@RequestMapping("/security")
public class SecurityController {
	
	@Autowired
	CryptographyFormSecurity form;
	
	@Autowired
	EncryptService encryptService;
	
	@RequestMapping(value="/encrypt", method=RequestMethod.GET)
	public String encrypt(Model model) {
		
		model.addAttribute("form", form);
		return "securityencrypt";
		
	}
	
	
	@PostMapping("/encryptfile")
	public String encryptFile(CryptographyFormSecurity form, HttpServletResponse response) throws IOException{
      
		try {
			
			byte[] encryptedFile;
			
			if(form.getAction()) {
				encryptedFile = encryptService.encryptFile(form);
				response.setHeader("Content-Disposition", "attachment; filename=encryptedFile");
			} else {
				encryptedFile = encryptService.decryptFile(form);
				response.setHeader("Content-Disposition", "attachment; filename=decryptedFile");
			}
			// Defines the content type and size of the response
			response.setContentType("application/octet-stream");
		    response.setContentLength(encryptedFile.length);
	
		    // Set headers to allow the image to be downloaded		    
		    response.setHeader("Cache-Control", "no-cache");
			
		    try (OutputStream outputStream = response.getOutputStream()) {
		        outputStream.write(encryptedFile);
		        outputStream.flush();
		    }catch(Exception e) {
				throw new RuntimeException("Error generating encrypted file", e);
			}
		} catch(Exception e) {
			return "redirect:/security/encrypt";
		}
		
		return null;
	}
	
}
