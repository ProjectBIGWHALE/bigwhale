package com.whale.web.certifies.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;


@Controller
@RequestMapping("/certificates")
public class CertificadosController {
	
	@Autowired
	com.whale.web.certifies.service.ProcessWorksheetService processWorksheetService;
	
	@Autowired
	com.whale.web.certifies.model.WorksheetAndForm worksheetAndForm;
	
	@Autowired
	com.whale.web.certifies.service.CreateCertificateService createCertificateService;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String homePage() {
	
		return "indexcertifies";
	}
	
	@RequestMapping(value="/certificateGenerator", method=RequestMethod.GET)
	public String certificateGenerator(Model model) {
		
		model.addAttribute("worksheetAndForm", worksheetAndForm);
		return "certificates";
		
	}
	
	@RequestMapping(value="/downloadimages", method=RequestMethod.POST)
	public ResponseEntity<byte[]> downloadImages(com.whale.web.certifies.model.WorksheetAndForm worksheetAndForm) throws Exception {
	    
		try {
			
		    List<String> names = processWorksheetService.savingNamesInAList(worksheetAndForm.getWorksheet().getWorksheet(), worksheetAndForm.getForm());
		    
	        byte[] arquivoZip = createCertificateService.createCertificates(worksheetAndForm.getForm(), names);
	
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	        headers.setContentDisposition(ContentDisposition.builder("attachment").filename("certificados.zip").build());
	        
	        return new ResponseEntity<>(arquivoZip, headers, HttpStatus.valueOf(200));
		} catch(Exception e) {
			
			HttpHeaders headers = new HttpHeaders();
		    headers.setLocation(UriComponentsBuilder.fromPath("/certificates/certificateGenerator").build().toUri());
		    return new ResponseEntity<>(headers, HttpStatus.FOUND);
		}
        
	    
	}
	
	
}
