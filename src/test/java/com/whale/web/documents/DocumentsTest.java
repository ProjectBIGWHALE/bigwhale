package com.whale.web.documents;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import com.whale.web.documents.imageconverter.exception.InvalidUploadedFileException;
import com.whale.web.documents.imageconverter.exception.UnableToConvertImageToOutputFormatException;
import com.whale.web.documents.imageconverter.exception.UnexpectedFileFormatException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.platform.commons.function.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.NestedServletException;

import com.whale.web.documents.certificategenerator.model.Certificate;
import com.whale.web.documents.certificategenerator.model.CertificateGeneratorForm;
import com.whale.web.documents.certificategenerator.model.Worksheet;
import com.whale.web.documents.filecompressor.service.FileCompressorService;
import com.whale.web.documents.qrcodegenerator.model.QRCodeGeneratorForm;
import com.whale.web.documents.textextract.service.TextExtractService;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest
public class DocumentsTest {
	
	@Autowired
	private MockMvc mockMvc;

    @MockBean
    private TextExtractService textExtractService;

    @MockBean
    private FileCompressorService compressorService;

	@Test
	public void shouldReturnTheHTMLCompactConverterForm() throws Exception {
		
		URI uri = new URI("/documents/compactconverter");
		mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(
				status().is(200));
		
	}
	
    public MockMultipartFile createTestZipFile() throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zipOut = new ZipOutputStream(baos)) {

            ZipEntry entry = new ZipEntry("test.txt");
            zipOut.putNextEntry(entry);

            byte[] fileContent = "This is a test file content.".getBytes();
            zipOut.write(fileContent, 0, fileContent.length);

            zipOut.closeEntry();

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            return new MockMultipartFile("file", "test.zip", "application/zip", bais);
        }
    }
	
    @Test
    public void testConverter() throws Exception {
        // Prepare your test file data and other necessary objects
        MockMultipartFile testFile = this.createTestZipFile();
        String format = ".zip";

        // Perform the mock HTTP POST request
        mockMvc.perform(MockMvcRequestBuilders.multipart("/documents/compactconverter")
                .file(testFile)
                .param("action", format))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.header().exists("Content-Disposition"))
                .andExpect(MockMvcResultMatchers.header().string("Content-Disposition", "attachment; filename=file" + format))
                .andExpect(MockMvcResultMatchers.header().exists("Content-Type"))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", "application/octet-stream"))
                .andExpect(MockMvcResultMatchers.content().bytes(testFile.getBytes()));
    }

    @Test
    public void textExtractshouldReturnTheHTMLForm() throws Exception {

        URI uri = new URI("/documents/textextract");
        mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(
                status().is(200));
    }

    @Test
    public void textExtractedShouldReturnTheHTMLForm() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "test-image.png",
                "image/png",
                "Test image content".getBytes()
        );

        String extractedText = "Extracted text from image.";

        when(textExtractService.extractTextFromImage(any())).thenReturn(extractedText);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/documents/textextracted")
                        .file(multipartFile))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("textextracted"))
                .andExpect(MockMvcResultMatchers.model().attribute("extractedText", extractedText));

        verify(textExtractService, times(1)).extractTextFromImage(any());
    }

    @Test
    public void compressFilePageShouldReturnTheHTMLForm() throws Exception {

        URI uri = new URI("/documents/filecompressor");
        mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(
                status().is(200));
    }

    @Test
    public void compressFileShouldReturnTheFileZip() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "test-file.txt",
                "text/plain",
                "Test file content".getBytes()
        );

        when(compressorService.compressFile(any())).thenReturn(multipartFile.getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/documents/filecompressor")
                        .file(multipartFile))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/octet-stream"))
                .andExpect(MockMvcResultMatchers.header().string("Content-Disposition", "attachment; filename=test-file.txt.zip"));

        verify(compressorService, times(1)).compressFile(any());
    }
    
    @Test
	public void shouldReturnTheHTMLCertificateGeneratorForm() throws Exception {
		
		URI uri = new URI("/documents/certificategenerator/");
		mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(
				status().is(200));
		
	}
    
	@Test
    public void shouldReturnTheCertificatesStatusCode200() throws Exception {
        CertificateGeneratorForm certificateGeneratorForm = new CertificateGeneratorForm();
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
        Certificate certificate = new Certificate();
        certificate.setFontSize(20);
        certificate.setX(200);
        certificate.setY(200);
        certificate.setImageLayout(imageLayout);
        
        certificateGeneratorForm.setCertificate(certificate);
        certificateGeneratorForm.setWorksheet(worksheet);
        
        
        mockMvc.perform(MockMvcRequestBuilders.multipart("/documents/certificategenerator")
        		.file(file)
                .flashAttr("certificateGeneratorForm", certificateGeneratorForm))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Disposition", Matchers.containsString("attachment")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE));
    }
    
    @Test
    public void shouldReturnARedirectionStatusCode302() throws Exception {
        CertificateGeneratorForm certificateGeneratorForm = new CertificateGeneratorForm();
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
        Certificate certificate = new Certificate();
        certificate.setFontSize(20);
        certificate.setX(200);
        certificate.setY(200);
        certificate.setImageLayout(imageLayout);
        
        certificateGeneratorForm.setCertificate(certificate);
        certificateGeneratorForm.setWorksheet(worksheet);
        
        
        mockMvc.perform(MockMvcRequestBuilders.multipart("/documents/certificategenerator")
        		.file(file)
                .flashAttr("worksheetAndForm", certificateGeneratorForm))
                .andExpect(status().is(302));
    }
    
    @Test
	public void shouldReturnTheQRCodeGeneratorHTMLForm() throws Exception {
		
		URI uri = new URI("/documents/qrcodegenerator");
		mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(
				status().is(200));
		
	}
    
    @Test
    public void shouldReturnAValidQRCode() throws Exception {
        URI uri = new URI("/documents/qrcodegenerator");
        
        QRCodeGeneratorForm qrCodeGeneratorForm = new QRCodeGeneratorForm();
        
        String link = "https://google.com.br";
        qrCodeGeneratorForm.setLink(link);
        
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
        		.param("link", qrCodeGeneratorForm.getLink()))
        		.andExpect(status().is(200))
		        .andExpect(MockMvcResultMatchers.header().string("Content-Disposition", Matchers.containsString("attachment")))
		        .andExpect(content().contentType(MediaType.IMAGE_PNG));
	}
    
	@Test
    public void shouldReturnAPageRedirectionStatusCode302() throws Exception {
        URI uri = new URI("/documents/qrcodegenerator");
        
        QRCodeGeneratorForm qrCodeGeneratorForm = new QRCodeGeneratorForm();
        
        String link = "";
        qrCodeGeneratorForm.setLink(link);
        
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
        		.param("link", qrCodeGeneratorForm.getLink()))
        		.andExpect(status().is(302));
	}
	
	@Test
	public void shouldReturnImageConverterHTML() throws Exception {
		URI uri = new URI("/documents/imageconverter");
		mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(
				status().is(200));
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
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/documents/imageconverter")
						.file(file)
						.param("outputFormat", outputType))
						.andExpect(status().isOk())
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

		try {
			// Faz uma requisição post na uri passando a imagem e formato de saída da imagem
			mockMvc.perform(MockMvcRequestBuilders.multipart("/documents/imageconverter")
							.file(file)
							.param("outputFormat", outputType))
							.andExpect(status().isInternalServerError()); // Verifica o status HTTP 500

		} catch (NestedServletException ex) {
			// Verifica se a exceção interna é a UnableToConvertImageToOutputFormatException
			assertTrue(ex.getCause() instanceof UnableToConvertImageToOutputFormatException);

			// Verifica a mensagem da exceção interna
			String expectedErrorMessage = "Não foi possível converter uma imagem jpeg para o formato teste. "
											+ "Tente novamente ou escolha outro formato de saída. ";
			assertEquals(expectedErrorMessage, ex.getCause().getMessage());
		}
	}


	@Test
	public void testInvalidFileTypeForConversion() throws Exception {
		// Cria um arquivo de texto simulando um upload de um arquivo que não é uma imagem
		byte[] fileContent = "Este é um arquivo de texto".getBytes();
		MockMultipartFile file = new MockMultipartFile(
				"image", // Nome do parâmetro no método do controlador
				"test.txt",
				"text/plain",
				fileContent
		);

		// Formato de saída
		String outputFormat = "jpeg";

		try {
			mockMvc.perform(MockMvcRequestBuilders.multipart("/documents/imageconverter")
							.file(file)
							.param("outputFormat", outputFormat))
							.andExpect(status().isBadRequest());


		} catch (NestedServletException ex) {
			// Verifica se a exceção interna é a UnexpectedFileFormatException
			assertTrue(ex.getCause() instanceof UnexpectedFileFormatException);

			// Verifica a mensagem da exceção interna
			String expectedErrorMessage = "Tipo de arquivo inválido. Apenas imagens são permitidas.";
			assertEquals(expectedErrorMessage, ex.getCause().getMessage());
		}
	}


	@Test
	public void testConvertImageFormatWithNullImage() throws Exception {
		// Criar um InputStream vazio
		InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);

		// Criar um MockMultipartFile com um InputStream vazio
		MockMultipartFile imageFile = new MockMultipartFile(
				"image",
				"test-image.jpg",
				"image/jpeg",
				emptyInputStream
		);

		// Defina um formato de saída válido
		String outputType = "jpeg";

		try {
			mockMvc.perform(MockMvcRequestBuilders.multipart("/documents/imageconverter")
					.file(imageFile)
					.param("outputFormat", outputType))
					.andExpect(status().isBadRequest());;

		} catch (NestedServletException ex) {
			// Verifica se a exceção interna é a InvalidUploadedFileException
			assertTrue(ex.getCause() instanceof InvalidUploadedFileException);

			// Verifica se a mensagem da exceção interna está correta
			String expectedErrorMessage = "Não foi enviado um arquivo válido.";
			assertEquals(expectedErrorMessage, ex.getCause().getMessage());
		}

	}
    
}
