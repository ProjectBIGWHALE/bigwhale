package com.whale.web.documents.certificategenerator.service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.whale.web.documents.certificategenerator.model.enums.CertificateTypeEnum;
import com.whale.web.documents.certificategenerator.utils.ValidateFiledsNullOfCertificatesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whale.web.documents.certificategenerator.model.Certificate;

@Service
public class CreateCertificateService {

	@Autowired
	EditSVGFiles createCerificateService;
	
	public byte[] createCertificates(Certificate certificate, List<String> names) throws Exception {
		certificate.setCertificateTypeEnum(CertificateTypeEnum.COURCE);
		ValidateFiledsNullOfCertificatesUtil.validate(certificate);
		String template = selectPatchCertificateModel(certificate.getCertificateModelId());
		Random random = new Random();
	    List<String> listCertificate = createCerificateService.cretateListCertificate(certificate, names, template);

	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    ZipOutputStream zos = new ZipOutputStream(bos);

	    for (int i = 0; i < listCertificate.size(); i++) {
			String personsName = names.get(i);
			String svgContent = new String(listCertificate.get(i).getBytes(), StandardCharsets.UTF_8);
			long serialPatch = random.nextLong(0, 999999999);

			ZipEntry entry = new ZipEntry(personsName.replace(" ", "") +serialPatch+".svg");
			zos.putNextEntry(entry);
			zos.write(svgContent.getBytes(StandardCharsets.UTF_8));
			zos.closeEntry();
	    }
	    zos.close();

	    return bos.toByteArray();
	}

	private String selectPatchCertificateModel(Long idModel){
		return switch (idModel.intValue()) {
			case 1 -> "../bigwhale/src/main/resources/static/certificatesmodels/certificate1.svg";
			case 2 -> "../bigwhale/src/main/resources/static/certificatesmodels/certificate2.svg";
			default -> throw new RuntimeException("Invalid Patch for model of certificate");
		};
	}
}
