package com.whale.web.certificates;

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

import com.whale.web.certificates.model.FormCertifies;
import com.whale.web.certificates.model.Worksheet;
import com.whale.web.certificates.model.WorksheetAndForm;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest
public class CertificatesControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void shouldReturnTheHTMLForm() throws Exception {
		
		URI uri = new URI("/certificates/certificategenerator/");
		mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(
				MockMvcResultMatchers.status().is(200));
		
	}
	
	@Test
    public void shouldReturnTheCertificatesStatusCode200() throws Exception {
        WorksheetAndForm worksheetAndForm = new WorksheetAndForm();
        Worksheet worksheet = new Worksheet();
        
        // Crie um arquivo CSV fictício para usar nos testes
        String csvContent = "col1,col2,col3\nvalue1,value2,value3";
        
        MockMultipartFile file = new MockMultipartFile("file", "worksheet.csv", MediaType.TEXT_PLAIN_VALUE, csvContent.getBytes());
        
        // Crie um arquivo de imagem fictício para usar nos testes
    	BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    	Graphics2D graphics = image.createGraphics();
    	graphics.setColor(Color.WHITE);
    	graphics.fillRect(0, 0, 100, 100);
    	graphics.dispose();

    	// Converte a imagem em bytes
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	ImageIO.write(image, "png", baos);
    	byte[] imageBytes = baos.toByteArray();
        
        MockMultipartFile imageLayout = new MockMultipartFile(
                "image",
                "white_image.png",
                MediaType.IMAGE_PNG_VALUE,
                imageBytes
        );
        
        worksheet.setWorksheet(file);
        FormCertifies formCertifies = new FormCertifies();
        formCertifies.setFontSize(20);
        formCertifies.setX(200);
        formCertifies.setY(200);
        formCertifies.setImageLayout(imageLayout);
        
        worksheetAndForm.setForm(formCertifies);
        worksheetAndForm.setWorksheet(worksheet);
        
        
        mockMvc.perform(MockMvcRequestBuilders.multipart("/certificates/downloadimages")
        		.file(file)
                .flashAttr("worksheetAndForm", worksheetAndForm))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Disposition", Matchers.containsString("attachment")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE));
    }
	
	@Test
    public void shouldReturnARedirectionStatusCode302() throws Exception {
        WorksheetAndForm worksheetAndForm = new WorksheetAndForm();
        Worksheet worksheet = new Worksheet();
        
        // Crie um arquivo CSV fictício para usar nos testes
        String csvContent = "col1,col2,col3\nvalue1,value2,value3";
        
        MockMultipartFile file = new MockMultipartFile("file", "worksheet.csv", MediaType.TEXT_PLAIN_VALUE, csvContent.getBytes());
        
        // Crie um arquivo de imagem fictício para usar nos testes
    	BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    	Graphics2D graphics = image.createGraphics();
    	graphics.setColor(Color.WHITE);
    	graphics.fillRect(0, 0, 100, 100);
    	graphics.dispose();

    	// Converte a imagem em bytes
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	ImageIO.write(image, "png", baos);
    	byte[] imageBytes = baos.toByteArray();
        
        MockMultipartFile imageLayout = new MockMultipartFile(
                "image",
                "white_image.png",
                MediaType.IMAGE_PNG_VALUE,
                imageBytes
        );
        
        worksheet.setWorksheet(null);
        FormCertifies formCertifies = new FormCertifies();
        formCertifies.setFontSize(20);
        formCertifies.setX(200);
        formCertifies.setY(200);
        formCertifies.setImageLayout(imageLayout);
        
        worksheetAndForm.setForm(formCertifies);
        worksheetAndForm.setWorksheet(worksheet);
        
        
        mockMvc.perform(MockMvcRequestBuilders.multipart("/certificates/downloadimages")
        		.file(file)
                .flashAttr("worksheetAndForm", worksheetAndForm))
                .andExpect(MockMvcResultMatchers.status().is(302));
    }
    
	
}
