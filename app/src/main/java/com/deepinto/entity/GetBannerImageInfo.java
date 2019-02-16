package com.deepinto.entity;

import java.util.List;

public class GetBannerImageInfo {

    private String code;
    private String msg;
    private List<ImageInfo> imageInfo;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ImageInfo> getImageInfo() {
        return imageInfo;
    }

    public void setImageInfo(List<ImageInfo> imageInfo) {
        this.imageInfo = imageInfo;
    }
}
