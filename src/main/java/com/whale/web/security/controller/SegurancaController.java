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

import com.whale.web.security.model.FormularioCriptoSecurity;
import com.whale.web.security.service.EncriptografarService;

@Controller
@RequestMapping("/seguranca")
public class SegurancaController {
	
	@Autowired
	FormularioCriptoSecurity formulario;
	
	@Autowired
	EncriptografarService encriptografarService;
	
	@RequestMapping(value="/encriptografar", method=RequestMethod.GET)
	public String geradorDeCertificados(Model model) {
		
		model.addAttribute("formulario", formulario);
		return "segurancacriptografar";
		
	}
	
	
	@PostMapping("/criptografararquivo")
	public String criptografarArquivo(FormularioCriptoSecurity formulario, HttpServletResponse response) throws IOException{
		
		
		// Cria um pipeline de anotação de sentenças
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // Texto de exemplo
        String text = "Teaching is not for sensitive souls. While reviewing future, past, and present tenses with my English class, I posed this question: “‘I am beautiful’ is what tense?” One student raised...";

        // Anotação de sentença e análise de sentimentos
        Annotation annotation = new Annotation(text);
        pipeline.annotate(annotation);

        // Obtém as anotações de sentimentos
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            System.out.println("Sentença: " + sentence);
            System.out.println("Sentimento: " + sentiment);
            System.out.println();
        }
        
      
		try {
			
			byte[] arquivoCriptografado;
			
			if(formulario.getAcao() == true) {
				arquivoCriptografado = encriptografarService.encriptografarArquivo(formulario);
				response.setHeader("Content-Disposition", "attachment; filename=arquivoCriptografado");
			} else {
				arquivoCriptografado = encriptografarService.descriptografarArquivo(formulario);
				response.setHeader("Content-Disposition", "attachment; filename=arquivoDescriptografado");
			}
			// Define o tipo de conteúdo e o tamanho da resposta
			response.setContentType("application/octet-stream");
		    response.setContentLength(arquivoCriptografado.length);
	
		    // Define os cabeçalhos para permitir que a imagem seja baixada
		    
		    response.setHeader("Cache-Control", "no-cache");
			
		    try (OutputStream outputStream = response.getOutputStream()) {
		        outputStream.write(arquivoCriptografado);
		        outputStream.flush();
		    }catch(Exception e) {
				throw new RuntimeException("Erro ao gerar arquivo criptografado", e);
			}
		} catch(Exception e) {
			return "redirect:/seguranca/encriptografar";
		}
		
		return null;
	}
	
}
