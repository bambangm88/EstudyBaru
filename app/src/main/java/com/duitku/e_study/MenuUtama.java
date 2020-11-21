package com.duitku.e_study;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.duitku.e_study.Auth.ChangePassword;
import com.duitku.e_study.Auth.LoginActivity;
import com.duitku.e_study.Auth.RegisterActivity;
import com.duitku.e_study.Menu.About;
import com.duitku.e_study.Menu.ChooseQuiz;
import com.duitku.e_study.Menu.Materi;
import com.duitku.e_study.Session.SessionManager;

public class MenuUtama extends AppCompatActivity {


    private Button materi , keluar , kuis ,about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_utama);
        materi = findViewById(R.id.materi) ;
        keluar = findViewById(R.id.keluar) ;
        kuis = findViewById(R.id.kuis) ;
        about = findViewById(R.id.about) ;
        materi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuUtama.this, Materi.class));
            }
        });

        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        kuis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuUtama.this, ChooseQuiz.class));
            }
        });


        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuUtama.this, About.class));
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }







    private void logout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Logout ?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                SessionManager session = new SessionManager(MenuUtama.this);
                //logOut
                session.logoutUser();
                Intent intent = new Intent(MenuUtama.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
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
            startActivity(new Intent(MenuUtama.this, RegisterActivity.class));
            return true;
        }

        if (id == R.id.changePassword) {
            startActivity(new Intent(MenuUtama.this, ChangePassword.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }






}
