package com.duitku.e_study.Auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.duitku.e_study.Api.ApiService;
import com.duitku.e_study.Api.Server;
import com.duitku.e_study.Constant.Constant;
import com.duitku.e_study.MenuUtama;
import com.duitku.e_study.Model.json.JsonLogin;
import com.duitku.e_study.Model.response.ResponseLogin;
import com.duitku.e_study.R;
import com.duitku.e_study.Session.SessionManager;
import com.duitku.e_study.Util.Helper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ApiService API;
    private Context mContext ;
    private SessionManager sessionManager ;
    private EditText etnis , etpwd ;
    private RelativeLayout rlprogress;
    private Button btnLogin ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        API = Server.getAPIService();
        mContext = this ;
        sessionManager = new SessionManager(mContext);

        btnLogin = findViewById(R.id.login);
        etnis = findViewById(R.id.nis);
        etpwd = findViewById(R.id.pin);
        rlprogress = findViewById(R.id.rlprogress);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nis = etnis.getText().toString();
                String pwd = etpwd.getText().toString();

                if (nis.equals("")){
                    etnis.setError("Required");
                }else if (pwd.equals("")){
                    etpwd.setError("Required");
                }else{
                    JsonLogin login = new JsonLogin();
                    login.setNis(nis);
                    login.setPwd(Helper.Hash_SHA256(pwd));
                    Login(login);
                }

            }
        });

        //if loggin
        if (sessionManager.isLoggedIn()){
            startActivity(new Intent(LoginActivity.this,MenuUtama.class));
            finish();
        }



    }


    private void Login(JsonLogin jsonLogin){
        showProgress(true);
        Call<ResponseLogin> call = API.requestLogin(jsonLogin);
        call.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {

                if(response.isSuccessful()) {
                    showProgress(false);
                    if (response.body().getMetadata() != null) {

                        String message = response.body().getMetadata().getMessage() ;
                        String status = response.body().getMetadata().getCode() ;

                        if(status.equals(Constant.ERR_200)){
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                            sessionManager.saveUser(Helper.ConvertResponseDataLoginToJson(response.body().getResponse().getData().get(0)));
                            startActivity(new Intent(LoginActivity.this, MenuUtama.class));
                            finish();

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
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
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