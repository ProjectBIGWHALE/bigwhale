package com.whale.web.documents;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.whale.web.documents.service.CompressorService;
import com.whale.web.documents.service.TextExtractService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest
public class DocumentsTest {
	
	@Autowired
	private MockMvc mockMvc;

    @MockBean
    private TextExtractService textExtractService;

    @MockBean
    private CompressorService compressorService;

	@Test
	public void shouldReturnTheHTMLForm() throws Exception {
		
		URI uri = new URI("/documents/fileconverter");
		mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(
				MockMvcResultMatchers.status().is(200));
		
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
        mockMvc.perform(MockMvcRequestBuilders.multipart("/documents/converter")
                .file(testFile)
                .param("action", format))
                .andExpect(MockMvcResultMatchers.status().isOk())
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
                MockMvcResultMatchers.status().is(200));
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
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("textextracted"))
                .andExpect(MockMvcResultMatchers.model().attribute("extractedText", extractedText));

        verify(textExtractService, times(1)).extractTextFromImage(any());
    }

    @Test
    public void compressFilePageShouldReturnTheHTMLForm() throws Exception {

        URI uri = new URI("/documents/compressor");
        mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(
                MockMvcResultMatchers.status().is(200));
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

        mockMvc.perform(MockMvcRequestBuilders.multipart("/documents/compressorprocessing")
                        .file(multipartFile))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/octet-stream"))
                .andExpect(MockMvcResultMatchers.header().string("Content-Disposition", "attachment; filename=test-file.txt.zip"));

        verify(compressorService, times(1)).compressFile(any());
    }
}
