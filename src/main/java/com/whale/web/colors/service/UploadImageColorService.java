package com.whale.web.colors.service;

import java.io.ByteArrayInputStream;

import com.whale.web.colors.configuration.CustomMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadImageColorService {
	
    public MultipartFile uploadImage(MultipartFile file) throws Exception {

        // Aqui, o método extrai os bytes da imagem enviada.
        byte[] bytes = file.getBytes();

        // Os bytes da imagem são armazenados em um objeto ByteArrayInputStream.
        // Isto é útil quando você precisa manipular dados armazenados em memória
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

        // Cria-se uma instância personalizada em memória por meio dos dados obtidos da imagem original
        MultipartFile multipartFile = new CustomMultipartFile(file.getOriginalFilename(),
                                                               file.getContentType(),
                                                                inputStream);

        // retornar-se o upload da imagem com os dados originais
        return multipartFile;
        
    }
	
}
