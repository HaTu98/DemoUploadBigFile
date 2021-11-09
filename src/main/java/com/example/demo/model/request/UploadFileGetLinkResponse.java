package com.example.demo.model.request;

import com.example.demo.model.dto.UploadPartFileLinkDto;

import java.io.Serializable;
import java.util.List;

public class UploadFileGetLinkResponse implements Serializable {
    private String linkUrl;
    private List<UploadPartFileLinkDto> parts;

    public UploadFileGetLinkResponse() {
    }

    public UploadFileGetLinkResponse(String linkUrl, List<UploadPartFileLinkDto> parts) {
        this.linkUrl = linkUrl;
        this.parts = parts;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public List<UploadPartFileLinkDto> getParts() {
        return parts;
    }

    public void setParts(List<UploadPartFileLinkDto> parts) {
        this.parts = parts;
    }

    @Override
    public String toString() {
        return "UploadFileGetLinkResponse{" +
                "linkUrl='" + linkUrl + '\'' +
                ", parts=" + parts +
                '}';
    }
}
