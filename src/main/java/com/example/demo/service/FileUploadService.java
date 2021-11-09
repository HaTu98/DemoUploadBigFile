package com.example.demo.service;

import com.example.demo.model.dto.UploadPartFileLinkDto;
import com.example.demo.model.request.UploadFileGetLinkResponse;
import org.springframework.stereotype.Service;

import java.io.*;
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
}
