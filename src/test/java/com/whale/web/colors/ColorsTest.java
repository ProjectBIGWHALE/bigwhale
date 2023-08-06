package com.whale.web.colors;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.whale.web.colors.model.FormColors;
import com.whale.web.colors.model.FormPalette;
import org.springframework.web.util.NestedServletException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest
public class ColorsTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void shouldReturnTheHTMLFormForChangeColorsApp() throws Exception {
		
		URI uri = new URI("/colors/transparentimage");
		mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(
				MockMvcResultMatchers.status().is(200));
		
	}
	
	@Test
	public void shouldReturnTheHTMLFormForPaletteColorsApp() throws Exception {
		
		URI uri = new URI("/colors/colorspalette");
		mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(
				MockMvcResultMatchers.status().is(200));
		
	}
	
    @Test
    public void shouldReturnAValidPNGProcessedImage() throws Exception {
        
    	// Criar a imagem em formato BufferedImage
    	BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    	Graphics2D graphics = image.createGraphics();
    	graphics.setColor(Color.WHITE);
    	graphics.fillRect(0, 0, 100, 100);
    	graphics.dispose();

    	// Converte a imagem em bytes
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	ImageIO.write(image, "png", baos);
    	byte[] imageBytes = baos.toByteArray();

        // Cria o objeto MockMultipartFile com os dados da imagem
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "white_image.png",
                MediaType.IMAGE_PNG_VALUE,
                imageBytes
        );

        // Define os parâmetros de formColors
        FormColors formColors = new FormColors();
        formColors.setColorForAlteration("#000000");
        formColors.setColorOfImage("#FFFFFF");
        formColors.setMargin(4);
        formColors.setImage(file);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/colors/processimage")
                .file(file)
                .param("colorForAlteration", formColors.getColorForAlteration())
                .param("colorOfImage", formColors.getColorOfImage())
                .param("margin", String.valueOf(formColors.getMargin())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Disposition", Matchers.containsString("attachment")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.IMAGE_PNG));
    }
    
    @Test
    public void shouldReturnARedirectionStatusCode302() throws Exception {
    	
    	byte[] imageBytes = null;
    	
    	// Cria o objeto MockMultipartFile com os dados da imagem
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "white_image.png",
                MediaType.IMAGE_PNG_VALUE,
                imageBytes
        );
    	
    	
        // Define os parâmetros de formColors
        FormColors formColors = new FormColors();
        formColors.setColorForAlteration("#000000");
        formColors.setColorOfImage("#FFFFFF");
        formColors.setMargin(4);
        formColors.setImage(null);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/colors/processimage")
                .file(file)
                .param("colorForAlteration", formColors.getColorForAlteration())
                .param("colorOfImage", formColors.getColorOfImage())
                .param("margin", String.valueOf(formColors.getMargin())))
                .andExpect(MockMvcResultMatchers.status().is(302));
    	
    }
    
    @Test
    public void shouldReturnAPaletteColorsPageView() throws Exception {
    	
    	FormPalette formPalette = new FormPalette();
    	
    	// Criar a imagem em formato BufferedImage
    	BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    	Graphics2D graphics = image.createGraphics();
    	graphics.setColor(Color.WHITE);
    	graphics.fillRect(0, 0, 100, 100);
    	graphics.dispose();

    	// Converte a imagem em bytes
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	ImageIO.write(image, "png", baos);
    	byte[] imageBytes = baos.toByteArray();

        // Cria o objeto MockMultipartFile com os dados da imagem
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "white_image.png",
                MediaType.IMAGE_PNG_VALUE,
                imageBytes
        );
    	
        formPalette.setImage(file);
        
        mockMvc.perform(MockMvcRequestBuilders.multipart("/colors/createpalette")
                .file(file)
                .flashAttr("form", formPalette))
                .andExpect(MockMvcResultMatchers.status().is(200));
    	
    }

	@Test
	public void shouldReturnHTMLformToConvertImageFormat() throws Exception {
		URI uri = new URI("/colors/imageconversion");
		mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(
				MockMvcResultMatchers.status().is(200));
	}

	@Test
	public void testToConvertAndDownloadImageSuccessfully() throws Exception {

		// Criar a imagem em formato BufferedImage
		BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, 100, 100);
		graphics.dispose();

		// Formatos para teste na entrada: (jpg, jpeg, bmp, gif, tif, tiff, png)
		String inputType = "jpeg";

		// Converte a imagem em bytes
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "jpeg", baos);
		byte[] imageBytes = baos.toByteArray();

		// Cria o objeto MockMultipartFile com os dados da imagem
		MockMultipartFile file = new MockMultipartFile(
				"image",
				"test-image." + inputType,
				"image/" + inputType,
				imageBytes
		);

		// Formatos para teste na saída: (jpg, jpeg, bmp, gif, tif, tiff, png)
		String outputType = "bmp";

		// Faz uma requisição post na uri passando a imagem e formato de saída da imagem e esperar status 200.
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/colors/convertformatimage")
						.file(file)
						.param("outputFormat", outputType))
						.andExpect(MockMvcResultMatchers.status().isOk())
						.andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();
		assertEquals("image/" + outputType, response.getContentType());
		assertEquals("attachment; filename=test-image." + outputType, response.getHeader("Content-Disposition"));

	}


	@Test
	public void testInvalidImageFormatForConversion() throws Exception {

		// Criar a imagem em formato BufferedImage
		BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, 100, 100);
		graphics.dispose();

		// Formatos válidos para teste na entrada: (jpg, jpeg, bmp, gif, tif, tiff, png)
		String inputType = "jpeg";

		// Converte a imagem em bytes
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "jpeg", baos);
		byte[] imageBytes = baos.toByteArray();

		// Cria o objeto MockMultipartFile com os dados da imagem
		MockMultipartFile file = new MockMultipartFile(
				"image",
				"test-image." + inputType,
				"image/" + inputType,
				imageBytes
		);

		//Formato inválido para saída
		String outputType = "teste";

		// Faz uma requisição post na uri passando a imagem e formato de saída da imagem e esperar a exceção.
		Throwable thrownException = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(MockMvcRequestBuilders.multipart("/colors/convertformatimage")
					.file(file)
					.param("outputFormat", outputType));
		});

		// Verifica se a exceção interna é a IllegalArgumentException
		assertTrue(thrownException.getCause() instanceof IllegalArgumentException);

		// Verifica se a mensagem da exceção interna está correta
		String expectedErrorMessage = "Conversão não realizada: o formato de saída especificado não é suportado.";
		assertEquals(expectedErrorMessage, thrownException.getCause().getMessage());
	}





}
