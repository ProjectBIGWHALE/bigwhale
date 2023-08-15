package com.whale.web.security;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.whale.web.security.cryptograph.model.CryptographyForm;
import com.whale.web.security.cryptograph.service.EncryptService;


@Controller
@RequestMapping("/security")
public class SecurityController {

	@Autowired
	CryptographyForm form;

	@Autowired
	EncryptService encryptService;

	@GetMapping(value="/cryptograph")
	public String cryptograph(Model model) {

		model.addAttribute("form", form);
		return "cryptograph";

	}


	@PostMapping("/cryptograph")
	public String cryptograph(CryptographyForm form, HttpServletResponse response) throws IOException{

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
			return "redirect:/security/cryptograph";
		}

		return null;
	}

}
