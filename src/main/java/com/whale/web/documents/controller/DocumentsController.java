package com.whale.web.documents.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.whale.web.documents.model.FormDocumentsConverter;
import com.whale.web.documents.service.ConverterService;

@Controller
@RequestMapping("/documents")
public class DocumentsController {

    @Autowired
    FormDocumentsConverter form;

    @Autowired
    ConverterService converterService;

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

}
