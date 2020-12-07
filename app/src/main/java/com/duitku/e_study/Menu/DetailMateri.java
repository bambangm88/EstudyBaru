package com.duitku.e_study.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.duitku.e_study.Constant.Constant;
import com.duitku.e_study.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailMateri extends AppCompatActivity {

    private ImageView imgMateri;
    private TextView materi, title;
    private TextView addQuiz;
    private Button btnQuiz, Quiz;
    private PDFView pdfView;
    private ProgressDialog mProgressDialog;

    String idmateri;
    String _materi;
    String _title ;
    String imgUrl ;
    String pdfUrl ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_materi);

        imgMateri = findViewById(R.id.imgMateri);
        materi = findViewById(R.id.materi);
        title = findViewById(R.id.title);
        addQuiz = findViewById(R.id.addQuiz);
        Quiz = findViewById(R.id.btnQuiz);

        Bundle bundle = getIntent().getExtras();
        idmateri = bundle.getString("idMateri");
        _materi = bundle.getString("materi");
        _title = bundle.getString("title");
        imgUrl = bundle.getString("img_url");
        pdfUrl = Constant.BASE_URL_PDF+bundle.getString("pdf_url");

        Log.e("TAG", "onCreate: "+pdfUrl );


        materi.setText(_materi);
        title.setText(_title);

        Glide.with(this).load(Constant.BASE_URL_IMAGE_MATERI + imgUrl).into(imgMateri);

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

        Toolbar toolbar = findViewById(R.id.toolbar_pay);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        // instantiate it within the onCreate method
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Downloading Materi");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        String fileName = pdfUrl.substring(pdfUrl.lastIndexOf('/') + 1);
        File file =new File(Environment.getExternalStorageDirectory(),fileName);

        if (file.exists()) {
            Uri pdfPath = Uri.fromFile(file);
            pdfView  =findViewById(R.id.pdfView);
            pdfView.fromUri(pdfPath)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .defaultPage(0)
                    .load();
        }else{
            // execute this when the downloader must be fired
            final DownloadTask downloadTask = new DownloadTask(this);
            downloadTask.execute(pdfUrl);
        }





    }


    //homeback
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here

                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();

                String fileName = sUrl[0].substring(sUrl[0].lastIndexOf('/') + 1);
                Log.e("TAG", "doInBackground: "+fileName);


                output = new FileOutputStream("/sdcard/"+fileName);


                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            mProgressDialog.cancel();
            if (result != null)
                Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
            context.startActivity(new Intent(context, PDFViewer.class).putExtra("idMateri","")
                    .putExtra("img_url","")
                    .putExtra("materi","")
                    .putExtra("img_url","")
                    .putExtra("pdf_url","")


            );

            ((Activity)context).finish();
        }


    }





}