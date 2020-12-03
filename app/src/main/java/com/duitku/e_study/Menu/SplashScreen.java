package com.duitku.e_study.Menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.duitku.e_study.Auth.LoginActivity;
import com.duitku.e_study.MenuUtama;
import com.duitku.e_study.R;
import com.duitku.e_study.Service.SoundService;


/**
 * Created by PEACE on 3/30/2016.
 */
public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
        PlayBackgroundSound();
    }


    public void PlayBackgroundSound() {
        Intent intent = new Intent(SplashScreen.this, SoundService.class);
        startService(intent);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

}