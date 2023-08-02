package com.whale.web.documents.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import com.whale.web.documents.service.TextExtractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.whale.web.documents.model.FormDocumentsConverter;
import com.whale.web.documents.service.ConverterService;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/documents")
public class DocumentsController {

    @Autowired
    FormDocumentsConverter form;

    @Autowired
    ConverterService converterService;

    @Autowired
    TextExtractService textService;

    @RequestMapping(value="/fileconverter", method=RequestMethod.GET)
    public String fileConverter(Model model) {

        model.addAttribute("form", form);
        return "fileconverter";

    }

    @PostMapping("/converter")
    public String converter(FormDocumentsConverter form, HttpServletResponse response) throws IOException{

        try {
            byte[] file = converterService.converterFile(form);
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
            return "redirect:/documents/fileconverter";
        }

        return null;
    }

    @GetMapping("/textextract")
    public String textExtract(){
        return "textextract";
    }

    @PostMapping("/textextracted")
    public String extractFromImage(@RequestParam("file") MultipartFile fileModel, Model model){
        String extractedText = textService.extractTextFromImage(fileModel);
        model.addAttribute("extractedText", extractedText);
        return "textextracted";
    }

}
