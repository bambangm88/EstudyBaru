package com.duitku.e_study.Api;




import com.duitku.e_study.Model.json.JsonAddSiswa;
import com.duitku.e_study.Model.json.JsonChangePassword;
import com.duitku.e_study.Model.json.JsonListQuiz;
import com.duitku.e_study.Model.json.JsonLogin;
import com.duitku.e_study.Model.json.JsonMateri;
import com.duitku.e_study.Model.json.JsonPeriod;
import com.duitku.e_study.Model.json.JsonQuiz;
import com.duitku.e_study.Model.json.JsonScore;
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
    @POST("siswa/addScore")
    Call<ResponseData> requestAddScore(@Body JsonScore body);

    @GET("siswa/downloadScore")
    Call<ResponseData> requestDownloadScore();

    @Headers("Content-Type: application/json")
    @POST("siswa/addMateri")
    Call<ResponseData> requestAddMateri(@Body JsonMateri body);

    @Headers("Content-Type: application/json")
    @POST("siswa/editMateri")
    Call<ResponseData> requestEditMateri(@Body JsonMateri body);

    @Headers("Content-Type: application/json")
    @POST("siswa/editQuiz")
    Call<ResponseData> requestEditQuiz(@Body JsonQuiz body);

    @Headers("Content-Type: application/json")
    @POST("siswa/deleteQuiz")
    Call<ResponseData> requestDeleteQuiz(@Body JsonQuiz body);

    @Headers("Content-Type: application/json")
    @POST("siswa/deleteMateri")
    Call<ResponseData> requestDeleteMateri(@Body JsonMateri body);

    @Headers("Content-Type: application/json")
    @POST("siswa/addSiswa")
    Call<ResponseData> requestAddSiswa(@Body JsonAddSiswa body);

    @Headers("Content-Type: application/json")
    @POST("siswa/changePassword")
    Call<ResponseData> requestChangePassword(@Body JsonChangePassword body);

    @GET("siswa/listMateri")
    Call<ResponseListMateri> listMateri();

    @Headers("Content-Type: application/json")
    @POST("siswa/listQuiz")
    Call<ResponseListQuiz> requestListQuiz(@Body JsonListQuiz body);

}
