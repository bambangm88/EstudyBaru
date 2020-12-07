package com.duitku.e_study.Model.Data;

import com.google.gson.annotations.SerializedName;

public class DataListMateri {

    @SerializedName("id_materi")
    private String id_materi;

    @SerializedName("title")
    private String title;

    @SerializedName("materi")
    private String materi;

    @SerializedName("quiz_identity")
    private String quiz_identity;

    @SerializedName("img_url")
    private String img_url;

    @SerializedName("pdf_url")
    private String pdf_url;

    public String getPdf_url() {
        return pdf_url;
    }

    public void setPdf_url(String pdf_url) {
        this.pdf_url = pdf_url;
    }


    public String getId_materi() {
        return id_materi;
    }

    public void setId_materi(String id_materi) {
        this.id_materi = id_materi;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMateri() {
        return materi;
    }

    public void setMateri(String materi) {
        this.materi = materi;
    }

    public String getQuiz_identity() {
        return quiz_identity;
    }

    public void setQuiz_identity(String quiz_identity) {
        this.quiz_identity = quiz_identity;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }



}
