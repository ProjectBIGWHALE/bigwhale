package com.whale.web.security.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.whale.web.security.model.CryptoFormSecurity;

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@Service
public class EncriptografarService {
	
	public byte[] encriptografarArquivo(CryptoFormSecurity form) throws Exception {
	    MultipartFile formFile = form.getFile();
	    String encryptionKey = form.getKey();
	    
	    try {
	    	
		    if(formFile.isEmpty() || formFile == null || encryptionKey.isEmpty() || encryptionKey == null) {
		    	throw new Exception();
		    }
	    	
	        // Obtém os bytes do arquivo do MultipartFile
	        byte[] bytesInFile = formFile.getBytes();

	        // Cria uma chave secreta com a chave fornecida
	        byte[] chaveBytes = Arrays.copyOf(encryptionKey.getBytes("UTF-8"), 16); // Ajuste o tamanho da chave para 16 bytes
	        SecretKeySpec secretKey = new SecretKeySpec(chaveBytes, "AES");

	        // Cria um objeto Cipher para realizar a criptografia
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        IvParameterSpec ivParameterSpec = new IvParameterSpec(new byte[16]); // Vetor de inicialização vazio para CBC mode
	        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

	        // Encripta o arquivo
	        byte[] encryptedFile = cipher.doFinal(bytesInFile);

	        return encryptedFile;
	    } catch (Exception e) {
	        // Trate a exceção de acordo com sua necessidade
	        throw new Exception("Error encrypting the file.", e);
	    }
	}

	public byte[] descriptografarArquivo(CryptoFormSecurity form) throws Exception {
	    MultipartFile fileOfForm = form.getFile();
	    String encryptionKey = form.getKey();
	    
	    try {
	    	
	        if (fileOfForm.isEmpty() || fileOfForm == null || encryptionKey.isEmpty() || encryptionKey == null) {
	            throw new Exception();
	        }
	        
	        // Obtém os bytes do arquivo do MultipartFile
	        byte[] encryptedFile = fileOfForm.getBytes();

	        // Cria uma chave secreta com a chave fornecida
	        byte[] chaveBytes = Arrays.copyOf(encryptionKey.getBytes("UTF-8"), 16); // Ajuste o tamanho da chave para 16 bytes
	        SecretKeySpec secretKey = new SecretKeySpec(chaveBytes, "AES");

	        // Cria um objeto Cipher para realizar a descriptografia
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        IvParameterSpec ivParameterSpec = new IvParameterSpec(new byte[16]); // Vetor de inicialização vazio para CBC mode
	        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

	        // Descriptografa o arquivo
	        byte[] decryptedFile = cipher.doFinal(encryptedFile);

	        return decryptedFile;
	    } catch (Exception e) {
	        // Trate a exceção de acordo com sua necessidade
	        throw new Exception("Error decrypting the file.", e);
	    }
	}


}
