package com.whale.web.documents.compactconverter.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
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
                tarOut.close(); // Feche o fluxo TarArchiveOutputStream adequadamente
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

            try (ByteArrayInputStream bais = new ByteArrayInputStream(file.getBytes());
             ZipInputStream zis = new ZipInputStream(bais);
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zipOut = new ZipOutputStream(baos)) {

            byte[] buffer = new byte[8192]; // Use um buffer razoável, 8 KB neste exemplo
            int bytesRead;

            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                zipOut.putNextEntry(new ZipEntry(zipEntry.getName()));

                while ((bytesRead = zis.read(buffer)) != -1) {
                    zipOut.write(buffer, 0, bytesRead);
                }

                zipOut.closeEntry();
                zis.closeEntry();
            }

            filesConverted.add(baos.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
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
                tarOut.close(); // Feche o fluxo TarArchiveOutputStream adequadamente
                
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
            try (ByteArrayInputStream bais = new ByteArrayInputStream(file.getBytes());
                ZipInputStream zis = new ZipInputStream(bais);
                ByteArrayOutputStream tarBaos = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[8192]; // Use a reasonable buffer size, 8 KB in this example
                int bytesRead;

                try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(tarBaos);
                    SevenZOutputFile sevenZOut = new SevenZOutputFile(new File("output.7z"))) {

                    ZipEntry zipEntry;
                    while ((zipEntry = zis.getNextEntry()) != null) {
                        TarArchiveEntry tarEntry = new TarArchiveEntry(zipEntry.getName());
                        tarEntry.setSize(zipEntry.getSize()); // Set the file size in the TAR header
                        tarOut.putArchiveEntry(tarEntry);

                        while ((bytesRead = zis.read(buffer)) != -1) {
                            tarOut.write(buffer, 0, bytesRead);
                        }

                        tarOut.closeArchiveEntry();
                        zis.closeEntry();
                    }

                    tarOut.finish();
                    tarOut.close(); // Close the TarArchiveOutputStream properly
                    filesConverted.add(tarBaos.toByteArray());

                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
        return filesConverted;
    }

}
