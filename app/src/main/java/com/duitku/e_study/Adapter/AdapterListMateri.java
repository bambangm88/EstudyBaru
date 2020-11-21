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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.duitku.e_study.Api.ApiService;
import com.duitku.e_study.Api.Server;
import com.duitku.e_study.Constant.Constant;
import com.duitku.e_study.Menu.DetailMateri;
import com.duitku.e_study.Menu.Materi;
import com.duitku.e_study.Menu.Quiz;
import com.duitku.e_study.Model.Data.DataListMateri;
import com.duitku.e_study.Model.json.JsonMateri;
import com.duitku.e_study.Model.json.JsonQuiz;
import com.duitku.e_study.Model.response.ResponseData;
import com.duitku.e_study.R;
import com.duitku.e_study.Session.SessionManager;

import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdapterListMateri extends RecyclerView.Adapter< AdapterListMateri.AdapterHolder>{

    private List<DataListMateri> AllMateriList;
    private Context mContext;

    private SessionManager sessionManager ;


    public AdapterListMateri(Context context, List<DataListMateri> reportList){
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

       Glide.with(mContext).load(img_url).into(holder.imgMateri);

       holder.cvMateri.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i = new Intent(mContext, DetailMateri.class);
               i.putExtra("idMateri", idmateri);
               i.putExtra("title", title);
               i.putExtra("materi", materi);
               i.putExtra("img_url", img_url);
               mContext.startActivity(i);
           }
       });


       holder.cvMateri.setOnLongClickListener(new View.OnLongClickListener() {
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
                   }
               });
               alertDialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {

                       // DO SOMETHING HERE

                       dialog.cancel();
                       JsonMateri json = new JsonMateri();
                       json.setId_materi(idmateri);
                       deleteMateri(json,idmateri);

                   }
               });

               AlertDialog dialog = alertDialog.create();
               dialog.show();
               return false;
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



    private void deleteMateri(JsonMateri jsonQuiz, String idMateri){

        ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage("request . .");
        pd.show();

        ApiService API  = Server.getAPIService();
        Call<ResponseData> call = API.requestDeleteMateri(jsonQuiz);
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
                            Intent i = new Intent(mContext , Materi.class);
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
