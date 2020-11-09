package com.duitku.e_study.Model.response;

import com.duitku.e_study.Model.Data.DataListMateri;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseListMateri {

    @SerializedName("metadata")
    private Metadata metadata = null;

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    @SerializedName("response")
    private Response response = null;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public class Response {

        @SerializedName("data")
        @Expose
        private List<DataListMateri> data = null;

        public List<DataListMateri> getData() {
            return data;
        }

        public void setData(List<DataListMateri> data) {
            this.data = data;
        }

    }


}

