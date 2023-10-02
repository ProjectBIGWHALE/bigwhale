package com.whale.web.documents.compactconverter.service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class CompactConverterService {


    public List<byte[]> converterFile(List<MultipartFile> files, String action) throws IOException {

        return switch (action) {
            case ".zip" -> convertToZip(files);
            case ".tar.gz" -> convertToTarGz(files);
            case ".7z" -> convertTo7z(files);
            case ".tar" -> convertToTar(files);
            default -> new ArrayList<>();
        };

    }

    public List<byte[]> convertToTarGz(List<MultipartFile> files) throws IOException {

        List<byte[]> filesConverted = new ArrayList<>();

        for (MultipartFile file : files) {
            try (ByteArrayInputStream bais = new ByteArrayInputStream(file.getBytes());
                ZipInputStream zis = new ZipInputStream(bais);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new GzipCompressorOutputStream(baos))) {

                byte[] buffer = new byte[8192]; // Use um buffer razoável, 8 KB neste exemplo
                int bytesRead;

                ZipEntry zipEntry;
                while ((zipEntry = zis.getNextEntry()) != null) {
                    TarArchiveEntry tarEntry = new TarArchiveEntry(zipEntry.getName());
                    tarEntry.setSize(zipEntry.getSize()); // Defina o tamanho do arquivo no cabeçalho do TAR
                    tarOut.putArchiveEntry(tarEntry);

                    while ((bytesRead = zis.read(buffer)) != -1) {
                        tarOut.write(buffer, 0, bytesRead);
                    }

                    tarOut.closeArchiveEntry();
                    zis.closeEntry();
                }

                tarOut.finish();
                
                filesConverted.add(baos.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
        return filesConverted;
    }

    public List<byte[]> convertToZip(List<MultipartFile> files) throws IOException {
        List<byte[]> filesConverted = new ArrayList<>();

        for (MultipartFile file : files) {

            if (Objects.requireNonNull(file.getOriginalFilename()).endsWith(".zip")) {
                try (ByteArrayInputStream bais = new ByteArrayInputStream(file.getBytes());
                     var zis = new ZipInputStream(bais);
                     var baos = new ByteArrayOutputStream();
                     var zipOut = new ZipArchiveOutputStream(baos)) {

                    byte[] buffer = new byte[8192];
                    int bytesRead;

                    ZipEntry zipEntry;
                    while ((zipEntry = zis.getNextEntry()) != null) {
                        var zipArchiveEntry = new ZipArchiveEntry(zipEntry.getName());
                        zipArchiveEntry.setSize(zipEntry.getSize());
                        zipOut.putArchiveEntry(zipArchiveEntry);

                        while ((bytesRead = zis.read(buffer)) != -1) {
                            zipOut.write(buffer, 0, bytesRead);
                        }
                        zipOut.closeArchiveEntry();
                    }
                    zipOut.finish();
                    filesConverted.add(baos.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }
        return filesConverted;
    }


    public List<byte[]> convertToTar(List<MultipartFile> files) throws IOException {

        List<byte[]> filesConverted = new ArrayList<>();

        for (MultipartFile file : files) {
        
            try (ByteArrayInputStream bais = new ByteArrayInputStream(file.getBytes());
                ZipInputStream zis = new ZipInputStream(bais);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                TarArchiveOutputStream tarOut = new TarArchiveOutputStream(baos)) {

                byte[] buffer = new byte[8192]; // Use um buffer razoável, 8 KB neste exemplo
                int bytesRead;

                ZipEntry zipEntry;
                while ((zipEntry = zis.getNextEntry()) != null) {
                    TarArchiveEntry tarEntry = new TarArchiveEntry(zipEntry.getName());
                    tarEntry.setSize(zipEntry.getSize()); // Defina o tamanho do arquivo no cabeçalho do TAR
                    tarOut.putArchiveEntry(tarEntry);

                    while ((bytesRead = zis.read(buffer)) != -1) {
                        tarOut.write(buffer, 0, bytesRead);
                    }

                    tarOut.closeArchiveEntry();
                    zis.closeEntry();
                }

                tarOut.finish();
                
                
                filesConverted.add(baos.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
        return filesConverted;
    }

    public List<byte[]> convertTo7z(List<MultipartFile> files) throws IOException {

        List<byte[]> filesConverted = new ArrayList<>();

        for (MultipartFile file : files) {
        try (var bais = new ByteArrayInputStream(file.getBytes());
             var zis = new ZipInputStream(bais)) {

            File tempFile = File.createTempFile("output", ".7z");
            try (var sevenZOut = new SevenZOutputFile(tempFile)) {

                byte[] buffer = new byte[8192];
                int bytesRead;

                ZipEntry zipEntry;
                while ((zipEntry = zis.getNextEntry()) != null) {
                    SevenZArchiveEntry sevenZEntry = new SevenZArchiveEntry();
                    sevenZEntry.setName(zipEntry.getName());
                    sevenZEntry.setSize(zipEntry.getSize());
                    sevenZOut.putArchiveEntry(sevenZEntry);

                    while ((bytesRead = zis.read(buffer)) != -1) {
                        sevenZOut.write(buffer, 0, bytesRead);
                    }

                    sevenZOut.closeArchiveEntry();
                    zis.closeEntry();
                }
            }
            byte[] compressedData = new byte[(int) tempFile.length()];
            try (FileInputStream fis = new FileInputStream(tempFile)) { fis.read(compressedData); }
            filesConverted.add(compressedData);
            Path tempFilePath = tempFile.toPath();
            Files.delete(tempFilePath);

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
        return filesConverted;
}

}
