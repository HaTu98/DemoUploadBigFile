package com.example.demo.service;


import com.example.demo.service.job.CombineFileJob;
import com.example.demo.service.job.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class CombineFileService {
    private static final Logger log = LoggerFactory.getLogger(CombineFileService.class);
    private final ApplicationContext applicationContext;
    private ExecutorService pool = Executors.newFixedThreadPool(2);

    public CombineFileService(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void combineFile(Integer partNumber, String folderName, String contentType, Long fileSize, String fileName) {
        log.info("Job combine partNumber {}, folderName {}, contentType {}, fileSize {}, fileName {}", partNumber, folderName, contentType, fileSize, fileName);
        Thread thread = applicationContext.getBean(CombineFileJob.class, partNumber, folderName, contentType, fileSize, fileName);
        thread.start();
    }

    public void combineFileJob(Integer partNumber, String folderName, String contentType, Long fileSize, String fileName) {
        Runnable job = new Consumer(partNumber, folderName, contentType, fileSize, fileName);
        pool.execute(job);
    }
}
