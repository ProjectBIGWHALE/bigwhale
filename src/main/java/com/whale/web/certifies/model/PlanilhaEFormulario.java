package com.whale.web.certifies.model;

import org.springframework.stereotype.Service;

@Service
public class PlanilhaEFormulario {
	
	private Planilha planilha;
	private FormularioCertifies formulario;
	
	public Planilha getPlanilha() {
		return planilha;
	}
	public void setPlanilha(Planilha planilha) {
		this.planilha = planilha;
	}
	public FormularioCertifies getFormulario() {
		return formulario;
	}
	public void setFormulario(FormularioCertifies formulario) {
		this.formulario = formulario;
	}
	
	
}
