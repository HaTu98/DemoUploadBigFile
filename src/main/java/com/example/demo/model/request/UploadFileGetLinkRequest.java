package com.example.demo.model.request;

import java.io.Serializable;

public class UploadFileGetLinkRequest implements Serializable {
    private String fileName;
    private Long fileSize;
    private Long partNumber;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Long getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(Long partNumber) {
        this.partNumber = partNumber;
    }

    @Override
    public String toString() {
        return "UploadFileGetLinkRequest{" +
                "fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", partNumber=" + partNumber +
                '}';
    }
}
