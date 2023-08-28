package com.whale.web.documents.certificategenerator.service;

import java.io.*;
import java.util.*;

import com.whale.web.documents.certificategenerator.model.Certificate;
import com.whale.web.documents.certificategenerator.model.enums.CertificateBasicInfosEnum;
import com.whale.web.documents.certificategenerator.model.enums.PersonBasicInfosEnum;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

@Service
public class EditSVGFiles {

	public List<String> cretateListCertificate(Certificate certificate, List<String> names, String certificateTemplate) throws Exception {
		Document patchModel = preparateCertificateWithBasicInfos(certificate, certificateTemplate);
		List<String> listCertificates = new ArrayList<>();

	    for (String name : names) {

	        String certificateForPerson = preparateCertificateForPerson(name, patchModel);

	        listCertificates.add(certificateForPerson);
	    }

	    return listCertificates;
	}

	private String preparateCertificateForPerson(String personName, Document document) throws Exception{
		List<PersonBasicInfosEnum> basicInfos = Arrays.asList(PersonBasicInfosEnum.values());

		NodeList textElements = document.getElementsByTagName("tspan");

		for (int i = 0; i < textElements.getLength(); i++) {
			Element textElement = (Element) textElements.item(i);
			String id = textElement.getAttribute("id");
			for (PersonBasicInfosEnum basicInfo : basicInfos) {
				if (id.equals(basicInfo.getTagId())) {
					switch (basicInfo) {
						case PERSON_NAME, PERSON_NAME_BODY -> textElement.setTextContent(personName);
					}
				}
			}
		}
		return convertDocumentToString(document);
	}

	private Document preparateCertificateWithBasicInfos(Certificate certificate, String certificateTemplate)
			throws ParserConfigurationException, IOException, SAXException, TransformerException {
		List<CertificateBasicInfosEnum> basicInfos = Arrays.asList(CertificateBasicInfosEnum.values());

		Document document = readSVG(certificateTemplate);
		NodeList textElements = document.getElementsByTagName("tspan");

		for (int i = 0; i < textElements.getLength(); i++) {
			Element textElement = (Element) textElements.item(i);
			String id = textElement.getAttribute("id");
			for (CertificateBasicInfosEnum basicInfo : basicInfos) {
				if (id.equals(basicInfo.getTagId())) {
					switch (basicInfo) {
						case EVENT_NAME, EVENT_NAME_BODY -> textElement.setTextContent(certificate.getEventName());
						case SPEAKER_NAME -> textElement.setTextContent(certificate.getSpeakerName());
						case SPEAKER_ROLE -> textElement.setTextContent(certificate.getSpeakerRole());
						case EVENT_DATE, EVENT_DATE_BODY -> textElement.setTextContent(certificate.getEventDate());
						case WORKLOAD -> textElement.setTextContent(certificate.getEventWorkLoad());
						case EVENT_LOCALE -> textElement.setTextContent(certificate.getEventLocale());
					}
				}
			}
		}
		return document;
	}

	private Document readSVG(String certificateTemplate) throws ParserConfigurationException, IOException, SAXException {
		File svgFile = new File(certificateTemplate);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(svgFile);
	}

	private String convertDocumentToString(Document document) throws TransformerException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(document), new StreamResult(writer));
		return writer.toString();
	}
}
