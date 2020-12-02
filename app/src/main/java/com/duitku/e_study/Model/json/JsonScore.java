package com.duitku.e_study.Model.json;


import com.google.gson.annotations.SerializedName;

public class JsonScore {

    @SerializedName("id_materi")
    private String id_materi;

    @SerializedName("nis")
    private String nis ;

    @SerializedName("score")
    private String score ;


    @SerializedName("soal_total")
    private String soal_total ;

    @SerializedName("soal_benar")
    private String soal_benar;

    @SerializedName("soal_salah")
    private String soal_salah;



    public String getId_materi() {
        return id_materi;
    }

    public void setId_materi(String id_materi) {
        this.id_materi = id_materi;
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getSoal_benar() {
        return soal_benar;
    }

    public void setSoal_benar(String soal_benar) {
        this.soal_benar = soal_benar;
    }

    public String getSoal_salah() {
        return soal_salah;
    }

    public void setSoal_salah(String soal_salah) {
        this.soal_salah = soal_salah;
    }



    public String getSoal_total() {
        return soal_total;
    }

    public void setSoal_total(String soal_total) {
        this.soal_total = soal_total;
    }



}


