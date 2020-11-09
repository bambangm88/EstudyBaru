package com.duitku.e_study;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.duitku.e_study.Adapter.AdapterListMateri;
import com.duitku.e_study.Api.ApiService;
import com.duitku.e_study.Api.Server;
import com.duitku.e_study.Auth.LoginActivity;
import com.duitku.e_study.Auth.RegisterActivity;
import com.duitku.e_study.Constant.Constant;
import com.duitku.e_study.Model.Data.DataListMateri;
import com.duitku.e_study.Model.response.ResponseListMateri;
import com.duitku.e_study.Session.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {




    private RecyclerView rvHistoriTrx;
    private List<DataListMateri> AllMateriList = new ArrayList<>();
    private ApiService API;
    private Context mContext;

    private AdapterListMateri Adapter;

    private LinearLayout btnStartDate , btnEndDate ;

    private EditText etStartDate , etEndDate ;

    private Button btnCari ;

    private RelativeLayout rlprogress;
    private SessionManager sessionManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = this ;
        API = Server.getAPIService();

        rlprogress = findViewById(R.id.rlprogress);
        sessionManager = new SessionManager(this);

        rvHistoriTrx = findViewById(R.id.rvHistoriTrx);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvHistoriTrx.setLayoutManager(new GridLayoutManager(this, 2));
        rvHistoriTrx.setItemAnimator(new DefaultItemAnimator());

        Adapter = new AdapterListMateri(this,AllMateriList);

        listMateri();




    }











    private void listMateri(){

        showProgress(true);

        Call<ResponseListMateri> call = API.listMateri();
        call.enqueue(new Callback<ResponseListMateri>() {
            @Override
            public void onResponse(Call<ResponseListMateri> call, Response<ResponseListMateri> response) {
                if(response.isSuccessful()) {
                    showProgress(false);
                    if (response.body().getMetadata() != null) {

                        String message = response.body().getMetadata().getMessage() ;
                        String status = response.body().getMetadata().getCode() ;

                        if(status.equals(Constant.ERR_200)) {

                            AllMateriList.addAll(response.body().getResponse().getData());
                            rvHistoriTrx.setAdapter(new AdapterListMateri(mContext, AllMateriList));
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
            public void onFailure(Call<ResponseListMateri> call, Throwable t) {
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.addsiswa) {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            return true;
        }

        if (id == R.id.keluar) {
            show_dialog(MainActivity.this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void show_dialog(Context context){

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Keluar ?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        sessionManager.logoutUser();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();

                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}
