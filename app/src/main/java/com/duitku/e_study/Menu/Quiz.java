package com.duitku.e_study.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.kalert.KAlertDialog;
import com.duitku.e_study.Adapter.AdapterListQuiz;
import com.duitku.e_study.Api.ApiService;
import com.duitku.e_study.Api.Server;
import com.duitku.e_study.Auth.ChangePassword;
import com.duitku.e_study.Auth.RegisterActivity;
import com.duitku.e_study.Constant.Constant;
import com.duitku.e_study.MenuUtama;
import com.duitku.e_study.Model.Data.DataListQuiz;
import com.duitku.e_study.Model.Data.DataLogin;
import com.duitku.e_study.Model.json.JsonListQuiz;
import com.duitku.e_study.Model.json.JsonLogin;
import com.duitku.e_study.Model.json.JsonQuiz;
import com.duitku.e_study.Model.json.JsonScore;
import com.duitku.e_study.Model.response.ResponseData;
import com.duitku.e_study.Model.response.ResponseListQuiz;
import com.duitku.e_study.R;
import com.duitku.e_study.Service.SoundService;
import com.duitku.e_study.Service.SoundServiceTiga;
import com.duitku.e_study.Session.SessionManager;
import com.duitku.e_study.Util.Helper;

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
    private Button btnNilai ;

    private RelativeLayout rlprogress;
    private SessionManager sessionManager ;

    private ArrayList<String> arrayJawaban = new ArrayList<>();
    private String idmateri ;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mContext = this ;
        SessionManager session = new SessionManager(mContext);
        DataLogin user = Helper.DecodeFromJsonResponseLogin(session.getInstanceUser());

        if (!user.getLevel().equals("SISWA")) {

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

        }

        Bundle bundle=getIntent().getExtras();
         idmateri = bundle.getString("idMateri");

        PlayBackgroundSound();

        API = Server.getAPIService();

        rlprogress = findViewById(R.id.rlprogress);
        btnNilai = findViewById(R.id.btnNilai);
        sessionManager = new SessionManager(this);

        rvQuiz = findViewById(R.id.rvQuiz);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvQuiz.setLayoutManager(mLayoutManager);
        rvQuiz.setItemAnimator(new DefaultItemAnimator());

        Adapter = new AdapterListQuiz(this,AllQuizList);
        JsonListQuiz json = new JsonListQuiz();
        json.setId_materi(idmateri);
        listQuiz(json);

        btnNilai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int checked = 0;
                arrayJawaban.clear();
                for (int i = 0; i < rvQuiz.getChildCount(); i++) {

                    try {
                        RadioGroup rb = rvQuiz.getChildAt(i).findViewById(R.id.opsi);
                        // Check which radio button was clicked
                        int checkedRadioButtonId = rb.getCheckedRadioButtonId();
                        if (checkedRadioButtonId == -1) {
                            // No item selected

                        }
                        else{
                            if (checkedRadioButtonId == R.id.jawaban_a) {
                                // Do something with the button
                                checked ++ ;

                            }

                            if (checkedRadioButtonId == R.id.jawaban_b) {
                                // Do something with the button
                                checked ++ ;
                            }

                            if (checkedRadioButtonId == R.id.jawaban_c) {
                                // Do something with the button
                                checked ++ ;
                            }

                            if (checkedRadioButtonId == R.id.jawaban_d) {
                                // Do something with the button
                                checked ++ ;
                            }
                        }



                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }

                if (checked != rvQuiz.getChildCount()){

                    new KAlertDialog(Quiz.this, KAlertDialog.WARNING_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Silahkan Jawab Semua Soal")
                            .setConfirmText("OK") //Do not call this if you don't want to show confirm button
                            .show();



                    //Toast.makeText(Quiz.this, "Silahkan Jawab Semua Soal " + rvQuiz.getChildCount(), Toast.LENGTH_SHORT).show();
                }else{

                    for (int i = 0; i < rvQuiz.getChildCount(); i++) {

                        try {
                            LinearLayout cv = rvQuiz.getChildAt(i).findViewById(R.id.cvJawaban);
                            LinearLayout llbg = rvQuiz.getChildAt(i).findViewById(R.id.LLBG);
                            TextView jawaban = rvQuiz.getChildAt(i).findViewById(R.id.jawaban);
                            arrayJawaban.add(""+jawaban.getTag());
                            if (jawaban.getTag().equals("BENAR")){
                                llbg.setBackgroundColor(Color.GREEN);
                            }else{
                                llbg.setBackgroundColor(Color.RED);
                            }

                            cv.setVisibility(View.VISIBLE);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }

                    int countBenar = 0 ;
                    for (String jawab : arrayJawaban){

                        if (jawab.equals("BENAR")){
                            countBenar ++;
                        }

                    }
                    int countSoal = rvQuiz.getChildCount() ;
                    int score =  (  countBenar * 100 ) / countSoal;
                    int countSalah = countSoal - countBenar ;

                    SessionManager session = new SessionManager(Quiz.this);
                    DataLogin user = Helper.DecodeFromJsonResponseLogin(session.getInstanceUser());

                    JsonScore json = new JsonScore();
                    json.setId_materi(idmateri);
                    json.setNis(user.getNis());
                    json.setSoal_total(""+countSoal);
                    json.setSoal_benar(""+countBenar);
                    json.setSoal_salah(""+countSalah);
                    json.setScore(""+score);
                    addScore(json);


                    //Toast.makeText(Quiz.this, "Terjawab Benar " + countBenar, Toast.LENGTH_SHORT).show();


                }

            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar_pay);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }









    private void listQuiz(JsonListQuiz json){
        btnNilai.setVisibility(View.GONE);
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
                            btnNilai.setVisibility(View.VISIBLE);
                            AllQuizList.addAll(response.body().getResponse().getData());
                            rvQuiz.setAdapter(new AdapterListQuiz(mContext, AllQuizList));
                            Adapter.notifyDataSetChanged();

                        }else{
                            btnNilai.setVisibility(View.GONE);
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        }


                    }else{
                        btnNilai.setVisibility(View.GONE);
                        Toast.makeText(mContext, "Error Response Data", Toast.LENGTH_LONG).show();
                    }

                }else{
                    btnNilai.setVisibility(View.GONE);
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.addquiz) {
            Bundle bundle=getIntent().getExtras();
            String idmateri = bundle.getString("idMateri");
            Intent i = new Intent(Quiz.this, InputQuiz.class);
            i.putExtra("idMateri", idmateri);
            startActivity(i);
            return true;
        }

        if (id == R.id.downloadScore) {

            downloadScore();

            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here

                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void addScore(JsonScore jsonScore){
        showProgress(true);
        Call<ResponseData> call = API.requestAddScore(jsonScore);
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                if(response.isSuccessful()) {
                    showProgress(false);
                    if (response.body().getMetadata() != null) {

                        String message = response.body().getMetadata().getMessage() ;
                        String status = response.body().getMetadata().getCode() ;

                        if(status.equals(Constant.ERR_200)){

                            new KAlertDialog(Quiz.this, KAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Selamat !")
                                    .setContentText("Score : " + jsonScore.getScore())
                                    .setCancelText("Kembali Ujian")
                                    .setConfirmText("OK") //Do not call this if you don't want to show confirm button
                                    .showCancelButton(true)
                                    .setCancelClickListener(new KAlertDialog.KAlertClickListener() {
                                        @Override
                                        public void onClick(KAlertDialog sDialog) {
                                            sDialog.cancel();
                                            Intent i = new Intent(mContext , Quiz.class);
                                            i.putExtra("idMateri", idmateri);
                                            mContext.startActivity(i);
                                            finish();
                                        }
                                    })
                                    .show();


                            //Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
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




    private void downloadScore(){
        showProgress(true);
        SessionManager session = new SessionManager(Quiz.this);
        DataLogin user = Helper.DecodeFromJsonResponseLogin(session.getInstanceUser());
        JsonLogin Json = new JsonLogin();
        Json.setNis(user.getNis());

        Call<ResponseData> call = API.requestDownloadScore(Json);
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                if(response.isSuccessful()) {
                    showProgress(false);
                    if (response.body().getMetadata() != null) {

                        String message = response.body().getMetadata().getMessage() ;
                        String status = response.body().getMetadata().getCode() ;


                        if(status.equals(Constant.ERR_200)){
                            String url= response.body().getMetadata().getDownloadUrl();

                            downloadManager(url);

                            //Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
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


    private void downloadManager(String url){

        Log.e("TAG", "downloadManager: "+Constant.BASE_URL_DOWNLOAD+url );

        DownloadManager downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(Constant.BASE_URL_DOWNLOAD+url);

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("My File");
        request.setDescription("Downloading");//request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,url);
        //request.setDestinationUri(Uri.parse("file://" + "MTN" + "/"+url));
        downloadmanager.enqueue(request);
    }

    public void PlayBackgroundSound() {
        Intent intent = new Intent(Quiz.this, SoundServiceTiga.class);
        startService(intent);
    }


}
