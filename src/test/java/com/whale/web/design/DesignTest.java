package com.whale.web.design;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URI;

import javax.imageio.ImageIO;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.whale.web.design.altercolor.model.AlterColorForm;
import com.whale.web.design.colorspalette.model.PaletteForm;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest
class DesignTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void shouldReturnTheHTMLFormForChangeColorsApp() throws Exception {
		
		URI uri = new URI("/design/altercolor");
		mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(
				MockMvcResultMatchers.status().is(200));
		
	}
	
	@Test
	void shouldReturnTheHTMLFormForPaletteColorsApp() throws Exception {
		
		URI uri = new URI("/design/colorspalette");
		mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(
				MockMvcResultMatchers.status().is(200));
		
	}
	
    @Test
    void shouldReturnAValidPNGProcessedImage() throws Exception {        
    	
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    	Graphics2D graphics = image.createGraphics();
    	graphics.setColor(Color.WHITE);
    	graphics.fillRect(0, 0, 100, 100);
    	graphics.dispose();
    	
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	ImageIO.write(image, "png", baos);
    	byte[] imageBytes = baos.toByteArray();
        
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "white_image.png",
                MediaType.IMAGE_PNG_VALUE,
                imageBytes
        );
        
        AlterColorForm formColors = new AlterColorForm();
        formColors.setColorForAlteration("#FF0000");
        formColors.setColorOfImage("#FFFFFF");
        formColors.setMargin(4.0);
        formColors.setImage(file);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/design/altercolor")
                .file(file)
                .param("colorForAlteration", formColors.getColorForAlteration())
                .param("colorOfImage", formColors.getColorOfImage())
                .param("margin", String.valueOf(formColors.getMargin())))
			.andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.header().string("Content-Disposition", Matchers.containsString("attachment; filename=ModifiedImage.png")))
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.IMAGE_PNG));
    }

    
    @Test
    void shouldReturnARedirectionStatusCode302() throws Exception {
    	
    	byte[] imageBytes = null;  	
    	
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "white_image.png",
                MediaType.IMAGE_PNG_VALUE,
                imageBytes
        );    	    	
       
        AlterColorForm formColors = new AlterColorForm();
        formColors.setColorForAlteration("#000000");
        formColors.setColorOfImage("#FFFFFF");
        formColors.setMargin(4.0);
        formColors.setImage(null);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/design/altercolor")
                .file(file)
                .param("colorForAlteration", formColors.getColorForAlteration())
                .param("colorOfImage", formColors.getColorOfImage())
                .param("margin", String.valueOf(formColors.getMargin())))
                .andExpect(MockMvcResultMatchers.status().is(302));
    	
    }

    
    @Test
    void shouldReturnAPaletteColorsPageView() throws Exception {
    	
    	PaletteForm formPalette = new PaletteForm();
    	    	
    	BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    	Graphics2D graphics = image.createGraphics();
    	graphics.setColor(Color.WHITE);
    	graphics.fillRect(0, 0, 100, 100);
    	graphics.dispose();
    	
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	ImageIO.write(image, "png", baos);
    	byte[] imageBytes = baos.toByteArray();
        
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "white_image.png",
                MediaType.IMAGE_PNG_VALUE,
                imageBytes
        );
    	
        formPalette.setImage(file);
        
        mockMvc.perform(MockMvcRequestBuilders.multipart("/design/colorspalette")
                .file(file)
                .flashAttr("form", formPalette))
                .andExpect(MockMvcResultMatchers.status().is(200));
    	
    }



}
