package com.whale.web.design;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.whale.web.configurations.UploadImage;
import com.whale.web.design.altercolor.model.AlterColorForm;
import com.whale.web.design.altercolor.service.AlterColorService;
import com.whale.web.design.colorspalette.model.PaletteForm;
import com.whale.web.design.colorspalette.service.CreateColorsPaletteService;


@Controller
@RequestMapping("/design")
public class DesignController {
	
	@Autowired
	AlterColorForm alterColorForm;
	
	@Autowired
	AlterColorService alterColorService;
	
	@Autowired
	UploadImage uploadImage;
	
	@Autowired
	PaletteForm paletteForm;
	
	@Autowired
	CreateColorsPaletteService createColorsPaletteService;
	
	// ALTER COLOR
	
	@GetMapping(value="/altercolor")
	public String alterColor(Model model) {
		
		model.addAttribute("form", alterColorForm);
		return "altercolor";
	}
	

	@PostMapping("/altercolor")
	public String alterColor(AlterColorForm form, HttpServletResponse response) throws IOException {
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
	        response.sendRedirect("/design/altercolor");
	    }
	    
	    return null;
	}
	
	// PALETTE COLOR
	
	@GetMapping(value="/colorspalette")
	public String colorsPalette(Model model) {
		
		model.addAttribute("form", paletteForm);
		return "colorspalette";
		
	}
	
	@PostMapping("/colorspalette")
	public String colorsPalette(PaletteForm form, Model model) throws Exception {
		
		MultipartFile upload = uploadImage.uploadImage(form.getImage());
		byte[] image = upload.getBytes();
		
		try {
			List<Color> listOfColors = createColorsPaletteService.createColorPalette(upload);
			form.setListOfColors(listOfColors);
			
			// Convert the image to base64
	        String imageBase64 = Base64.getEncoder().encodeToString(image);
			model.addAttribute("form", form);
			// Add the base64 image to the template
			model.addAttribute("imageBase64", imageBase64);

			return "paletteview";
			
		} catch (Exception e) {
			return "redirect:/design/colorspalette";
		}
		
	}
	
}
