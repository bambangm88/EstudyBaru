package com.duitku.e_study.Api;


import static com.duitku.e_study.Constant.Constant.BASE_URL_API;

public class Server {
    public static ApiService getAPIService() {
        return Client.getClient(BASE_URL_API).create(ApiService.class);
    }


    public static ApiService getAPIServicePDF() {
        return Client.getClientUploadPDF(BASE_URL_API).create(ApiService.class);
    }




}
