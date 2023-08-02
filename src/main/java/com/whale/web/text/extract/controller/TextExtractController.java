package com.whale.web.text.extract.controller;

import com.whale.web.text.extract.service.TextExtractService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(path = "/text-extract")
public class TextExtractController {

    private final TextExtractService textService;

    public TextExtractController(TextExtractService textService) {
        this.textService = textService;
    }

    @GetMapping
    public String hello(){
        return "text-extract";
    }

    @PostMapping
    public String extractFromImage(@RequestParam("file") MultipartFile fileModel, Model model){
        String extractedText = textService.extractTextFromImage(fileModel);
        model.addAttribute("extractedText", extractedText);
        return "text-extracted";
    }
}