package com.whale.web.certifies.model;

import org.springframework.stereotype.Service;

@Service
public class WorksheetAndForm {
	
	private Worksheet worksheet;
	private FormCertifies form;
	
	public Worksheet getWorksheet() {
		return worksheet;
	}
	public void setWorksheet(Worksheet worksheet) {
		this.worksheet = worksheet;
	}
	public FormCertifies getForm() {
		return form;
	}
	public void setForm(FormCertifies form) {
		this.form = form;
	}
	
	
}
