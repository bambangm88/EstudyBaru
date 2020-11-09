package com.duitku.e_study.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.duitku.e_study.Adapter.AdapterListQuiz;
import com.duitku.e_study.Api.ApiService;
import com.duitku.e_study.Api.Server;
import com.duitku.e_study.Constant.Constant;
import com.duitku.e_study.Model.Data.DataListQuiz;
import com.duitku.e_study.Model.json.JsonListQuiz;
import com.duitku.e_study.Model.response.ResponseListQuiz;
import com.duitku.e_study.R;
import com.duitku.e_study.Session.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Quiz extends AppCompatActivity {



    private RecyclerView rvQuiz;
    private List<DataListQuiz> AllQuizList = new ArrayList<>();
    private ApiService API;
    private Context mContext;

    private AdapterListQuiz Adapter;

    private LinearLayout btnStartDate , btnEndDate ;

    private EditText etStartDate , etEndDate ;

    private Button btnCari ;

    private RelativeLayout rlprogress;
    private SessionManager sessionManager ;
    
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle=getIntent().getExtras();
        String idmateri = bundle.getString("idMateri");

        mContext = this ;
        API = Server.getAPIService();

        rlprogress = findViewById(R.id.rlprogress);
        sessionManager = new SessionManager(this);

        rvQuiz = findViewById(R.id.rvQuiz);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvQuiz.setLayoutManager(mLayoutManager);
        rvQuiz.setItemAnimator(new DefaultItemAnimator());

        Adapter = new AdapterListQuiz(this,AllQuizList);
        JsonListQuiz json = new JsonListQuiz();
        json.setId_materi(idmateri);
        listQuiz(json);
        
    }









    private void listQuiz(JsonListQuiz json){

        showProgress(true);
        Call<ResponseListQuiz> call = API.requestListQuiz(json);
        call.enqueue(new Callback<ResponseListQuiz>() {
            @Override
            public void onResponse(Call<ResponseListQuiz> call, Response<ResponseListQuiz> response) {
                if(response.isSuccessful()) {
                    showProgress(false);
                    if (response.body().getMetadata() != null) {

                        String message = response.body().getMetadata().getMessage() ;
                        String status = response.body().getMetadata().getCode() ;

                        if(status.equals(Constant.ERR_200)) {

                            AllQuizList.addAll(response.body().getResponse().getData());
                            rvQuiz.setAdapter(new AdapterListQuiz(mContext, AllQuizList));
                            Adapter.notifyDataSetChanged();

                        }else{
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        }


                    }else{

                        Toast.makeText(mContext, "Error Response Data", Toast.LENGTH_LONG).show();
                    }

                }else{
                    showProgress(false);
                    Toast.makeText(mContext, "Error Response Data", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseListQuiz> call, Throwable t) {
                showProgress(false);
                Toast.makeText(mContext, "Internal server error / check your connection", Toast.LENGTH_SHORT).show();
                Log.e("Error", "onFailure: "+t.getMessage() );
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
