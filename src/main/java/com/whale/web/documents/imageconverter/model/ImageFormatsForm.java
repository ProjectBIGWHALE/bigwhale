package com.whale.web.documents.imageconverter.model;

public class ImageFormatsForm {
	
    private Long id;
    private String name;

    public ImageFormatsForm(){}

    public ImageFormatsForm(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
	
}
