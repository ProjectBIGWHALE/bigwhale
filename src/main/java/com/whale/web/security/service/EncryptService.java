package com.whale.web.security.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.whale.web.security.model.CryptoFormSecurity;

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@Service
public class EncryptService {

	public byte[] encryptFile(CryptoFormSecurity form) throws Exception {
		MultipartFile formFile = form.getFile();
		String encryptionKey = form.getKey();

		try {

			if (formFile.isEmpty() || formFile == null || encryptionKey.isEmpty() || encryptionKey == null) {
				throw new Exception();
			}

			// Get the bytes of the file from MultipartFile
			byte[] bytesInFile = formFile.getBytes();

			// Create a secret key with the provided key
			byte[] keyBytes = Arrays.copyOf(encryptionKey.getBytes("UTF-8"), 16); // Adjust the key size to 16 bytes
			SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

			// Create a Cipher object for encryption
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec ivParameterSpec = new IvParameterSpec(new byte[16]); // Empty initialization vector for CBC
																					// mode
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

			// Encrypt the file
			byte[] encryptedFile = cipher.doFinal(bytesInFile);

			return encryptedFile;
		} catch (Exception e) {
			// Handle the exception according to your needs
			throw new Exception("Error encrypting the file.", e);
		}
	}

	public byte[] decryptFile(CryptoFormSecurity form) throws Exception {
		MultipartFile fileOfForm = form.getFile();
		String encryptionKey = form.getKey();

		try {

			if (fileOfForm.isEmpty() || fileOfForm == null || encryptionKey.isEmpty() || encryptionKey == null) {
				throw new Exception();
			}

			// Get the bytes of the file from MultipartFile
			byte[] encryptedFile = fileOfForm.getBytes();

			// Create a secret key with the provided key
			byte[] keyBytes = Arrays.copyOf(encryptionKey.getBytes("UTF-8"), 16); // Adjust the key size to 16 bytes
			SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

			// Create a Cipher object for decryption
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec ivParameterSpec = new IvParameterSpec(new byte[16]); // Empty initialization vector for CBC
																					// mode
			cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

			// Decrypt the file
			byte[] decryptedFile = cipher.doFinal(encryptedFile);

			return decryptedFile;
		} catch (Exception e) {
			// Handle the exception according to your needs
			throw new Exception("Error decrypting the file.", e);
		}
	}
}
