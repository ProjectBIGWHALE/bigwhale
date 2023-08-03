package com.whale.web.certifies.service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.whale.web.certifies.model.FormularioCertifies;

@Service
public class CriarCertificadosService {
	
	@Autowired
	UploadImagemServiceCertifies uploadImagemService;
	
	@Autowired
	EditarImagemService editarImagemService;
	
	public byte[] criarCertificados(FormularioCertifies formulario, List<String> nomes) throws Exception {
		
	    Integer x = formulario.getX();
	    Integer y = formulario.getY();
	    Integer tamanhoDaFonte = formulario.getTamanhoDaFonte();
	    
	    MultipartFile imagemLayoult = uploadImagemService.uploadImagem(formulario.getImagemLayoult());
	    
	    if(x == null || y == null || tamanhoDaFonte == null || imagemLayoult == null || imagemLayoult.isEmpty()) {
	    	throw new Exception();
	    }
	    
	    List<byte[]> imagensComTexto = editarImagemService.editarImagem(imagemLayoult, nomes, y, x, tamanhoDaFonte);

	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    ZipOutputStream zos = new ZipOutputStream(bos);

	    for (int i = 0; i < imagensComTexto.size(); i++) {
	        byte[] imagem = imagensComTexto.get(i);
	        String nomeDaPessoa = nomes.get(i);

	        ZipEntry entry = new ZipEntry(nomeDaPessoa.replace(" ", "") + ".png");
	        zos.putNextEntry(entry);
	        zos.write(imagem);
	        zos.closeEntry();
	    }
	    zos.close();

	    return bos.toByteArray();
		

	}
	
}
