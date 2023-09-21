package com.whale.web.documents;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import java.net.URI;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import com.whale.web.documents.certificategenerator.model.enums.CertificateTypeEnum;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

import com.whale.web.documents.certificategenerator.model.Certificate;
import com.whale.web.documents.certificategenerator.model.CertificateGeneratorForm;
import com.whale.web.documents.certificategenerator.model.Worksheet;
import com.whale.web.documents.filecompressor.FileCompressorService;
import com.whale.web.documents.qrcodegenerator.model.QRCodeGeneratorForm;
import com.whale.web.documents.textextract.TextExtractService;

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


	@ParameterizedTest
    @CsvSource({
        "/documents/compactconverter",
        "/documents/textextract",
        "/documents/filecompressor",
        "/documents/qrcodegenerator",
        "/documents/certificategenerator/",
        "/documents/imageconverter"
    })
    void testWithDifferentURIs(String uri) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(uri))
               .andExpect(status().is(200));
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
    void shouldReturnTheCertificatesStatusCode200() throws Exception {
        CertificateGeneratorForm certificateGeneratorForm = new CertificateGeneratorForm();
        Worksheet worksheet = new Worksheet();

        String csvContent = "col1,col2,col3\nvalue1,value2,value3";

        MockMultipartFile file = new MockMultipartFile("file", "worksheet.csv", MediaType.TEXT_PLAIN_VALUE, csvContent.getBytes());

        worksheet.setWorksheet(file);
        Certificate certificate = new Certificate();
        certificate.setCertificateModelId(1L);
		certificate.setEventName("ABC dos DEVS");
		certificate.setCertificateTypeEnum(CertificateTypeEnum.COURCE);
		certificate.setEventLocale("São Paulo");
		certificate.setEventDate("2023-09-12");
		certificate.setEventWorkLoad("20");
		certificate.setSpeakerName("Ronnyscley");
		certificate.setSpeakerRole("CTO");

        certificateGeneratorForm.setCertificate(certificate);
        certificateGeneratorForm.setWorksheet(worksheet);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/documents/certificategenerator")
        		.file(file)
                .flashAttr("certificateGeneratorForm", certificateGeneratorForm))
                .andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
				.andExpect(MockMvcResultMatchers.header().string("Content-Disposition", Matchers.containsString("attachment")));
    }
    
    @Test
    void shouldReturnARedirectionStatusCode302() throws Exception {
        CertificateGeneratorForm certificateGeneratorForm = new CertificateGeneratorForm();
        Worksheet worksheet = new Worksheet();

        String csvContent = "col1,col2,col3\nvalue1,value2,value3";

        MockMultipartFile file = new MockMultipartFile("file", "worksheet.csv", MediaType.TEXT_PLAIN_VALUE, csvContent.getBytes());

        worksheet.setWorksheet(null);
        Certificate certificate = new Certificate();
		certificate.setCertificateModelId(1L);
		certificate.setCertificateTypeEnum(CertificateTypeEnum.COURCE);
		certificate.setEventLocale("São Paulo");
		certificate.setEventDate("20/10/2023");
		certificate.setEventWorkLoad("20");
		certificate.setSpeakerName("Ronnyscley");
		certificate.setSpeakerRole("CTO");

        certificateGeneratorForm.setCertificate(certificate);
        certificateGeneratorForm.setWorksheet(worksheet);


        mockMvc.perform(MockMvcRequestBuilders.multipart("/documents/certificategenerator")
        		.file(file)
                .flashAttr("worksheetAndForm", certificateGeneratorForm))
                .andExpect(status().is(302));
    }
    

    
    @Test
    void shouldReturnAValidQRCodeLink() throws Exception {
        URI uri = new URI("/documents/qrcodegenerator");
        
        QRCodeGeneratorForm qrCodeGeneratorForm = new QRCodeGeneratorForm();
        qrCodeGeneratorForm.setLink("https://google.com.br");
		qrCodeGeneratorForm.setDataType("link");

		mockMvc.perform(MockMvcRequestBuilders.post(uri)
						.param("link", qrCodeGeneratorForm.getLink())
						.param("dataType", qrCodeGeneratorForm.getDataType()))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.header().string("Content-Disposition", Matchers.containsString("attachment; filename=\"QRCode.png\"")))
				.andExpect(content().contentType(MediaType.IMAGE_PNG));
	}

	@Test
	void nullLinkThatReturnStatus302_QRCodeLink() throws Exception {
		URI uri = new URI("/documents/qrcodegenerator");

		QRCodeGeneratorForm qrCodeGeneratorForm = new QRCodeGeneratorForm();
		qrCodeGeneratorForm.setLink(null);
		qrCodeGeneratorForm.setDataType("link");

		mockMvc.perform(MockMvcRequestBuilders.post(uri)
						.param("link", qrCodeGeneratorForm.getLink())
						.param("dataType", qrCodeGeneratorForm.getDataType()))
				.andExpect(status().is(302));
	}

	@Test
	void shouldReturnAValidQRCodeEmail() throws Exception {
		URI uri = new URI("/documents/qrcodegenerator");

		QRCodeGeneratorForm qrCodeGeneratorForm = new QRCodeGeneratorForm();
		qrCodeGeneratorForm.setDataType("email");
		qrCodeGeneratorForm.setEmail("erasmo.ads.tech@gmail.com");
		qrCodeGeneratorForm.setTitleEmail("Teste");
		qrCodeGeneratorForm.setTextEmail("Este é um teste de geração QRCode para envio de email");

		mockMvc.perform(MockMvcRequestBuilders.post(uri)
						.param("dataType", qrCodeGeneratorForm.getDataType())
						.param("email", qrCodeGeneratorForm.getEmail())
						.param("titleEmail", qrCodeGeneratorForm.getTitleEmail())
						.param("textEmail", qrCodeGeneratorForm.getTextEmail()))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.header().string("Content-Disposition", Matchers.containsString("attachment; filename=\"QRCode.png\"")))
				.andExpect(content().contentType(MediaType.IMAGE_PNG));
	}


	@Test
	void nullEmailThatReturnStatus302_QRCodeEmail() throws Exception {
		URI uri = new URI("/documents/qrcodegenerator");

		QRCodeGeneratorForm qrCodeGeneratorForm = new QRCodeGeneratorForm();
		qrCodeGeneratorForm.setDataType("email");
		qrCodeGeneratorForm.setEmail(null);
		qrCodeGeneratorForm.setTitleEmail(null);
		qrCodeGeneratorForm.setTextEmail(null);

		mockMvc.perform(MockMvcRequestBuilders.post(uri)
						.param("dataType", qrCodeGeneratorForm.getDataType())
						.param("email", qrCodeGeneratorForm.getEmail())
						.param("titleEmail", qrCodeGeneratorForm.getTitleEmail())
						.param("textEmail", qrCodeGeneratorForm.getTextEmail()))
				.andExpect(status().is(302));
	}


	@Test
	void shouldReturnAValidQRCodeWhatsapp() throws Exception {
		URI uri = new URI("/documents/qrcodegenerator");

		QRCodeGeneratorForm qrCodeGeneratorForm = new QRCodeGeneratorForm();
		qrCodeGeneratorForm.setDataType("whatsapp");
		qrCodeGeneratorForm.setPhoneNumber("5527997512017");
		qrCodeGeneratorForm.setText("Teste de QRCODE no envio de mensagem ao whatsapp");

		mockMvc.perform(MockMvcRequestBuilders.post(uri)
						.param("dataType", qrCodeGeneratorForm.getDataType())
						.param("phoneNumber", qrCodeGeneratorForm.getPhoneNumber())
						.param("text", qrCodeGeneratorForm.getText()))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.header().string("Content-Disposition", Matchers.containsString("attachment; filename=\"QRCode.png\"")))
				.andExpect(content().contentType(MediaType.IMAGE_PNG));
	}


	@Test
	void nullWhatsappThatReturnStatus302_QRCodeWhatsapp() throws Exception {
		URI uri = new URI("/documents/qrcodegenerator");

		QRCodeGeneratorForm qrCodeGeneratorForm = new QRCodeGeneratorForm();
		qrCodeGeneratorForm.setDataType("whatsapp");
		qrCodeGeneratorForm.setPhoneNumber(null);
		qrCodeGeneratorForm.setText(null);

		mockMvc.perform(MockMvcRequestBuilders.post(uri)
						.param("dataType", qrCodeGeneratorForm.getDataType())
						.param("phoneNumber", qrCodeGeneratorForm.getPhoneNumber())
						.param("text", qrCodeGeneratorForm.getText()))
				.andExpect(status().is(302));
	}


	@Test
	void nullDataTypeThatReturnStatus302_QRCode() throws Exception {
		URI uri = new URI("/documents/qrcodegenerator");

		QRCodeGeneratorForm qrCodeGeneratorForm = new QRCodeGeneratorForm();
		qrCodeGeneratorForm.setDataType(null);
		qrCodeGeneratorForm.setPhoneNumber("5527997512017");
		qrCodeGeneratorForm.setText("Teste de QRCODE no envio de mensagem ao whatsapp");

		mockMvc.perform(MockMvcRequestBuilders.post(uri)
						.param("dataType", qrCodeGeneratorForm.getDataType())
						.param("phoneNumber", qrCodeGeneratorForm.getPhoneNumber())
						.param("text", qrCodeGeneratorForm.getText()))
				.andExpect(status().is(302));
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
						.andExpect(MockMvcResultMatchers.status().isFound());
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
						.andExpect(MockMvcResultMatchers.status().isFound());
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
						.andExpect(MockMvcResultMatchers.status().isFound());

	}


    
}
