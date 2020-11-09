package com.duitku.e_study.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.duitku.e_study.Model.Data.DataListQuiz;
import com.duitku.e_study.R;
import com.duitku.e_study.Session.SessionManager;

import java.util.List;

import butterknife.ButterKnife;


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


        holder.textsoal.setText(soal);
        holder.rbjawaban_a.setText("A. "+jawaban_a);
        holder.rbjawaban_b.setText("B. "+jawaban_b);
        holder.rbjawaban_c.setText("C. "+jawaban_c);
        holder.rbjawaban_d.setText("D. "+jawaban_d);


    }

    @Override
    public int getItemCount() {
        return AllQuizList.size();
    }

    public class AdapterHolder extends RecyclerView.ViewHolder{

        TextView textsoal ;
        RadioButton rbjawaban_a , rbjawaban_b , rbjawaban_c , rbjawaban_d ;

        public AdapterHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            textsoal = itemView.findViewById(R.id.soal);
            rbjawaban_a = itemView.findViewById(R.id.jawaban_a);
            rbjawaban_b = itemView.findViewById(R.id.jawaban_b);
            rbjawaban_c = itemView.findViewById(R.id.jawaban_c);
            rbjawaban_d = itemView.findViewById(R.id.jawaban_d);

        }
    }






}
