package com.whale.web.certificates.controller;

import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/certificates")
public class CertificatesController {
	
	@Autowired
	com.whale.web.certificates.service.ProcessWorksheetService processWorksheetService;
	
	@Autowired
	com.whale.web.certificates.model.WorksheetAndForm worksheetAndForm;
	
	@Autowired
	com.whale.web.certificates.service.CreateCertificateService createCertificateService;
	
	@RequestMapping(value="/certificategenerator", method=RequestMethod.GET)
	public String certificateGenerator(Model model) {
		
		model.addAttribute("worksheetAndForm", worksheetAndForm);
		return "certificates";
		
	}
	
	@RequestMapping(value = "/downloadimages", method = RequestMethod.POST)
	public String downloadImages(com.whale.web.certificates.model.WorksheetAndForm worksheetAndForm, HttpServletResponse response) throws Exception {
		try {
		    List<String> names = processWorksheetService.savingNamesInAList(worksheetAndForm.getWorksheet().getWorksheet(), worksheetAndForm.getForm());
		    byte[] zipFile = createCertificateService.createCertificates(worksheetAndForm.getForm(), names);
	
		    
		        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
		        response.setHeader("Content-Disposition", "attachment; filename=\"certificates.zip\"");
	
		        try (OutputStream outputStream = response.getOutputStream()) {
			        outputStream.write(zipFile);
			        outputStream.flush();
			    }catch(Exception e) {
					throw new RuntimeException("Error generating encrypted file", e);
				}
	    } catch (Exception e) {
	    	return "redirect:/certificates/certificategenerator";
	    }
	    
	    return null;
	}
	
	
}
