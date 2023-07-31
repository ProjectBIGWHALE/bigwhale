package com.whale.web.text.extract.controller;

import com.whale.web.text.extract.service.TextExtractService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "text-extract")
public class TextExtractController {

    private final TextExtractService textService;

    public TextExtractController(TextExtractService textService) {
        this.textService = textService;
    }

    @PostMapping
    public String extractFromImage(@RequestParam MultipartFile file){
        return textService.extractTextFromImage(file);
    }
}