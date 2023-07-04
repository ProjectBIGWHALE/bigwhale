package com.whale.web.security.controller;

import java.io.IOException;
import java.io.OutputStream;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;


import java.util.Properties;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.whale.web.security.model.CryptoFormSecurity;
import com.whale.web.security.service.EncryptService;

@Controller
@RequestMapping("/security")
public class SecurityController {
	
	@Autowired
	CryptoFormSecurity form;
	
	@Autowired
	EncryptService encryptService;
	
	@RequestMapping(value="/encrypt", method=RequestMethod.GET)
	public String certificateGenerator(Model model) {
		
		model.addAttribute("form", form);
		return "securityencrypt";
		
	}
	
	
	@PostMapping("/encryptfile")
	public String encryptFile(CryptoFormSecurity form, HttpServletResponse response) throws IOException{
		
		
		// Create a sentence annotation pipeline
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // Sample Text
        String text = "Teaching is not for sensitive souls. While reviewing future, past, and present tenses with my English class, I posed this question: “‘I am beautiful’ is what tense?” One student raised...";

        // Sentence annotation and sentiment analysis
        Annotation annotation = new Annotation(text);
        pipeline.annotate(annotation);

        // Get the sentiment notes
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap verdict : sentences) {
            String feeling = verdict.get(SentimentCoreAnnotations.SentimentClass.class);
            System.out.println("Verdict: " + verdict);
            System.out.println("Sentimento: " + feeling);
            System.out.println();
        }
        
      
		try {
			
			byte[] encryptedFile;
			
			if(form.getAction() == true) {
				encryptedFile = encryptService.encryptFile(form);
				response.setHeader("Content-Disposition", "attachment; filename=encryptedFile");
			} else {
				encryptedFile = encryptService.decryptFile(form);
				response.setHeader("Content-Disposition", "attachment; filename=encryptedFile");
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
