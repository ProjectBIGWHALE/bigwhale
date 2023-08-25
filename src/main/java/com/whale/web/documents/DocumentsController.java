package com.whale.web.documents;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import com.whale.web.documents.imageconverter.exception.*;
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
import com.whale.web.documents.imageconverter.model.ImageFormatsForm;
import com.whale.web.documents.imageconverter.model.ImageConversionForm;
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
	ImageConversionForm imageConversionForm;
    
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

    @GetMapping(value="/compactconverter")
    public String compactConverter(Model model) {

        model.addAttribute("form", compactConverterForm);
        return "compactconverter";

    }

    @PostMapping("/compactconverter")
    public String compactConverter(CompactConverterForm form, HttpServletResponse response) throws IOException{

        try {
            byte[] file = compactConverterService.converterFile(form);
            String format = form.getAction();

            response.setHeader("Content-Disposition", "attachment; filename=file" + format);
            response.setContentType("application/octet-stream");
            response.setContentLength(file.length);
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

	            try (OutputStream os = response.getOutputStream()) {
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
		List<ImageFormatsForm> list = Arrays.asList(	new ImageFormatsForm(1L, "bmp"),
											new ImageFormatsForm(2L, "jpg"),
											new ImageFormatsForm(3L, "jpeg"),
											new ImageFormatsForm(4L, "gif"));

		model.addAttribute("list", list);
		model.addAttribute("form", imageConversionForm);
		return "imageconverter";
	}


	@PostMapping("/imageconverter")
	public String imageConverter(@ModelAttribute("form") ImageConversionForm imageConversionForm, HttpServletResponse response) {
		try {
			byte[] formattedImage = imageConverterService.convertImageFormat(imageConversionForm.getOutputFormat(), imageConversionForm.getImage());

			String originalFileNameWithoutExtension = StringUtils.stripFilenameExtension(Objects.requireNonNull(imageConversionForm.getImage().getOriginalFilename()));
			String convertedFileName = originalFileNameWithoutExtension + "." + imageConversionForm.getOutputFormat().toLowerCase();

			response.setContentType("image/" + imageConversionForm.getOutputFormat().toLowerCase());
			response.setHeader("Content-Disposition", "attachment; filename=" + convertedFileName);
			response.setHeader("Cache-Control", "no-cache");

			try (OutputStream os = response.getOutputStream()) {
				os.write(formattedImage);
				os.flush();
			}catch(Exception e) {
				e.printStackTrace();
			}

		} catch (Exception ex){
			return "redirect:/documents/imageconverter";
		}

		return null;
	}

	@GetMapping(value="/qrcodegenerator")
	public String qrCodeGenerator(Model model) {
		
		model.addAttribute("form", qrCodeGeneratorForm);
		return "qrcodegenerator";
		
	}
	

	@PostMapping("/qrcodegenerator")
	public String qrCodeGenerator(QRCodeGeneratorForm qrCodeGeneratorForm, HttpServletResponse response) throws IOException{
		
		try {
			byte[] processedImage = qrCodeGeneratorService.generateQRCode(qrCodeGeneratorForm.getLink());

	        response.setContentType("image/png");
	        response.setHeader("Content-Disposition", "attachment; filename=\"ModifiedImage.png\"");
	        response.setHeader("Cache-Control", "no-cache");

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
	
	@GetMapping(value="/certificategenerator")
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
