package com.duitku.e_study.Menu;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.duitku.e_study.Api.ApiService;
import com.duitku.e_study.Api.Server;
import com.duitku.e_study.Constant.Constant;
import com.duitku.e_study.Model.json.JsonQuiz;
import com.duitku.e_study.Model.response.ResponseData;
import com.duitku.e_study.R;
import com.duitku.e_study.Session.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InputMateri extends AppCompatActivity {

    private EditText title , materi ;

    private Button btnSubmit ;
    private ApiService API;
    private Context mContext ;
    private SessionManager sessionManager ;
    private RelativeLayout rlprogress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_materi);

        API = Server.getAPIService();
        mContext = this ;
        sessionManager = new SessionManager(mContext);

        title = findViewById(R.id.title);
        materi = findViewById(R.id.materi);

        rlprogress = findViewById(R.id.rlprogress);
        btnSubmit = findViewById(R.id.btnSubmit);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




            }
        });




    }

    private void addMateri(JsonQuiz jsonQuiz){
        showProgress(true);
        Call<ResponseData> call = API.requestAddQuiz(jsonQuiz);
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



}
