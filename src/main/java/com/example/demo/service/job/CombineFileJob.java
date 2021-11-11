package com.example.demo.service.job;

import com.example.demo.config.Constant;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Component
@Scope("prototype")
public class CombineFileJob extends Thread{
    private static final Logger log = LoggerFactory.getLogger(CombineFileJob.class);

    private static final int MAX_RETRY = 600;
    private Integer partNumber;
    private String folderName;
    private String contentType;
    private Long fileSize;
    private String fileName;

    public CombineFileJob(Integer partNumber, String folderName, String contentType, Long fileSize, String fileName) {
        this.partNumber = partNumber;
        this.folderName = folderName;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("Combine file");
        int retry = 0;
        while (true) {
            try {
                if (isUploadDone(partNumber, folderName, fileSize)) {
                    log.info("Start combine file");
                    combineFiles(folderName);
                    break;
                } else {
                    try {
                        retry++;
                        Thread.sleep(1000);
                        log.info("Combine file time {}", retry);
                        if (retry > MAX_RETRY) {
                            File folder = new File(Constant.TEMP_FILE + "/" + folderName);
                            FileUtils.deleteDirectory(folder);
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        stopWatch.stop();
        log.info("Complete combine file take {}", stopWatch.getTotalTimeMillis());
    }

    private boolean isUploadDone(Integer partNumber, String folderName, Long fileSize) throws NoSuchAlgorithmException, IOException {
        File folder = new File(Constant.TEMP_FILE + "/" + folderName);

        if (folder.listFiles() == null) return false;
        if (Arrays.asList(folder.listFiles()).size() != partNumber) return false;

        MessageDigest md5Digest = MessageDigest.getInstance("MD5");
        int size = 0;
        for (File file : folder.listFiles()) {
            if (!checkSum(file, md5Digest)) {
                return false;
            }
            size += file.length();
        }
        if (size != fileSize) return false;
        return true;
    }

    public void combineFiles(String folderName) {
        File folder = new File(Constant.TEMP_FILE + "/" + folderName);
        File output = new File( Constant.FINAL_FILE + "/" + folderName + ".mp4");

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
            }
            fos.close();
            //FileUtils.deleteDirectory(folder);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkSum(File file , MessageDigest digest) throws IOException {
        String checkSum = getFileChecksum(digest, file);
        return true;
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
