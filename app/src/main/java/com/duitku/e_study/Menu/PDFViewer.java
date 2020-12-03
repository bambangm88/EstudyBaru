package com.duitku.e_study.Menu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.duitku.e_study.R;
import com.github.barteksc.pdfviewer.PDFView;

public class PDFViewer extends AppCompatActivity {

    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_d_f_viewer);


        pdfView  =findViewById(R.id.pdfView);
        pdfView.fromAsset("sample.pdf")
                .enableSwipe(true)
                .swipeHorizontal(false)
                .defaultPage(0)
                .load();


    }
}