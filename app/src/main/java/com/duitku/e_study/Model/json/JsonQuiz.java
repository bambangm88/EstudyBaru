package com.duitku.e_study.Model.json;


import com.google.gson.annotations.SerializedName;

public class JsonQuiz {

    @SerializedName("id_materi")
    private String id_materi;


    @SerializedName("id_quiz")
    private String id_quiz ;

    @SerializedName("image_string")
    private String image_string ;

    @SerializedName("soal")
    private String soal;

    @SerializedName("jawaban_a")
    private String jawaban_a;

    @SerializedName("jawaban_b")
    private String jawaban_b;

    @SerializedName("jawaban_c")
    private String jawaban_c;

    @SerializedName("jawaban_d")
    private String jawaban_d;

    @SerializedName("jawaban_isi")
    private String jawaban_isi;

    public String getId_materi() {
        return id_materi;
    }

    public void setId_materi(String id_materi) {
        this.id_materi = id_materi;
    }

    public String getSoal() {
        return soal;
    }

    public void setSoal(String soal) {
        this.soal = soal;
    }

    public String getJawaban_a() {
        return jawaban_a;
    }

    public void setJawaban_a(String jawaban_a) {
        this.jawaban_a = jawaban_a;
    }

    public String getJawaban_b() {
        return jawaban_b;
    }

    public void setJawaban_b(String jawaban_b) {
        this.jawaban_b = jawaban_b;
    }

    public String getJawaban_c() {
        return jawaban_c;
    }

    public void setJawaban_c(String jawaban_c) {
        this.jawaban_c = jawaban_c;
    }

    public String getJawaban_d() {
        return jawaban_d;
    }

    public void setJawaban_d(String jawaban_d) {
        this.jawaban_d = jawaban_d;
    }

    public String getJawaban_isi() {
        return jawaban_isi;
    }

    public void setJawaban_isi(String jawaban_isi) {
        this.jawaban_isi = jawaban_isi;
    }

    public String getId_quiz() {
        return id_quiz;
    }

    public void setId_quiz(String id_quiz) {
        this.id_quiz = id_quiz;
    }


    public String getImage_string() {
        return image_string;
    }

    public void setImage_string(String image_string) {
        this.image_string = image_string;
    }






}


