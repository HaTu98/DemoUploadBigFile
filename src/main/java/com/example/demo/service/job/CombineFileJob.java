package com.example.demo.service.job;

import com.example.demo.config.Constant;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.io.*;
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
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        stopWatch.stop();
        log.info("Complete combine file take {}", stopWatch.getTotalTimeMillis());
    }

    private boolean isUploadDone(Integer partNumber, String folderName, Long fileSize) {
        File folder = new File(Constant.TEMP_FILE + "\\" + folderName);

        if (folder.listFiles() == null) return false;
        if (Arrays.asList(folder.listFiles()).size() != partNumber) return false;
        int size = 0;
        for (File file : folder.listFiles()) {
            size += file.length();
        }
        if (size != fileSize) return false;
        return true;
    }

    public void combineFiles(String folderName) {
        File folder = new File(Constant.TEMP_FILE + "\\" + folderName);
        File output = new File( Constant.FINAL_FILE + "\\" + folderName + ".mp4");

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
            FileUtils.deleteDirectory(folder);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
