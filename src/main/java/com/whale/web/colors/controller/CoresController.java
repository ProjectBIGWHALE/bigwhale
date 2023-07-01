package com.whale.web.colors.controller;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.whale.web.colors.model.FormularioColors;
import com.whale.web.colors.model.FormularioPaleta;
import com.whale.web.colors.service.AlterarCorService;
import com.whale.web.colors.service.PaletaDeCoresService;


@Controller
@RequestMapping("/cores")
public class CoresController {
	
	@Autowired
	AlterarCorService alterarCorService;
	
	@Autowired
	FormularioColors formulario;
	
	@Autowired
	PaletaDeCoresService paletaDeCoresService;
	
	@RequestMapping(value="/imagemtransparente", method=RequestMethod.GET)
	public String geradorDeCertificados(Model model) {
		
		model.addAttribute("formulario", formulario);
		return "cores";
		
	}
	
	@PostMapping("/processarimagem")
	public String processarImagem(FormularioColors formulario, HttpServletResponse response) throws IOException{
		
		try {
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
		
		} catch(Exception e) {
			return "redirect:/cores/imagemtransparente";
		}
		return null;
		
	}
	
	@RequestMapping(value="/paletadecores", method=RequestMethod.GET)
	public String paletaDeCores(Model model) {
		
		model.addAttribute("formulario", formulario);
		return "paletadecores";
		
	}
	
	@PostMapping("/criarpaleta")
	public String criarPaleta(FormularioPaleta formulario, Model model) {
		
		try {
			List<Color> listaDeCores = paletaDeCoresService.criarPaletaDeCores(formulario.getImagem());
			formulario.setListaDeCores(listaDeCores);
			
			// Converter a imagem em base64
	        String imagemBase64 = Base64.getEncoder().encodeToString(formulario.getImagem().getBytes());
			
			model.addAttribute("formulario", formulario);
			model.addAttribute("imagemBase64", imagemBase64); // Adicionar a imagem base64 ao modelo
			return "visualizacaodapaleta";
			
		} catch (Exception e) {
			return "redirect:/cores/paletadecores";
		}
		
	}
	
}
