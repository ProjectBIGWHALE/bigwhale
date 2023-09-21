package com.whale.web.documents;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.whale.web.documents.certificategenerator.model.CertificateGeneratorForm;
import com.whale.web.documents.certificategenerator.service.CreateCertificateService;
import com.whale.web.documents.certificategenerator.service.ProcessWorksheetService;
import com.whale.web.documents.compactconverter.model.CompactConverterForm;
import com.whale.web.documents.compactconverter.service.CompactConverterService;
import com.whale.web.documents.filecompressor.FileCompressorService;
import com.whale.web.documents.imageconverter.model.ImageFormatsForm;
import com.whale.web.documents.imageconverter.model.ImageConversionForm;
import com.whale.web.documents.imageconverter.service.ImageConverterService;
import com.whale.web.documents.qrcodegenerator.model.QRCodeGeneratorForm;
import com.whale.web.documents.qrcodegenerator.service.QRCodeGeneratorService;
import com.whale.web.documents.textextract.TextExtractService;

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
            byte[] bytes = compactConverterService.converterFile(form);
            String format = form.getAction();

            response.setHeader("Content-Disposition", "attachment; filename=file" + format);
            response.setContentType("application/octet-stream");
            response.setContentLength(bytes.length);
            response.setHeader("Cache-Control", "no-cache");

            OutputStream outputStream = response.getOutputStream();
            outputStream.write(bytes);
            outputStream.flush();
     
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

	            OutputStream outputStream = response.getOutputStream();
				outputStream.write(bytes);
				outputStream.flush();
				
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
			byte[] bytes = imageConverterService.convertImageFormat(imageConversionForm.getOutputFormat(), imageConversionForm.getImage());

			String originalFileNameWithoutExtension = StringUtils.stripFilenameExtension(Objects.requireNonNull(imageConversionForm.getImage().getOriginalFilename()));
			String convertedFileName = originalFileNameWithoutExtension + "." + imageConversionForm.getOutputFormat().toLowerCase();

			response.setContentType("image/" + imageConversionForm.getOutputFormat().toLowerCase());
			response.setHeader("Content-Disposition", "attachment; filename=" + convertedFileName);
			response.setHeader("Cache-Control", "no-cache");

			OutputStream outputStream = response.getOutputStream();
            outputStream.write(bytes);
            outputStream.flush();

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
			byte[] bytes;
			switch (qrCodeGeneratorForm.getDataType()) {
				case "link" -> bytes = qrCodeGeneratorService
						.generateQRCode(qrCodeGeneratorForm.getLink());
				case "whatsapp" -> bytes = qrCodeGeneratorService
						.generateWhatsAppLinkQRCode(qrCodeGeneratorForm.getPhoneNumber(), qrCodeGeneratorForm.getText());
				case "email" -> bytes = qrCodeGeneratorService
						.generateEmailLinkQRCode(qrCodeGeneratorForm.getEmail(), qrCodeGeneratorForm.getTextEmail(), qrCodeGeneratorForm.getTitleEmail());
				default -> {
					return "redirect:/documents/qrcodegenerator";
				}
			}

	        response.setContentType("image/png");
	        response.setHeader("Content-Disposition", "attachment; filename=\"QRCode.png\"");
	        response.setHeader("Cache-Control", "no-cache");

	        OutputStream outputStream = response.getOutputStream();
            outputStream.write(bytes);
            outputStream.flush();

		} catch(Exception e) {
			return "redirect:/documents/qrcodegenerator";
		}
		return null;
	}

	
	@GetMapping("/certificategenerator")
	public String certificateGenerator(Model model) {
		
		model.addAttribute("certificateGeneratorForm", certificateGeneratorForm);
		return "certificategenerator";
		
	}

	@PostMapping(value = "/certificategenerator")
	public String certificateGenerator(CertificateGeneratorForm certificateGeneratorForm, HttpServletResponse response) throws Exception {
		try {
		    List<String> names = processWorksheetService.savingNamesInAList(certificateGeneratorForm.getWorksheet().getWorksheet());
		    byte[] bytes = createCertificateService.createCertificates(certificateGeneratorForm.getCertificate(), names);

		    response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
		    response.setHeader("Content-Disposition", "attachment; filename=\"certificates.zip\"");
	
		    OutputStream outputStream = response.getOutputStream();
            outputStream.write(bytes);
            outputStream.flush();

	    } catch (Exception e) {
			return "redirect:/documents/certificategenerator";
	    }

	    return null;
	}

}
