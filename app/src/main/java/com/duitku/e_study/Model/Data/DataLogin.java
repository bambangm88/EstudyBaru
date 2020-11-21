package com.duitku.e_study.Model.Data;

import com.google.gson.annotations.SerializedName;

public class DataLogin {

    @SerializedName("id_user")
    private String id_user;

    @SerializedName("nis")
    private String nis;

    @SerializedName("nama")
    private String nama;

    @SerializedName("kelas")
    private String kelas;

    @SerializedName("level")
    private String level;


    @SerializedName("pwd")
    private String pwd;


    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }






}
