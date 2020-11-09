package com.duitku.e_study.Api;




import com.duitku.e_study.Model.json.JsonAddSiswa;
import com.duitku.e_study.Model.json.JsonListQuiz;
import com.duitku.e_study.Model.json.JsonLogin;
import com.duitku.e_study.Model.json.JsonPeriod;
import com.duitku.e_study.Model.json.JsonQuiz;
import com.duitku.e_study.Model.response.ResponseData;
import com.duitku.e_study.Model.response.ResponseListMateri;
import com.duitku.e_study.Model.response.ResponseListQuiz;
import com.duitku.e_study.Model.response.ResponseLogin;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("siswa/login")
    Call<ResponseLogin> requestLogin(@Body JsonLogin body);

    @Headers("Content-Type: application/json")
    @POST("siswa/addQuiz")
    Call<ResponseData> requestAddQuiz(@Body JsonQuiz body);

    @Headers("Content-Type: application/json")
    @POST("siswa/addSiswa")
    Call<ResponseData> requestAddSiswa(@Body JsonAddSiswa body);

    @GET("siswa/listMateri")
    Call<ResponseListMateri> listMateri();

    @Headers("Content-Type: application/json")
    @POST("siswa/listQuiz")
    Call<ResponseListQuiz> requestListQuiz(@Body JsonListQuiz body);

}
