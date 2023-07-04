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

import com.whale.web.colors.model.FormColors;
import com.whale.web.colors.model.FormPalette;
import com.whale.web.colors.service.AlterColorService;
import com.whale.web.colors.service.ColorPaletteService;


@Controller
@RequestMapping("/colors")
public class ColorsController {
	
	@Autowired
	AlterColorService alterCorService;
	
	@Autowired
	FormColors form;
	
	@Autowired
	ColorPaletteService colorPaletteService;
	
	@RequestMapping(value="/transparentimage", method=RequestMethod.GET)
	public String certificateGenerator(Model model) {
		
		model.addAttribute("form", form);
		return "colors";
		
	}
	
	@PostMapping("/processimage")
	public String processImage(FormColors form, HttpServletResponse response) throws IOException{
		
		try {
		    File processedImage = alterCorService.alterColor(form.getImage(), form.getColorOfImagem(), form.getColorForAlteration(), form.getMargin());
	
		    // Define the content type and size of the response
		    response.setContentType("image/png");
		    response.setContentLength((int) processedImage.length());
	
		    // Set headers to allow the image to be downloaded
		    response.setHeader("Content-Disposition", "attachment; filename=\"ModifiedImage.png\"");
		    response.setHeader("Cache-Control", "no-cache");
	
		    // Write the modified image in the answer
		    try (InputStream is = new FileInputStream(processedImage)) {
		        IOUtils.copy(is, response.getOutputStream());
		    
		    } catch (IOException e) {
		        throw new RuntimeException("Error writing image in response.", e);
		    }
		
		} catch(Exception e) {
			return "redirect:/colors/transparentimage";
		}
		return null;
		
	}
	
	@RequestMapping(value="/colorpalette", method=RequestMethod.GET)
	public String paletaDeCores(Model model) {
		
		model.addAttribute("form", form);
		return "colorpalette";
		
	}
	
	@PostMapping("/createpalette")
	public String criarPaleta(FormPalette form, Model model) {
		
		try {
			List<Color> listOfColors = colorPaletteService.createColorPalette(form.getImage());
			form.setListOfColors(listOfColors);
			
			// Convert the image to base64
	        String imageBase64 = Base64.getEncoder().encodeToString(form.getImage().getBytes());
			
			model.addAttribute("formulario", form);
			model.addAttribute("imageBase64", imageBase64); // Add the base64 image to the template
			return "paletteview";
			
		} catch (Exception e) {
			return "redirect:/colors/colorspalette";
		}
		
	}
	
}
