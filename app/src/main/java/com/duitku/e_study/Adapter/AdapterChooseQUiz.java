package com.duitku.e_study.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.duitku.e_study.Constant.Constant;
import com.duitku.e_study.Menu.DetailMateri;
import com.duitku.e_study.Menu.Quiz;
import com.duitku.e_study.Model.Data.DataListMateri;
import com.duitku.e_study.R;
import com.duitku.e_study.Session.SessionManager;

import java.util.List;

import butterknife.ButterKnife;


public class AdapterChooseQUiz extends RecyclerView.Adapter< AdapterChooseQUiz.AdapterHolder>{

    private List<DataListMateri> AllMateriList;
    private Context mContext;

    private SessionManager sessionManager ;


    public AdapterChooseQUiz(Context context, List<DataListMateri> reportList){
        this.mContext = context;
        AllMateriList = reportList;
        sessionManager = new SessionManager(mContext);

    }

    @Override
    public AdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_materi, parent, false);
        return new AdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdapterHolder holder, int position) {
        final DataListMateri report = AllMateriList.get(position);

       String idmateri = report.getId_materi();
       String materi = report.getMateri();
       String title = report.getTitle();
       String img_url = report.getImg_url();


       holder.texttitle.setText(title);

        Glide.with(mContext).load(Constant.BASE_URL_IMAGE_MATERI+img_url).into(holder.imgMateri);

       holder.cvMateri.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i = new Intent(mContext , Quiz.class);
               i.putExtra("idMateri", idmateri);
               mContext.startActivity(i);
           }
       });

    }

    @Override
    public int getItemCount() {
        return AllMateriList.size();
    }

    public class AdapterHolder extends RecyclerView.ViewHolder{

        TextView texttitle ;
        ImageView imgMateri ;
        CardView cvMateri ;

        public AdapterHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            texttitle = itemView.findViewById(R.id.titleMateri);
            imgMateri = itemView.findViewById(R.id.imgMateri);
            cvMateri = itemView.findViewById(R.id.cvMateri);


        }
    }






}
