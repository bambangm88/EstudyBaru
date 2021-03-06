package com.duitku.e_study.Model.response;

import com.duitku.e_study.Model.Data.DataListMateri;
import com.duitku.e_study.Model.Data.DataListQuiz;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseListQuiz {

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
        private List<DataListQuiz> data = null;

        public List<DataListQuiz> getData() {
            return data;
        }

        public void setData(List<DataListQuiz> data) {
            this.data = data;
        }

    }


}

