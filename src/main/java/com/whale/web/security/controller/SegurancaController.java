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

import com.whale.web.security.model.FormularioSecurity;
import com.whale.web.security.service.EncriptografarService;

@Controller
@RequestMapping("/seguranca")
public class SegurancaController {
	
	@Autowired
	FormularioSecurity formulario;
	
	@Autowired
	EncriptografarService encriptografarService;
	
	@RequestMapping(value="/encriptografar", method=RequestMethod.GET)
	public String geradorDeCertificados(Model model) {
		
		model.addAttribute("formulario", formulario);
		return "seguranca";
		
	}
	
	
	@PostMapping("/criptografararquivo")
	public String criptografarArquivo(FormularioSecurity formulario, HttpServletResponse response) throws IOException{
		
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
