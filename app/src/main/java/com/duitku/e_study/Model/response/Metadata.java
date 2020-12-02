package com.duitku.e_study.Model.response;

import com.google.gson.annotations.SerializedName;

public class Metadata {

    @SerializedName("code")
    private String code;

    @SerializedName("message")
    private String message;

    @SerializedName("urlDownload")
    private String urlDownload;


    public String getDownloadUrl() {
        return urlDownload;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.urlDownload = downloadUrl;
    }



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
