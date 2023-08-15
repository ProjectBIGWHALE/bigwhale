package com.whale.web.documents;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import com.whale.web.documents.imageconverter.exception.InvalidUploadedFileException;
import com.whale.web.documents.imageconverter.exception.UnableToConvertImageToOutputFormatException;
import com.whale.web.documents.imageconverter.exception.UnableToReadImageFormatException;
import com.whale.web.documents.imageconverter.exception.UnexpectedFileFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.whale.web.documents.certificategenerator.model.CertificateGeneratorForm;
import com.whale.web.documents.certificategenerator.service.CreateCertificateService;
import com.whale.web.documents.certificategenerator.service.ProcessWorksheetService;
import com.whale.web.documents.compactconverter.model.CompactConverterForm;
import com.whale.web.documents.compactconverter.service.CompactConverterService;
import com.whale.web.documents.filecompressor.service.FileCompressorService;
import com.whale.web.documents.imageconverter.model.Format;
import com.whale.web.documents.imageconverter.model.ImageConverterForm;
import com.whale.web.documents.imageconverter.service.ImageConverterService;
import com.whale.web.documents.qrcodegenerator.model.QRCodeGeneratorForm;
import com.whale.web.documents.qrcodegenerator.service.QRCodeGeneratorService;
import com.whale.web.documents.textextract.service.TextExtractService;

@Controller
@RequestMapping("/documents")
public class DocumentsController {

    @Autowired
    CompactConverterForm compactConverterForm;

    @Autowired
    CompactConverterService compactConverterService;

    @Autowired
    TextExtractService textService;

    @Autowired
    FileCompressorService fileCompressorService;
    
    @Autowired
    ImageConverterForm imageConverterForm;
    
    @Autowired
    ImageConverterService imageConverterService;
    
    @Autowired
    QRCodeGeneratorForm qrCodeGeneratorForm;
    
    @Autowired
    QRCodeGeneratorService qrCodeGeneratorService;
    
    @Autowired
    CertificateGeneratorForm certificateGeneratorForm;
    
    @Autowired
    ProcessWorksheetService processWorksheetService;
    
    @Autowired
    CreateCertificateService createCertificateService;

    @RequestMapping(value="/compactconverter", method=RequestMethod.GET)
    public String compactConverter(Model model) {

        model.addAttribute("form", compactConverterForm);
        return "compactconverter";

    }

    @PostMapping("/compactconverter")
    public String compactConverter(CompactConverterForm form, HttpServletResponse response) throws IOException{

        try {
            byte[] file = compactConverterService.converterFile(form);
            String format = form.getAction().toString();

            response.setHeader("Content-Disposition", "attachment; filename=file" + format);

            // Defines the content type and size of the response
            response.setContentType("application/octet-stream");
            response.setContentLength(file.length);

            // Set headers to allow the image to be downloaded
            response.setHeader("Cache-Control", "no-cache");

            try (OutputStream outputStream = response.getOutputStream()) {
                outputStream.write(file);
                outputStream.flush();
            }catch(Exception e) {
                e.printStackTrace();
            }
        } catch(Exception e) {
            return "redirect:/documents/compactconverter";
        }

        return null;
    }

    @GetMapping("/textextract")
    public String textExtract(){
    	try {
    		return "textextract";
    	} catch(Exception e) {
    		return "redirect:/";
    	}
    }

    @PostMapping("/textextracted")
    public String extractFromImage(@RequestParam("file") MultipartFile fileModel, Model model){
    	try {
    		String extractedText = textService.extractTextFromImage(fileModel);
    		model.addAttribute("extractedText", extractedText);
    		return "textextracted";
    	}catch(Exception e) {
    		return "redirect:/";
    	}
    }

    @GetMapping("/filecompressor")
    public String fileCompressor() {
        return "filecompressor";
    }

    @PostMapping("/filecompressor")
    public String fileCompressor(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        
            try {
                byte[] bytes = fileCompressorService.compressFile(file);
                response.setHeader("Content-Disposition", "attachment; filename="+file.getOriginalFilename()+".zip");
                response.setContentType("application/octet-stream");
                response.setHeader("Cache-Control", "no-cache");
                
                try (OutputStream outputStream = response.getOutputStream()) {
	                OutputStream os = response.getOutputStream();
	                os.write(bytes);
	                os.flush();
                } catch(Exception e) {
                	e.printStackTrace();
                }
            } catch (Exception e) {
            	return "redirect:/documents/filecompressor";
            }
        
        return null;
    }
    
	@GetMapping(value = "/imageconverter")
	public String imageConverter(Model model) {
		List<Format> list = Arrays.asList(	new Format(1L, "bmp"),
											new Format(2L, "jpg"),
											new Format(3L, "jpeg"),
											new Format(4L, "gif"),
											new Format(6L, "png"),
											new Format(6L, "tiff"),
											new Format(7L, "tif"));

		model.addAttribute("list", list);
		model.addAttribute("form", imageConverterForm);
		return "imageconverter";
	}


	@PostMapping("/imageconverter")
	public ResponseEntity<String> imageConverter(@ModelAttribute("form") ImageConverterForm imageConverterForm, HttpServletResponse response) {
		try {
			byte[] formattedImage = imageConverterService.convertImageFormat(imageConverterForm.getOutputFormat(), imageConverterForm.getImage());

			String originalFileNameWithoutExtension = StringUtils.stripFilenameExtension(Objects.requireNonNull(imageConverterForm.getImage().getOriginalFilename()));
			String convertedFileName = originalFileNameWithoutExtension + "." + imageConverterForm.getOutputFormat().toLowerCase();

			response.setContentType("image/" + imageConverterForm.getOutputFormat().toLowerCase());
			response.setHeader("Content-Disposition", "attachment; filename=" + convertedFileName);
			response.setHeader("Cache-Control", "no-cache");

			try (OutputStream os = response.getOutputStream()) {
				os.write(formattedImage);
				os.flush();
			}

			return ResponseEntity.ok().build();

		} catch (UnexpectedFileFormatException | InvalidUploadedFileException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());

		} catch (UnableToConvertImageToOutputFormatException | UnableToReadImageFormatException ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());

		} catch (IOException ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Um erro ocorreu:" + Arrays.toString(ex.getStackTrace()));
		}
	}
	
	@RequestMapping(value="/qrcodegenerator", method=RequestMethod.GET)
	public String qrCodeGenerator(Model model) throws IOException {
		
		model.addAttribute("form", qrCodeGeneratorForm);
		return "qrcodegenerator";
		
	}
	

	@PostMapping("/qrcodegenerator")
	public String qrCodeGenerator(QRCodeGeneratorForm qrCodeGeneratorForm, HttpServletResponse response) throws IOException{
		
		
		try {
			byte[] processedImage = qrCodeGeneratorService.generateQRCode(qrCodeGeneratorForm.getLink());
			
			// Define o tipo de conteúdo e o tamanho da resposta
	        response.setContentType("image/png");
	        response.setHeader("Content-Disposition", "attachment; filename=\"ModifiedImage.png\"");
	        response.setHeader("Cache-Control", "no-cache");

	        // Copia os bytes do arquivo para o OutputStream
	        try (OutputStream os = response.getOutputStream()) {
	            os.write(processedImage);
	            os.flush();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		} catch(Exception e) {
			return "redirect:/documents/qrcodegenerator";
		}
		
		return null;
	}
	
	@RequestMapping(value="/certificategenerator", method=RequestMethod.GET)
	public String certificateGenerator(Model model) {
		
		model.addAttribute("certificateGeneratorForm", certificateGeneratorForm);
		return "certificategenerator";
		
	}
	
	@RequestMapping(value = "/certificategenerator", method = RequestMethod.POST)
	public String certificateGenerator(CertificateGeneratorForm certificateGeneratorForm, HttpServletResponse response) throws Exception {
		try {
		    List<String> names = processWorksheetService.savingNamesInAList(certificateGeneratorForm.getWorksheet().getWorksheet(), certificateGeneratorForm.getCertificate());
		    byte[] zipFile = createCertificateService.createCertificates(certificateGeneratorForm.getCertificate(), names);
	
		    
		        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
		        response.setHeader("Content-Disposition", "attachment; filename=\"certificates.zip\"");
	
		        try (OutputStream outputStream = response.getOutputStream()) {
			        outputStream.write(zipFile);
			        outputStream.flush();
			    }catch(Exception e) {
					throw new RuntimeException("Error generating encrypted file", e);
				}
	    } catch (Exception e) {
	    	return "redirect:/documents/certificategenerator";
	    }
	    
	    return null;
	}

}
