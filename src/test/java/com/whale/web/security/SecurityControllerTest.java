package com.whale.web.security;

import java.net.URI;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.whale.web.security.controller.SecurityController;
import com.whale.web.security.model.CryptographyFormSecurity;
import com.whale.web.security.service.EncryptService;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest
public class SecurityControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private CryptographyFormSecurity criptographyFormSecurity = new CryptographyFormSecurity();
	
	@Autowired
	EncryptService encryptService;
	
	@Test
	public void shouldReturnTheHTMLForm() throws Exception {
		
		URI uri = new URI("/security/encrypt");
		mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(
				MockMvcResultMatchers.status().is(200));
		
	}
	
    public SecurityControllerTest() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new SecurityController()).build();
    }

    @Test
    public void shouldReturnEncryptedFile() throws Exception {
        URI uri = new URI("/security/encryptfile");

        MockMultipartFile file = new MockMultipartFile("file", "test.txt",
                MediaType.TEXT_PLAIN_VALUE, "Test content".getBytes());

        criptographyFormSecurity.setKey("TEST");
        criptographyFormSecurity.setFile(file);
        criptographyFormSecurity.setAction(true);

        mockMvc.perform(MockMvcRequestBuilders.multipart(uri)
                .file(file)
                .param("key", criptographyFormSecurity.getKey())
                .param("action", String.valueOf(criptographyFormSecurity.getAction())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Disposition", Matchers.containsString("attachment")))
                .andExpect(MockMvcResultMatchers.header().string("Content-Disposition", Matchers.containsString("encryptedFile")))
                .andExpect(result -> {
                    byte[] encryptedContent = result.getResponse().getContentAsByteArray();
                    byte[] expectedEncryptedContent = encryptService.encryptFile(criptographyFormSecurity);
                    Assertions.assertArrayEquals(expectedEncryptedContent, encryptedContent);
                });
    }
    
    @Test
    public void shouldReturnDecryptedFile() throws Exception {
        URI uri = new URI("/security/encryptfile");

        // Criação do arquivo simulado
        MockMultipartFile file = new MockMultipartFile("file", "test.txt",
                MediaType.TEXT_PLAIN_VALUE, "Test content".getBytes());
  
        criptographyFormSecurity.setKey("TEST");
        criptographyFormSecurity.setFile(file); 
        criptographyFormSecurity.setAction(true);
        
        byte[] encryptedContent = encryptService.encryptFile(criptographyFormSecurity);
        MockMultipartFile encryptedFile = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, encryptedContent);
        
        criptographyFormSecurity.setFile(encryptedFile);
        criptographyFormSecurity.setAction(false);
        
        mockMvc.perform(MockMvcRequestBuilders.multipart(uri)
                .file(encryptedFile)
                .param("key", criptographyFormSecurity.getKey())
                .param("action", String.valueOf(criptographyFormSecurity.getAction())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Disposition", Matchers.containsString("attachment")))
                .andExpect(MockMvcResultMatchers.header().string("Content-Disposition", Matchers.containsString("decryptedFile")))
                .andExpect(result -> {
                    byte[] decryptedContent = result.getResponse().getContentAsByteArray();
                    byte[] expectedDecryptedContent = encryptService.decryptFile(criptographyFormSecurity);
                    Assertions.assertArrayEquals(expectedDecryptedContent, decryptedContent);
                });
        
    }

    @Test
    public void shouldReturnRedirectStatusPage302() throws Exception {
        URI uri = new URI("/security/encryptfile");

        // Criação do arquivo simulado
        MockMultipartFile file = new MockMultipartFile("file", "test.txt",
                MediaType.TEXT_PLAIN_VALUE, "Test content".getBytes());
  
        criptographyFormSecurity.setKey("CORRECT KEY");
        criptographyFormSecurity.setFile(file); 
        criptographyFormSecurity.setAction(true);
        
        byte[] encryptedContent = encryptService.encryptFile(criptographyFormSecurity);
        MockMultipartFile encryptedFile = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, encryptedContent);
        
        criptographyFormSecurity.setFile(encryptedFile);
        criptographyFormSecurity.setAction(false);
        criptographyFormSecurity.setKey("WRONG KEY");
        
        mockMvc.perform(MockMvcRequestBuilders.multipart(uri)
                .file(encryptedFile)
                .param("key", criptographyFormSecurity.getKey())
                .param("action", String.valueOf(criptographyFormSecurity.getAction())))
                .andExpect(MockMvcResultMatchers.status().is(302));
        
    }
    

	
}
