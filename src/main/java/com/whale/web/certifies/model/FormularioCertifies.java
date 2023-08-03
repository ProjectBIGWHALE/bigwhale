package com.whale.web.certifies.model;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FormularioCertifies {
	
	private MultipartFile imagemLayoult;
	private String nomeDaPessoa;
	private Integer x;
	private Integer y;
	private Integer tamanhoDaFonte;
	
	public MultipartFile getImagemLayoult() {
		return imagemLayoult;
	}
	public void setImagemLayoult(MultipartFile imagemLayoult) {
		this.imagemLayoult = imagemLayoult;
	}
	public String getNomeDaPessoa() {
		return nomeDaPessoa;
	}
	public void setNomeDaPessoa(String nomeDaPessoa) {
		this.nomeDaPessoa = nomeDaPessoa;
	}
	public Integer getX() {
		return x;
	}
	public void setX(Integer x) {
		this.x = x;
	}
	public Integer getY() {
		return y;
	}
	public void setY(Integer y) {
		this.y = y;
	}
	public Integer getTamanhoDaFonte() {
		return tamanhoDaFonte;
	}
	public void setTamanhoDaFonte(Integer tamanhoDaFonte) {
		this.tamanhoDaFonte = tamanhoDaFonte;
	}
	
}
