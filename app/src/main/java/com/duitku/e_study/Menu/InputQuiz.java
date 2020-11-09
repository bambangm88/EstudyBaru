package com.duitku.e_study.Menu;

import android.content.Context;
import android.content.Intent;
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
import com.duitku.e_study.MainActivity;
import com.duitku.e_study.Model.json.JsonQuiz;
import com.duitku.e_study.Model.response.ResponseData;
import com.duitku.e_study.R;
import com.duitku.e_study.Session.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InputQuiz extends AppCompatActivity {

    private EditText soal , jawaban_a , jawaban_b , jawaban_c , jawaban_d;
    private Spinner jawaban_soal ;
    private Button btnSubmit ;
    private ApiService API;
    private Context mContext ;
    private SessionManager sessionManager ;
    private RelativeLayout rlprogress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_quiz);

        API = Server.getAPIService();
        mContext = this ;
        sessionManager = new SessionManager(mContext);

        soal = findViewById(R.id.soal);
        jawaban_a = findViewById(R.id.jawaban_a);
        jawaban_b = findViewById(R.id.jawaban_b);
        jawaban_c = findViewById(R.id.jawaban_c);
        jawaban_d = findViewById(R.id.jawaban_d);
        jawaban_soal = findViewById(R.id.spinner);
        rlprogress = findViewById(R.id.rlprogress);
        btnSubmit = findViewById(R.id.btnSubmit);

        Bundle bundle=getIntent().getExtras();
        String idmateri = bundle.getString("idMateri");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String _soal = soal.getText().toString();
                String  _jawaban_a = jawaban_a.getText().toString();
                String _jawaban_b = jawaban_b.getText().toString();
                String _jawaban_c = jawaban_c.getText().toString();
                String _jawaban_d = jawaban_d.getText().toString();
                String _jawaban_soal = jawaban_soal.getSelectedItem().toString();

                if (_soal.equals("")){
                    soal.setError("required");
                }else  if (_jawaban_a.equals("")){
                    jawaban_a.setError("required");
                }else  if (_jawaban_b.equals("")){
                    jawaban_b.setError("required");
                }else  if (_jawaban_c.equals("")){
                    jawaban_c.setError("required");
                }else  if (_jawaban_d.equals("")){
                    jawaban_d.setError("required");
                }else{

                    JsonQuiz jsonQuiz = new JsonQuiz();
                    jsonQuiz.setId_materi(idmateri);
                    jsonQuiz.setSoal(_soal);
                    jsonQuiz.setJawaban_a(_jawaban_a);
                    jsonQuiz.setJawaban_b(_jawaban_b);
                    jsonQuiz.setJawaban_c(_jawaban_c);
                    jsonQuiz.setJawaban_d(_jawaban_d);
                    jsonQuiz.setJawaban_isi(_jawaban_soal);
                    addQuiz(jsonQuiz);

                }




            }
        });




    }

    private void addQuiz(JsonQuiz jsonQuiz){
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
        soal.setText("");
        jawaban_a.setText("");
        jawaban_b.setText("");
        jawaban_c.setText("");
        jawaban_d.setText("");
    }




}
