package com.whale.web.documents.certificategenerator.model.enums;

public enum CertificateTypeEnum {
    PARTICIPATION("Participante"), SPEAKER("Palestrante"), COURCE("Curso");

    private final String type;

    CertificateTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
