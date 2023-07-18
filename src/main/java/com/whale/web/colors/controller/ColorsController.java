package com.whale.web.colors.controller;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.whale.web.colors.model.FormColors;
import com.whale.web.colors.model.FormPalette;
import com.whale.web.colors.service.AlterColorService;
import com.whale.web.colors.service.ColorPaletteService;
import com.whale.web.colors.service.UploadImagemServiceColors;


@Controller
@RequestMapping("/colors")
public class ColorsController {
	
	@Autowired
	AlterColorService alterColorService;
	
	@Autowired
	FormColors form;
	
	@Autowired
	ColorPaletteService colorPaletteService;
	
	@Autowired
	UploadImagemServiceColors uploadImageService;
	
	@RequestMapping(value="/transparentimage", method=RequestMethod.GET)
	public String certificateGenerator(Model model) {
		
		model.addAttribute("form", form);
		return "colors";
		
	}
	

	@PostMapping("/processimage")
	public String processImage(FormColors form, HttpServletResponse response) throws IOException {
	    try {
	        byte[] processedImage = alterColorService.alterColor(form.getImage(), form.getColorOfImage(), form.getColorForAlteration(), form.getMargin());

	        // Define o tipo de conteúdo e o tamanho da resposta
	        response.setContentType("image/png");
	        response.setHeader("Content-Disposition", "attachment; filename=\"ModifiedImage.png\"");
	        response.setHeader("Cache-Control", "no-cache");

	        // Copia os bytes do arquivo para o OutputStream
	        try (OutputStream os = response.getOutputStream()) {
	            os.write(processedImage);
	            os.flush();
	        }
	    } catch (Exception e) {
	        response.sendRedirect("/colors/transparentimage");
	    }
	    
	    return null;
	}


	
	@RequestMapping(value="/colorspalette", method=RequestMethod.GET)
	public String colorsPalette(Model model) {
		
		model.addAttribute("form", form);
		return "colorspalette";
		
	}
	
	@PostMapping("/createpalette")
	public String createPalette(FormPalette form, Model model) throws Exception {
		
		MultipartFile upload = uploadImageService.uploadImage(form.getImage());
		byte[] image = upload.getBytes();
		
		try {
			List<Color> listOfColors = colorPaletteService.createColorPalette(upload);
			form.setListOfColors(listOfColors);
			
			// Convert the image to base64
	        String imageBase64 = Base64.getEncoder().encodeToString(image);
			model.addAttribute("form", form);
			// Add the base64 image to the template
			model.addAttribute("imageBase64", imageBase64);

			return "paletteview";
			
		} catch (Exception e) {
			return "redirect:/colors/colorspalette";
		}
		
	}
	
}
