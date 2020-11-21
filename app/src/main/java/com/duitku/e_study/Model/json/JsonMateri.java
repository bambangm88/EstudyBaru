package com.duitku.e_study.Model.json;


import com.google.gson.annotations.SerializedName;

public class JsonMateri {

    @SerializedName("id_materi")
    private String id_materi;


    @SerializedName("title_materi")
    private String title_materi;

    @SerializedName("materi")
    private String materi;

    @SerializedName("image_string")
    private String image_string;

    public String getTitle_materi() {
        return title_materi;
    }

    public void setTitle_materi(String title_materi) {
        this.title_materi = title_materi;
    }

    public String getMateri() {
        return materi;
    }

    public void setMateri(String materi) {
        this.materi = materi;
    }

    public String getImage_string() {
        return image_string;
    }

    public void setImage_string(String image_string) {
        this.image_string = image_string;
    }



    public String getId_materi() {
        return id_materi;
    }

    public void setId_materi(String id_materi) {
        this.id_materi = id_materi;
    }





}


