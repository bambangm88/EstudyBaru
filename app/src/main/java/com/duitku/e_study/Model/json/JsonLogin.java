package com.duitku.e_study.Model.json;


import com.google.gson.annotations.SerializedName;

public class JsonLogin {

    @SerializedName("nis")
    private String nis;

    @SerializedName("pwd")
    private String pwd;

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }







}


