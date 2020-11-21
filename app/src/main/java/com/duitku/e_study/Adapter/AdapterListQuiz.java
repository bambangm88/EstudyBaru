package com.duitku.e_study.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.duitku.e_study.Api.ApiService;
import com.duitku.e_study.Api.Server;
import com.duitku.e_study.Auth.ChangePassword;
import com.duitku.e_study.Constant.Constant;
import com.duitku.e_study.Menu.DetailMateri;
import com.duitku.e_study.Menu.EditQuiz;
import com.duitku.e_study.Menu.Quiz;
import com.duitku.e_study.Model.Data.DataListQuiz;
import com.duitku.e_study.Model.Data.DataLogin;
import com.duitku.e_study.Model.json.JsonQuiz;
import com.duitku.e_study.Model.response.ResponseData;
import com.duitku.e_study.R;
import com.duitku.e_study.Session.SessionManager;
import com.duitku.e_study.Util.Helper;

import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdapterListQuiz extends RecyclerView.Adapter< AdapterListQuiz.AdapterHolder>{

    private List<DataListQuiz> AllQuizList;
    private Context mContext;

    private SessionManager sessionManager ;


    public AdapterListQuiz(Context context, List<DataListQuiz> reportList){
        this.mContext = context;
        AllQuizList = reportList;
        sessionManager = new SessionManager(mContext);

    }

    @Override
    public AdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_quiz, parent, false);
        return new AdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdapterHolder holder, int position) {
        final DataListQuiz quiz = AllQuizList.get(position);


        String idQuiz = quiz.getId_quiz();
        String idMateri = quiz.getId_materi();
        String soal = quiz.getSoal();
        String jawaban_a = quiz.getJawaban_a();
        String jawaban_b = quiz.getJawaban_b();
        String jawaban_c = quiz.getJawaban_c();
        String jawaban_d = quiz.getJawaban_d();
        String jawaban_isi = quiz.getJawaban_isi();
        String imgUrl = quiz.getImage();

        if (!imgUrl.equals("")){
            holder.ivimage.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(Constant.BASE_URL_IMAGE_QUIZ+imgUrl).into(holder.ivimage);
        }


        holder.textsoal.setText(soal);
        holder.rbjawaban_a.setText("A. "+jawaban_a);
        holder.rbjawaban_b.setText("B. "+jawaban_b);
        holder.rbjawaban_c.setText("C. "+jawaban_c);
        holder.rbjawaban_d.setText("D. "+jawaban_d);


        holder.jawaban.setTag("SALAH");
        holder.jawaban.setText("Jawaban :"+jawaban_isi.toUpperCase());
        holder.rbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id){
                    case R.id.jawaban_a:
                        String isi = "A" ;
                        if (isi.equals(jawaban_isi.toUpperCase())){
                            holder.jawaban.setTag("BENAR");
                        }
                        break;
                    case R.id.jawaban_b:
                        String _isi = "B" ;
                        if (_isi.equals(jawaban_isi.toUpperCase())){
                            holder.jawaban.setTag("BENAR");
                        }
                        break;
                    case R.id.jawaban_c:
                        String __isi = "C" ;
                        if (__isi.equals(jawaban_isi.toUpperCase())){
                            holder.jawaban.setTag("BENAR");
                        }
                        break;
                    case R.id.jawaban_d:

                        String ___isi = "D" ;
                        if (___isi.equals(jawaban_isi.toUpperCase())){
                            holder.jawaban.setTag("BENAR");
                        }
                        break;
                }
            }
        });

        SessionManager session = new SessionManager(mContext);
        DataLogin user = Helper.DecodeFromJsonResponseLogin(session.getInstanceUser());

        if (!user.getLevel().equals("SISWA")) {
            holder.cvQuiz.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    //pass the 'context' here
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                    alertDialog.setTitle("Action");
                    alertDialog.setMessage("");
                    alertDialog.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Intent i = new Intent(mContext, EditQuiz.class);
                            i.putExtra("idQuiz", idQuiz);
                            i.putExtra("idMateri", idMateri);
                            i.putExtra("soal", soal);
                            i.putExtra("jawaban_a", jawaban_a);
                            i.putExtra("jawaban_b", jawaban_b);
                            i.putExtra("jawaban_c", jawaban_c);
                            i.putExtra("jawaban_d", jawaban_d);
                            i.putExtra("jawaban_isi", jawaban_isi);
                            i.putExtra("image", imgUrl);
                            mContext.startActivity(i);
                        }
                    });
                    alertDialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // DO SOMETHING HERE
                            dialog.cancel();
                            JsonQuiz json = new JsonQuiz();
                            json.setId_quiz(idQuiz);
                            deleteQuiz(json, idQuiz);

                        }
                    });

                    AlertDialog dialog = alertDialog.create();
                    dialog.show();
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return AllQuizList.size();
    }

    public class AdapterHolder extends RecyclerView.ViewHolder{

        TextView textsoal ;
        RadioButton rbjawaban_a , rbjawaban_b , rbjawaban_c , rbjawaban_d ;
        RadioGroup rbGroup ;
        LinearLayout cvJawaban ;
        TextView jawaban ,jawabanX ;
        CardView cvQuiz ;
        ImageView ivimage ;

        public AdapterHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            textsoal = itemView.findViewById(R.id.soal);
            rbjawaban_a = itemView.findViewById(R.id.jawaban_a);
            rbjawaban_b = itemView.findViewById(R.id.jawaban_b);
            rbjawaban_c = itemView.findViewById(R.id.jawaban_c);
            rbjawaban_d = itemView.findViewById(R.id.jawaban_d);
            rbjawaban_d = itemView.findViewById(R.id.jawaban_d);
            cvJawaban = itemView.findViewById(R.id.cvJawaban);
            jawaban = itemView.findViewById(R.id.jawaban);
            jawabanX = itemView.findViewById(R.id.jawabanX);
            rbGroup = itemView.findViewById(R.id.opsi);
            cvQuiz = itemView.findViewById(R.id.cvQuiz);
            ivimage = itemView.findViewById(R.id.imgQuiz);

        }
    }


    private void deleteQuiz(JsonQuiz jsonQuiz, String idMateri){

        ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage("request . .");
        pd.show();

        ApiService API  = Server.getAPIService();
        Call<ResponseData> call = API.requestDeleteQuiz(jsonQuiz);
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                if(response.isSuccessful()) {
                    pd.cancel();
                    if (response.body().getMetadata() != null) {

                        String message = response.body().getMetadata().getMessage() ;
                        String status = response.body().getMetadata().getCode() ;

                        if(status.equals(Constant.ERR_200)){
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                            //sessionManager.saveUser(Helper.ConvertResponseDataLoginToJson(response.body()));
                            Intent i = new Intent(mContext , Quiz.class);
                            i.putExtra("idMateri", idMateri);
                            mContext.startActivity(i);
                            ((Activity)mContext).finish();
                        }else{
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                else{
                    pd.cancel();
                    //Log.e("TAG", "onResponse: "+response.body().toString() );
                    Toast.makeText(mContext, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                pd.cancel();
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }




}
