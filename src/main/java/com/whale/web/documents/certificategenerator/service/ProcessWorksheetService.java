package com.whale.web.documents.certificategenerator.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.whale.web.documents.certificategenerator.model.Certificate;

@Service
public class ProcessWorksheetService {
	
	public List<String> savingNamesInAList(MultipartFile worksheet, Certificate certificate) throws Exception {

		if (worksheet.isEmpty()) {
			throw new IOException();
		}

		List<String> names = new ArrayList<>();

		try (CSVReader reader = new CSVReader(
				new InputStreamReader(worksheet.getInputStream(), StandardCharsets.UTF_8))) {
			List<String[]> lines = reader.readAll();
			for (String[] line : lines) {
				if (line.length > 0) {
					names.add(line[0]);
				}
			}
		}

		return names;

	}
	
}
