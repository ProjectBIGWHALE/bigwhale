package com.whale.web.documents.compactconverter.service;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;
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
            default -> Collections.emptyList();
        };

    }

    public List<byte[]> convertToZip(List<MultipartFile> files) throws IOException {
        List<byte[]> zipFiles = new ArrayList<>();

        for (MultipartFile file : files) {

                ByteArrayOutputStream zipOutputStream = new ByteArrayOutputStream();
                ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(zipOutputStream);
                ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(file.getInputStream());

                ZipArchiveEntry zipEntry;
                while ((zipEntry = zipArchiveInputStream.getNextZipEntry()) != null) {

                    ZipArchiveEntry newZipEntry = new ZipArchiveEntry(zipEntry.getName());
                    zipArchiveOutputStream.putArchiveEntry(newZipEntry);
                    IOUtils.copy(zipArchiveInputStream, zipArchiveOutputStream);
                    zipArchiveOutputStream.closeArchiveEntry();
                }

                zipArchiveInputStream.close();
                zipArchiveOutputStream.close();
                zipFiles.add(zipOutputStream.toByteArray());


        }
        return zipFiles;
    }

    public List<byte[]> convertToTar(List<MultipartFile> files) throws IOException {
        List<byte[]> filesConverted = new ArrayList<>();

        for (MultipartFile file : files) {

                try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                     TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
                     InputStream zipInputStream = file.getInputStream();
                     ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(zipInputStream)) {

                    ZipArchiveEntry zipEntry;
                    while ((zipEntry = zipArchiveInputStream.getNextZipEntry()) != null) {
                        TarArchiveEntry tarEntry = new TarArchiveEntry(zipEntry.getName());
                        tarEntry.setSize(zipEntry.getSize());

                        tarOutputStream.putArchiveEntry(tarEntry);
                        IOUtils.copy(zipArchiveInputStream, tarOutputStream);
                        tarOutputStream.closeArchiveEntry();
                    }
                    filesConverted.add(outputStream.toByteArray());
                }


        }
        return filesConverted;
    }


    public List<byte[]> convertTo7z(List<MultipartFile> files) throws IOException {
        List<byte[]> filesConverted = new ArrayList<>();

        for (MultipartFile file : files) {

                File tempFile = File.createTempFile("temp", ".7z");
                tempFile.deleteOnExit();
                try (SevenZOutputFile sevenZOutputFile = new SevenZOutputFile(tempFile)) {
                    try (InputStream zipInputStream = file.getInputStream();
                         ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(zipInputStream)) {

                        ZipArchiveEntry zipEntry;
                        while ((zipEntry = zipArchiveInputStream.getNextZipEntry()) != null) {
                            SevenZArchiveEntry sevenZEntry = sevenZOutputFile.createArchiveEntry(new File(zipEntry.getName()), zipEntry.getName());
                            sevenZOutputFile.putArchiveEntry(sevenZEntry);

                            byte[] buffer = new byte[8192];
                            int bytesRead;
                            while ((bytesRead = zipArchiveInputStream.read(buffer)) != -1) {
                                sevenZOutputFile.write(buffer, 0, bytesRead);
                            }
                            sevenZOutputFile.closeArchiveEntry();
                        }
                    }
                }

                byte[] sevenZBytes = Files.readAllBytes(tempFile.toPath());
                filesConverted.add(sevenZBytes);
                tempFile.delete();

        }

        return filesConverted;
    }



    public List<byte[]> convertToTarGz(List<MultipartFile> files) throws IOException {
        List<byte[]> filesConverted = new ArrayList<>();

        for (MultipartFile file : files) {

                ByteArrayOutputStream tarGzOutputStream = new ByteArrayOutputStream();

                try (TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(tarGzOutputStream)) {
                    InputStream zipInputStream = file.getInputStream();
                    ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(zipInputStream);

                    ZipArchiveEntry zipEntry;
                    while ((zipEntry = zipArchiveInputStream.getNextZipEntry()) != null) {
                        TarArchiveEntry tarEntry = new TarArchiveEntry(zipEntry.getName());
                        tarEntry.setSize(zipEntry.getSize());
                        tarArchiveOutputStream.putArchiveEntry(tarEntry);
                        IOUtils.copy(zipArchiveInputStream, tarArchiveOutputStream);
                        tarArchiveOutputStream.closeArchiveEntry();
                    }
                }

                try (GzipCompressorOutputStream gzipOutputStream = new GzipCompressorOutputStream(tarGzOutputStream)) {
                    gzipOutputStream.write(tarGzOutputStream.toByteArray());
                }
                filesConverted.add(tarGzOutputStream.toByteArray());
        }
        return filesConverted;
    }

}


