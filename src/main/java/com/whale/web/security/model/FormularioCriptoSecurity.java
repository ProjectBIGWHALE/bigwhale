package com.whale.web.security.model;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FormularioCriptoSecurity {
	
	private MultipartFile arquivo;
	private String chave;
	private Boolean acao;
	
	public MultipartFile getArquivo() {
		return arquivo;
	}
	public void setArquivo(MultipartFile arquivo) {
		this.arquivo = arquivo;
	}
	public String getChave() {
		return chave;
	}
	public void setChave(String chave) {
		this.chave = chave;
	}
	public Boolean getAcao() {
		return acao;
	}
	public void setAcao(Boolean acao) {
		this.acao = acao;
	}
	
}
