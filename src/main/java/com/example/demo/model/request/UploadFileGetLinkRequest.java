package com.example.demo.model.request;


public class UploadFileGetLinkRequest {
    private String fileName;
    private Long fileSize;
    private Integer partNumber;
    private String contentType;

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

    public Integer getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(Integer partNumber) {
        this.partNumber = partNumber;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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
