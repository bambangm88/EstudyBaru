package com.duitku.e_study.Auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.duitku.e_study.Api.ApiService;
import com.duitku.e_study.Api.Server;
import com.duitku.e_study.Constant.Constant;
import com.duitku.e_study.Model.json.JsonAddSiswa;
import com.duitku.e_study.Model.json.JsonQuiz;
import com.duitku.e_study.Model.response.ResponseData;
import com.duitku.e_study.R;
import com.duitku.e_study.Session.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {


    private ApiService API;
    private Context mContext ;
    private SessionManager sessionManager ;
    private RelativeLayout rlprogress;
    private EditText nama , nis , kelas ;
    private Button btnSubmit ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        API = Server.getAPIService();
        mContext = this ;
        sessionManager = new SessionManager(mContext);
        rlprogress = findViewById(R.id.rlprogress);

        nama = findViewById(R.id.nama);
        nis = findViewById(R.id.nis);
        kelas = findViewById(R.id.kelas);

        btnSubmit = findViewById(R.id.btnSubmit);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String _nis = nis.getText().toString();
                String _nama = nama.getText().toString();
                String _kelas = kelas.getText().toString();

                if (_nis.equals("")){
                    nis.setError("required");
                }else  if (_nama.equals("")){
                    nama.setError("required");
                }else  if (_kelas.equals("")){
                    kelas.setError("required");
                }else {
                    JsonAddSiswa json = new JsonAddSiswa();
                    json.setNis(_nis);
                    json.setNama(_nama);
                    json.setKelas(_kelas);
                    addSiswa(json);

                }



            }
        });


    }


    private void addSiswa(JsonAddSiswa json){
        showProgress(true);
        Call<ResponseData> call = API.requestAddSiswa(json);
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                if(response.isSuccessful()) {
                    showProgress(false);
                    if (response.body().getMetadata() != null) {

                        String message = response.body().getMetadata().getMessage() ;
                        String status = response.body().getMetadata().getCode() ;

                        if(status.equals(Constant.ERR_200)){
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                            //sessionManager.saveUser(Helper.ConvertResponseDataLoginToJson(response.body()));
                            //startActivity(new Intent(InputQuiz.this, MainActivity.class));
                            //finish();
                            refresh();

                        }else{

                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                else{
                    showProgress(false);
                    //Log.e("TAG", "onResponse: "+response.body().toString() );
                    Toast.makeText(mContext, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                showProgress(false);
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showProgress(Boolean bool){

        if (bool){
            rlprogress.setVisibility(View.VISIBLE);
        }else {
            rlprogress.setVisibility(View.GONE);
        }
    }


    private void refresh(){
        nis.setText("");
        nama.setText("");
        kelas.setText("");
    }


}