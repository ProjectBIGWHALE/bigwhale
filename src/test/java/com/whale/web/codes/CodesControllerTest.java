package com.whale.web.codes;

import java.net.URI;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.whale.web.codes.model.FormCodes;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest
public class CodesControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	FormCodes formCodes = new FormCodes();
	
	@Test
	public void shouldReturnTheHTMLForm() throws Exception {
		
		URI uri = new URI("/codes/qrcode");
		mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(
				MockMvcResultMatchers.status().is(200));
		
	}
	
	@Test
    public void shouldReturnAValidQRCode() throws Exception {
        URI uri = new URI("/codes/generateqrcode");
        
        String link = "https://google.com.br";
        formCodes.setLink(link);
        
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
        		.param("link", formCodes.getLink()))
        		.andExpect(MockMvcResultMatchers.status().is(200))
		        .andExpect(MockMvcResultMatchers.header().string("Content-Disposition", Matchers.containsString("attachment")))
		        .andExpect(content().contentType(MediaType.IMAGE_PNG));
	}
	
	@Test
    public void shouldReturnAPageRedirectionStatusCode302() throws Exception {
        URI uri = new URI("/codes/generateqrcode");
        
        String link = "";
        formCodes.setLink(link);
        
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
        		.param("link", formCodes.getLink()))
        		.andExpect(MockMvcResultMatchers.status().is(302));
	}
	
	
}
