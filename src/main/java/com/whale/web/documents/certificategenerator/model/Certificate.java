package com.whale.web.documents.certificategenerator.model;

import com.whale.web.documents.certificategenerator.model.enums.CertificateTypeEnum;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class Certificate {
	
    private CertificateTypeEnum certificateTypeEnum;
    private String eventName;
    private String speakerName;
    private String speakerRole;
    private String eventWorkLoad;
    private String eventDate;
    private String eventLocale;
    private Long certificateModelId;

    public CertificateTypeEnum getCertificateTypeEnum() {
        return certificateTypeEnum;
    }

    public void setCertificateTypeEnum(CertificateTypeEnum certificateTypeEnum) {
        this.certificateTypeEnum = certificateTypeEnum;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getSpeakerRole() {
        return speakerRole;
    }

    public void setSpeakerRole(String speakerRole) {
        this.speakerRole = speakerRole;
    }

    public String getEventWorkLoad() {
        return eventWorkLoad;
    }

    public void setEventWorkLoad(String eventWorkLoad) {
        this.eventWorkLoad = eventWorkLoad;
    }

    public String getSpeakerName() {
        return speakerName;
    }

    public void setSpeakerName(String speakerName) {
        this.speakerName = speakerName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventLocale() {
        return eventLocale;
    }

    public void setEventLocale(String eventLocale) {
        this.eventLocale = eventLocale;
    }

    public Long getCertificateModelId() {
        return certificateModelId;
    }

    public void setCertificateModelId(Long certificateModelId) {
        this.certificateModelId = certificateModelId;
    }
}
