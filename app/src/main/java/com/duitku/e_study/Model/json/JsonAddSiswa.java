package com.duitku.e_study.Model.json;


import com.google.gson.annotations.SerializedName;

public class JsonAddSiswa {

    @SerializedName("nama")
    private String nama;

    @SerializedName("nis")
    private String nis;

    @SerializedName("kelas")
    private String kelas;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }










}


