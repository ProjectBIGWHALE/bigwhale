package com.whale.web.certifies.service;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.whale.web.certifies.model.FormularioCertifies;

@Service
public class ProcessarPlanilhaService {
	
	public List<String> salvandoNomesEmUmaLista(MultipartFile planilha, FormularioCertifies formularioService) throws Exception {
		
		if(planilha.isEmpty()) {
			throw new Exception();
		}
		
		List<String> nomes = new ArrayList();
		
	    try (CSVReader reader = new CSVReader(new InputStreamReader(planilha.getInputStream(), StandardCharsets.UTF_8))) {
	        List<String[]> linhas = reader.readAll();
	        for (String[] linha : linhas) {
	            if (linha.length > 0) {
	                nomes.add(linha[0]);
	            }
	        }
	    }
	    
        return nomes;
		
	}
	
}
