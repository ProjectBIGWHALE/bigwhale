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


@Controller
@RequestMapping("/certificados")
public class CertificadosController {
	
	@Autowired
	com.whale.web.certifies.service.ProcessarPlanilhaService processarPlanilhaService;
	
	@Autowired
	com.whale.web.certifies.model.PlanilhaEFormulario planilhaEFormulario;
	
	@Autowired
	com.whale.web.certifies.service.CriarCertificadosService criarCertificadosService;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String paginaInicial() {
	
		return "indexcertifies";
	}
	
	@RequestMapping(value="/geradorDeCertificados", method=RequestMethod.GET)
	public String geradorDeCertificados(Model model) {
		
		model.addAttribute("planilhaEFormulario",planilhaEFormulario);
		return "certificados";
		
	}
	
	@RequestMapping(value="/baixarimagens", method=RequestMethod.POST)
	public ResponseEntity<byte[]> baixarimagens(com.whale.web.certifies.model.PlanilhaEFormulario planilhaEFormulario) throws Exception {
	    
	    List<String> nomes = processarPlanilhaService.salvandoNomesEmUmaLista(planilhaEFormulario.getPlanilha().getPlanilha(), planilhaEFormulario.getFormulario());
	    
        byte[] arquivoZip = criarCertificadosService.criarCertificados(planilhaEFormulario.getFormulario(), nomes);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename("certificados.zip").build());

        return new ResponseEntity<>(arquivoZip, headers, HttpStatus.OK);
	    
	}
	
	
}
