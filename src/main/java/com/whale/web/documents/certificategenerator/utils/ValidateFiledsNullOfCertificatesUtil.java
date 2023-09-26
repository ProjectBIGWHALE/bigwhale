package com.whale.web.documents.certificategenerator.utils;

import com.whale.web.documents.certificategenerator.model.Certificate;

import java.lang.reflect.Field;

public class ValidateFiledsNullOfCertificatesUtil {
    public static void validate(Certificate certificate) throws IllegalAccessException {
        Class<Certificate> certificateClass = Certificate.class;

        Field[] fields = certificateClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Object objet = field.get(certificate);
            if (objet == null) {
                throw new RuntimeException("Field: "+field.getName()+" is null");
            }
        }
    }
}
