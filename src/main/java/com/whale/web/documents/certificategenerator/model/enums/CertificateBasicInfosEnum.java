package com.whale.web.documents.certificategenerator.model.enums;

public enum CertificateBasicInfosEnum {
    EVENT_NAME("basicInfoEventName"), EVENT_NAME_BODY("basicInfoEventNameTwo"), SPEAKER_NAME("basicInfoSpeakerName"),
    SPEAKER_ROLE("basicInfoSpeakerRole"), EVENT_DATE("basicInfoEventDate"), WORKLOAD("basicInfoEventWorkload"),
    EVENT_LOCALE("basicInfoEventLocale"), EVENT_DATE_BODY("basicInfoEventDateTwo");

    private final String tagId;

    CertificateBasicInfosEnum(String tagId) {
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
