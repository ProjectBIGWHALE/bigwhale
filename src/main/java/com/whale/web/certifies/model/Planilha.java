package com.whale.web.certifies.model;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class Planilha {
	
	private MultipartFile planilha;

	public MultipartFile getPlanilha() {
		return planilha;
	}

	public void setPlanilha(MultipartFile planilha) {
		this.planilha = planilha;
	}
	
}
