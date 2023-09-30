package com.whale.web.home;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest
class HomeControllerTest {
	
	@Autowired
	private MockMvc mockMvc;


	@ParameterizedTest
    @CsvSource({
        "/",
        "/help",
        "/supportus"        
    })
    void testWithDifferentURIs(String uri) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(
               MockMvcResultMatchers.status().is(200));
    }
	
}
