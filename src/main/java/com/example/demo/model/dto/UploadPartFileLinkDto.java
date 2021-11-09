package com.example.demo.model.dto;

import java.io.Serializable;

public class UploadPartFileLinkDto implements Serializable {
    private String partNumber;
    private String folder;

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    @Override
    public String toString() {
        return "UploadPartFileLinkDto{" +
                "partNumber='" + partNumber + '\'' +
                ", folder='" + folder + '\'' +
                '}';
    }
}
