package com.example.demo.service;

import com.example.demo.model.dto.UploadPartFileLinkDto;
import com.example.demo.model.request.UploadFileGetLinkResponse;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileUploadService {
    public void combineFiles(final  File folder, String location) {
        File output = new File(location + ".mp4");

        FileOutputStream fos;
        FileInputStream fis;

        byte[] fileBytes;
        int bytesRead = 0;
        try {
            fos = new FileOutputStream(output, true);
            for (File file : folder.listFiles()) {
                fis = new FileInputStream(file);
                fileBytes = new byte[(int) file.length()];
                bytesRead = fis.read(fileBytes, 0,(int)  file.length());

                assert(bytesRead == fileBytes.length);
                assert(bytesRead == (int) file.length());

                fos.write(fileBytes);
                fos.flush();
                fis.close();
                fis = null;
            }
            fos.close();
            fos = null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void chunksFile(String fileName, String url) {
        File inputFile = new File(url);

        FileInputStream inputStream;
        FileOutputStream filePart;

        int fileSize = (int)inputFile.length();
        int nChunks = 0, read = 0, readLength = 1024 * 1024;
        byte[] byteChunkPart;
        try {
            inputStream = new FileInputStream(inputFile);
            while (fileSize > 0) {
                if (fileSize <= 1024) {
                    readLength = fileSize;
                }

                byteChunkPart = new byte[readLength];
                read = inputStream.read(byteChunkPart, 0, readLength);
                fileSize -= read;
                assert (read == byteChunkPart.length);
                nChunks++;
                String newFileName = fileName + "part" + (nChunks - 1);
                filePart = new FileOutputStream(newFileName);
                filePart.write(byteChunkPart);
                filePart.flush();
                filePart.close();
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UploadFileGetLinkResponse generatePart(Integer partNumber, String folder) {
        if (partNumber < 0) return null;
        List<UploadPartFileLinkDto> uploadPartFileLinks = new ArrayList<>();
        for (int i = 0; i < partNumber; i++) {
            UploadPartFileLinkDto partFileLink = new UploadPartFileLinkDto();
            partFileLink.setFolder(folder);
            partFileLink.setPartNumber("part" + (i + 1));
            uploadPartFileLinks.add(partFileLink);
        }
        return new UploadFileGetLinkResponse(folder, uploadPartFileLinks);
    }

    public String getCheckSum(String url) throws NoSuchAlgorithmException, IOException {
        File file = new File(url);

        MessageDigest md5Digest = MessageDigest.getInstance("MD5");
         return getFileChecksum(md5Digest, file);
    }

    private static String getFileChecksum(MessageDigest digest, File file) throws IOException
    {
        //Get file input stream for reading the file content
        FileInputStream fis = new FileInputStream(file);

        //Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        };

        //close the stream; We don't need it now.
        fis.close();

        //Get the hash's bytes
        byte[] bytes = digest.digest();

        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        //return complete hash
        return sb.toString();
    }
}
