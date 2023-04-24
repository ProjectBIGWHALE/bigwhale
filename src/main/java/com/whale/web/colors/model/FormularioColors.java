package com.whale.web.colors.model;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FormularioColors {
	
	MultipartFile imagem;
	String corDaImagem;
	String corParaAlteracao;
	Integer margem;
	
	
	public MultipartFile getImagem() {
		return imagem;
	}
	public void setImagem(MultipartFile imagem) {
		this.imagem = imagem;
	}
	public String getCorDaImagem() {
		return corDaImagem;
	}
	public void setCorDaImagem(String corDaImagem) {
		this.corDaImagem = corDaImagem;
	}
	public String getCorParaAlteracao() {
		return corParaAlteracao;
	}
	public void setCorParaAlteracao(String corParaAlteracao) {
		this.corParaAlteracao = corParaAlteracao;
	}
	public Integer getMargem() {
		return margem;
	}
	public void setMargem(Integer margem) {
		this.margem = margem;
	}
	
}
