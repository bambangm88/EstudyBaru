package com.duitku.e_study.Auth;

import android.content.Context;
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
import com.duitku.e_study.Model.Data.DataLogin;
import com.duitku.e_study.Model.json.JsonAddSiswa;
import com.duitku.e_study.Model.json.JsonChangePassword;
import com.duitku.e_study.Model.response.ResponseData;
import com.duitku.e_study.R;
import com.duitku.e_study.Session.SessionManager;
import com.duitku.e_study.Util.Helper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity {


    private ApiService API;
    private Context mContext ;
    private SessionManager sessionManager ;
    private RelativeLayout rlprogress;
    private EditText oldPassword , newPassword , confirmPassword;
    private Button btnSubmit ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        API = Server.getAPIService();
        mContext = this ;
        sessionManager = new SessionManager(mContext);
        rlprogress = findViewById(R.id.rlprogress);

        oldPassword = findViewById(R.id.oldPassword);
        newPassword = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.confirmPassword);

        btnSubmit = findViewById(R.id.btnSubmit);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String _oldPassword = oldPassword.getText().toString();
                String _newPassword = newPassword.getText().toString();
                String _confirmPassword = confirmPassword.getText().toString();

                if (_oldPassword.equals("")){
                    oldPassword.setError("required");
                }else  if (_newPassword.equals("")){
                    newPassword.setError("required");
                }else  if (_confirmPassword.equals("")){
                    confirmPassword.setError("required");
                }else  if (!_confirmPassword.equals(_newPassword)){
                    Toast.makeText(mContext, "Confirm password tidak sama", Toast.LENGTH_SHORT).show();
                }else {
                    JsonChangePassword json = new JsonChangePassword();
                    SessionManager session = new SessionManager(ChangePassword.this);
                    DataLogin user = Helper.DecodeFromJsonResponseLogin(session.getInstanceUser());
                    json.setNis(user.getNis());
                    json.setPwdBaru(_newPassword);
                    json.setPwdlama(_oldPassword);
                    changePassword(json);

                }



            }
        });


    }


    private void changePassword(JsonChangePassword json){
        showProgress(true);
        Call<ResponseData> call = API.requestChangePassword(json);
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
                            //startActivity(new Intent(InputQuiz.this, Materi.class));
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
        oldPassword.setText("");
        newPassword.setText("");
        confirmPassword.setText("");
    }


}