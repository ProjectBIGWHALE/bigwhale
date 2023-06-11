package com.whale.web.codes.controller;

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

import com.whale.web.codes.model.FormularioCodes;
import com.whale.web.codes.service.QRCodeService;

@Controller
@RequestMapping("/codigos")
public class CodigosController {
	
	@Autowired
	FormularioCodes formulario;
	
	@Autowired
	QRCodeService qrCodeService;
	
	@RequestMapping(value="/qrcode", method=RequestMethod.GET)
	public String geradorDeCertificados(Model model) {
		
		model.addAttribute("formulario", formulario);
		return "codigos";
		
	}
	

	@PostMapping("/gerarqrcode")
	public String processarImagem(FormularioCodes formulario, HttpServletResponse response) throws IOException{
		
		try {
			File imagemProcessada = qrCodeService.gerarQRCode(formulario.getLink());
			
			// Define o tipo de conteúdo e o tamanho da resposta
		    response.setContentType("image/png");
		    response.setContentLength((int) imagemProcessada.length());
	
		    // Define os cabeçalhos para permitir que a imagem seja baixada
		    response.setHeader("Content-Disposition", "attachment; filename=\"ImagemAlterada.png\"");
		    response.setHeader("Cache-Control", "no-cache");
			
			try (InputStream is = new FileInputStream(imagemProcessada)){
				IOUtils.copy(is, response.getOutputStream());
			}catch(Exception e) {
				throw new RuntimeException("Erro ao escrever imagem na resposta.", e);
			}
		} catch(Exception e) {
			return "redirect:/codigos/qrcode";
		}
		
		return null;
	}
}
