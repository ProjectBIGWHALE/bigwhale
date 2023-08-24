package com.whale.web.documents;

import static org.junit.jupiter.api.Assertions.assertEquals;
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


	public MockMultipartFile createTestImage(String inputFormat) throws IOException{
		BufferedImage bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    	Graphics2D graphics = bufferedImage.createGraphics();
    	graphics.setColor(Color.WHITE);
    	graphics.fillRect(0, 0, 100, 100);
    	graphics.dispose();

    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	ImageIO.write(bufferedImage, inputFormat, baos);
    	baos.toByteArray();

		MockMultipartFile image = new MockMultipartFile(
				"image",
				"test-image." + inputFormat,
				"image/" + inputFormat,
				baos.toByteArray()
		);
		return image;
	}


	@Test
	void shouldReturnTheHTMLCompactConverterForm() throws Exception {
		
		URI uri = new URI("/documents/compactconverter");
		mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(
				status().is(200));
		
	}
	

	
    @Test
    void testConverter() throws Exception {

        MockMultipartFile testFile = this.createTestZipFile();
		String format = ".zip";

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
    void textExtractshouldReturnTheHTMLForm() throws Exception {

        URI uri = new URI("/documents/textextract");
        mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(
                status().is(200));
    }

    @Test
    void textExtractedShouldReturnTheHTMLForm() throws Exception {
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
    void compressFilePageShouldReturnTheHTMLForm() throws Exception {

        URI uri = new URI("/documents/filecompressor");
        mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(
                status().is(200));
    }

    @Test
    void compressFileShouldReturnTheFileZip() throws Exception {
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
	void shouldReturnTheHTMLCertificateGeneratorForm() throws Exception {
		
		URI uri = new URI("/documents/certificategenerator/");
		mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(
				status().is(200));
		
	}
    
	@Test
    void shouldReturnTheCertificatesStatusCode200() throws Exception {
        CertificateGeneratorForm certificateGeneratorForm = new CertificateGeneratorForm();
        Worksheet worksheet = new Worksheet();        
        
        String csvContent = "col1,col2,col3\nvalue1,value2,value3";
        
        MockMultipartFile file = new MockMultipartFile("file", "worksheet.csv", MediaType.TEXT_PLAIN_VALUE, csvContent.getBytes());

		MockMultipartFile imageLayout = this.createTestImage("png");
        

        
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
    void shouldReturnARedirectionStatusCode302() throws Exception {
        CertificateGeneratorForm certificateGeneratorForm = new CertificateGeneratorForm();
        Worksheet worksheet = new Worksheet();

        String csvContent = "col1,col2,col3\nvalue1,value2,value3";
        
        MockMultipartFile file = new MockMultipartFile("file", "worksheet.csv", MediaType.TEXT_PLAIN_VALUE, csvContent.getBytes());

		MockMultipartFile imageLayout = this.createTestImage("png");
        
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
	void shouldReturnTheQRCodeGeneratorHTMLForm() throws Exception {
		
		URI uri = new URI("/documents/qrcodegenerator");
		mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(
				status().is(200));
		
	}
    
    @Test
    void shouldReturnAValidQRCode() throws Exception {
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
    void shouldReturnAPageRedirectionStatusCode302() throws Exception {
        URI uri = new URI("/documents/qrcodegenerator");
        
        QRCodeGeneratorForm qrCodeGeneratorForm = new QRCodeGeneratorForm();
        
        String link = "";
        qrCodeGeneratorForm.setLink(link);
        
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
        		.param("link", qrCodeGeneratorForm.getLink()))
        		.andExpect(status().is(302));
	}
	
	@Test
	void shouldReturnImageConverterHTML() throws Exception {
		URI uri = new URI("/documents/imageconverter");
		mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(
				status().is(200));
	}

	@Test
	void testToConvertAndDownloadImageSuccessfully() throws Exception {

		String inputType = "jpeg";
		MockMultipartFile file = this.createTestImage(inputType);

		String outputType = "bmp";
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
	void testUnableToConvertImageToOutputFormatException() throws Exception {

		MockMultipartFile imageFile = createTestImage("png");
		String invalidOutputFormat = "teste";
		mockMvc.perform(MockMvcRequestBuilders.multipart("/documents/imageconverter")
						.file(imageFile)
						.param("outputFormat", invalidOutputFormat))
						.andExpect(MockMvcResultMatchers.status().isInternalServerError())
						.andExpect(MockMvcResultMatchers.content()
						.string("Could not convert an image png for format "
						+ invalidOutputFormat
						+ ". Please try again or choose another output format."));
	}


	@Test
	void testUnexpectedFileFormatException() throws Exception {
		// Preparação
		MockMultipartFile file = new MockMultipartFile(
				"image",
				"test.txt",
				"text/txt",
				"Este é um arquivo de texto".getBytes()
		);

		mockMvc.perform(MockMvcRequestBuilders.multipart("/documents/imageconverter")
						.file(file)
						.param("outputFormat", "png"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.content().string("Only images are allowed."));
	}

	@Test
	void testConvertImageFormatWithNullImage() throws Exception {
		MockMultipartFile file = new MockMultipartFile(
				"image",
				"test.png",
				null,
				new byte[0]
		);

		mockMvc.perform(MockMvcRequestBuilders.multipart("/documents/imageconverter")
						.file(file)
						.param("outputFormat", "png"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.content().string("Uploaded image file is null, empty or image format is not supported"));

	}


    
}
