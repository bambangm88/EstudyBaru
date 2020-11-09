package com.duitku.e_study.Model.json;


import com.google.gson.annotations.SerializedName;

public class JsonListQuiz {


    @SerializedName("id_materi")
    private String id_materi;

    public String getId_materi() {
        return id_materi;
    }

    public void setId_materi(String id_materi) {
        this.id_materi = id_materi;
    }



}


