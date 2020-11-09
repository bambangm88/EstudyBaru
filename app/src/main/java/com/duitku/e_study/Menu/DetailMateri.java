package com.duitku.e_study.Menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.duitku.e_study.R;

public class DetailMateri extends AppCompatActivity {

    private ImageView imgMateri ;
    private TextView materi , title ;
    private TextView  addQuiz;
    private Button btnQuiz , Quiz ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_materi);

        imgMateri = findViewById(R.id.imgMateri);
        materi = findViewById(R.id.materi);
        title = findViewById(R.id.title);
        addQuiz = findViewById(R.id.addQuiz);
        Quiz = findViewById(R.id.btnQuiz);

        Bundle bundle=getIntent().getExtras();
        String idmateri = bundle.getString("idMateri");
        String _materi = bundle.getString("materi");
        String _title = bundle.getString("title");
        String imgUrl = bundle.getString("img_url");

        materi.setText(_materi);
        title.setText(_title);
        Glide.with(this).load(imgUrl).into(imgMateri);

        addQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailMateri.this, InputQuiz.class);
                i.putExtra("idMateri", idmateri);
                 startActivity(i);
            }
        });

        Quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailMateri.this, Quiz.class);
                i.putExtra("idMateri", idmateri);
                startActivity(i);
            }
        });


    }
}
