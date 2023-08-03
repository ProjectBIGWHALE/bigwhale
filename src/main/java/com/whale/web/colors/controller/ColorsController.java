package com.whale.web.colors.controller;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import com.whale.web.colors.model.FormConvert;
import com.whale.web.colors.model.Format;
import com.whale.web.colors.service.ConvertImageFormatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.whale.web.colors.model.FormColors;
import com.whale.web.colors.model.FormPalette;
import com.whale.web.colors.service.AlterColorService;
import com.whale.web.colors.service.ColorPaletteService;
import com.whale.web.colors.service.UploadImageColorService;
import org.springframework.util.StringUtils;

@Controller
@RequestMapping("/colors")
public class ColorsController {
	
	@Autowired
	AlterColorService alterColorService;

	@Autowired
	FormColors form;

	@Autowired
	FormConvert formConvert;
	
	@Autowired
	ColorPaletteService colorPaletteService;
	
	@Autowired
	UploadImageColorService uploadImageService;

	@Autowired
	ConvertImageFormatService convertImageFormatService;
	
	@RequestMapping(value="/transparentimage", method=RequestMethod.GET)
	public String certificateGenerator(Model model) {
		
		model.addAttribute("form", form);
		return "transparentimage";
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

	@RequestMapping(value = "/imageconversion", method = RequestMethod.GET)
	public String convertimage(Model model) {
		List<Format> list = Arrays.asList(new Format(1L, "png"),
											new Format(2L, "bmp"),
											new Format(3L, "jpg"),
											new Format(4L, "jpeg"));

		model.addAttribute("list", list);
		model.addAttribute("form", formConvert);
		return "imageconversion";
	}



// ...

	@PostMapping("/convertformatimage")
	public String convertAndDownloadImage(@ModelAttribute("form") FormConvert formConvert, HttpServletResponse response) throws Exception {

		try {
			byte[] formattedImage = convertImageFormatService.convertImageFormat(formConvert.getOutputFormat(), formConvert.getImage());

			// Extrair o nome do arquivo original sem sua extensão e adicionar o novo formato.
			String originalFileName = StringUtils.stripFilenameExtension(Objects.requireNonNull(formConvert.getImage().getOriginalFilename()));
			String convertedFileName = originalFileName + "." + formConvert.getOutputFormat().toLowerCase();

			// Define os cabeçalhos da resposta para fazer o download
			response.setContentType("image/" + formConvert.getOutputFormat().toLowerCase());
			response.setHeader("Content-Disposition", "attachment; filename=" + convertedFileName);
			response.setHeader("Cache-Control", "no-cache");

			// Copia os bytes do arquivo para o OutputStream
			try (OutputStream os = response.getOutputStream()) {
				os.write(formattedImage);
				os.flush();
			}

		} catch (Exception e) {
			return "redirect:/colors/imageconversion";
		}
		return null;
	}



}
