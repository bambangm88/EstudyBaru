package com.duitku.e_study.Model.response;

import com.google.gson.annotations.SerializedName;

public class ResponseLogin {

    @SerializedName("metadata")
    private Metadata metadata = null;

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }





}

