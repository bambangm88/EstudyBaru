package com.duitku.e_study.Model.json;


import com.google.gson.annotations.SerializedName;

public class JsonChangePassword {

    @SerializedName("nis")
    private String nis;

    @SerializedName("pwdBaru")
    private String pwdBaru;

    @SerializedName("pwdLama")
    private String pwdlama;

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public String getPwdlama() {
        return pwdlama;
    }

    public void setPwdlama(String pwdlama) {
        this.pwdlama = pwdlama;
    }

    public String getPwdBaru() {
        return pwdBaru;
    }

    public void setPwdBaru(String pwdBaru) {
        this.pwdBaru = pwdBaru;
    }



}


