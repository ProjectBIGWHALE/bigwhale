package com.whale.web.colors.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.whale.web.colors.model.FormularioColors;
import com.whale.web.colors.service.AlterarCorService;


@Controller
@RequestMapping("/cores")
public class CoresController {
	
	@Autowired
	AlterarCorService alterarCorService;
	
	@Autowired
	FormularioColors formulario;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String paginaInicial() {
	
		return "/colors/index";
	}
	
	@RequestMapping(value="/imagemtransparente", method=RequestMethod.GET)
	public String geradorDeCertificados(Model model) {
		
		model.addAttribute("formulario", formulario);
		return "/colors/cores";
		
	}
	
	@PostMapping("/processarimagem")
	public void processarImagem(FormularioColors formulario, HttpServletResponse response) throws IOException{
		
	    File imagemProcessada = alterarCorService.alterarCor(formulario.getImagem(), formulario.getCorDaImagem(), formulario.getCorParaAlteracao(), formulario.getMargem());

	    // Define o tipo de conteúdo e o tamanho da resposta
	    response.setContentType("image/png");
	    response.setContentLength((int) imagemProcessada.length());

	    // Define os cabeçalhos para permitir que a imagem seja baixada
	    response.setHeader("Content-Disposition", "attachment; filename=\"ImagemAlterada.png\"");
	    response.setHeader("Cache-Control", "no-cache");

	    // Escreve a imagem modificada na resposta
	    try (InputStream is = new FileInputStream(imagemProcessada)) {
	        IOUtils.copy(is, response.getOutputStream());
	    } catch (IOException e) {
	        throw new RuntimeException("Erro ao escrever imagem na resposta.", e);
	    }
	}
	
}
