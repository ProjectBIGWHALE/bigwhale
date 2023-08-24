package com.whale.web.security.cryptograph.service;

import com.whale.web.documents.imageconverter.exception.InvalidUploadedFileException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.whale.web.security.cryptograph.model.CryptographyForm;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@Service
public class EncryptService {

	public byte[] encryptFile(CryptographyForm form) throws Exception {
		MultipartFile formFile = form.getFile();
		String encryptionKey = form.getKey();

		try {

			if (formFile.isEmpty() || encryptionKey.isEmpty()) {
				throw new InvalidUploadedFileException("An invalid file was sent");
			}

			// Get the bytes of the file from MultipartFile
			byte[] bytesInFile = formFile.getBytes();

			// Create a secret key with the provided key
			byte[] keyBytes = Arrays.copyOf(encryptionKey.getBytes(StandardCharsets.UTF_8), 16); // Adjust the key size to 16 bytes
			SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

			// Create a Cipher object for encryption
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			// Empty initialization vector for CBC mode
			IvParameterSpec ivParameterSpec = new IvParameterSpec(new byte[16]);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

			// Encrypt the file
			return cipher.doFinal(bytesInFile);

		} catch (Exception e) {
			// Handle the exception according to your needs
			throw new Exception("Error encrypting the file.", e);
		}
	}

	public byte[] decryptFile(CryptographyForm form) throws Exception {
		MultipartFile fileOfForm = form.getFile();
		String encryptionKey = form.getKey();

		try {

			if (fileOfForm.isEmpty() || encryptionKey.isEmpty()) {
				throw new InvalidUploadedFileException("An invalid file was sent");
			}

			// Get the bytes of the file from MultipartFile
			byte[] encryptedFile = fileOfForm.getBytes();

			// Create a secret key with the provided key
			byte[] keyBytes = Arrays.copyOf(encryptionKey.getBytes(StandardCharsets.UTF_8), 16); // Adjust the key size to 16 bytes
			SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

			// Create a Cipher object for decryption
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			// Empty initialization vector for CBC mode
			IvParameterSpec ivParameterSpec = new IvParameterSpec(new byte[16]);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

			// Decrypt the file
			return cipher.doFinal(encryptedFile);
			
		} catch (Exception e) {
			// Handle the exception according to your needs
			throw new Exception("Error decrypting the file.", e);
		}
	}
}
