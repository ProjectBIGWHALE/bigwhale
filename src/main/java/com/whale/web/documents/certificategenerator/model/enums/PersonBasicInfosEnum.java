package com.whale.web.documents.certificategenerator.model.enums;

public enum PersonBasicInfosEnum {
    PERSON_NAME("personName"), PERSON_NAME_BODY("personNameTwo");

    private final String tagId;

    PersonBasicInfosEnum(String tagId) {
        this.tagId = tagId;
    }

    public String getTagId() {
        return tagId;
    }

    @Override
    public String toString() {
        return tagId;
    }
}
