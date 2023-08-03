package com.whale.web.security.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.whale.web.security.model.FormularioCriptoSecurity;

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@Service
public class EncriptografarService {
	
	public byte[] encriptografarArquivo(FormularioCriptoSecurity formulario) throws Exception {
	    MultipartFile arquivoDoFormulario = formulario.getArquivo();
	    String chaveDeCriptografia = formulario.getChave();
	    
	    try {
	    	
		    if(arquivoDoFormulario.isEmpty() || arquivoDoFormulario == null || chaveDeCriptografia.isEmpty() || chaveDeCriptografia == null) {
		    	throw new Exception();
		    }
	    	
	        // Obtém os bytes do arquivo do MultipartFile
	        byte[] arquivoBytes = arquivoDoFormulario.getBytes();

	        // Cria uma chave secreta com a chave fornecida
	        byte[] chaveBytes = Arrays.copyOf(chaveDeCriptografia.getBytes("UTF-8"), 16); // Ajuste o tamanho da chave para 16 bytes
	        SecretKeySpec secretKey = new SecretKeySpec(chaveBytes, "AES");

	        // Cria um objeto Cipher para realizar a criptografia
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        IvParameterSpec ivParameterSpec = new IvParameterSpec(new byte[16]); // Vetor de inicialização vazio para CBC mode
	        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

	        // Encripta o arquivo
	        byte[] arquivoEncriptado = cipher.doFinal(arquivoBytes);

	        return arquivoEncriptado;
	    } catch (Exception e) {
	        // Trate a exceção de acordo com sua necessidade
	        throw new Exception("Erro ao encriptografar o arquivo.", e);
	    }
	}

	public byte[] descriptografarArquivo(FormularioCriptoSecurity formulario) throws Exception {
	    MultipartFile arquivoDoFormulario = formulario.getArquivo();
	    String chaveDeCriptografia = formulario.getChave();
	    
	    try {
	    	
	        if (arquivoDoFormulario.isEmpty() || arquivoDoFormulario == null || chaveDeCriptografia.isEmpty() || chaveDeCriptografia == null) {
	            throw new Exception();
	        }
	        
	        // Obtém os bytes do arquivo do MultipartFile
	        byte[] arquivoEncriptado = arquivoDoFormulario.getBytes();

	        // Cria uma chave secreta com a chave fornecida
	        byte[] chaveBytes = Arrays.copyOf(chaveDeCriptografia.getBytes("UTF-8"), 16); // Ajuste o tamanho da chave para 16 bytes
	        SecretKeySpec secretKey = new SecretKeySpec(chaveBytes, "AES");

	        // Cria um objeto Cipher para realizar a descriptografia
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        IvParameterSpec ivParameterSpec = new IvParameterSpec(new byte[16]); // Vetor de inicialização vazio para CBC mode
	        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

	        // Descriptografa o arquivo
	        byte[] arquivoDescriptografado = cipher.doFinal(arquivoEncriptado);

	        return arquivoDescriptografado;
	    } catch (Exception e) {
	        // Trate a exceção de acordo com sua necessidade
	        throw new Exception("Erro ao descriptografar o arquivo.", e);
	    }
	}


}
