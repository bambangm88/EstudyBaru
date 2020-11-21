package com.duitku.e_study.Util;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.duitku.e_study.Model.Data.DataLogin;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Helper {



    public static void setTitleDate(TextView tanggalBulan , TextView hari , TextView tahun ){

        String date = new SimpleDateFormat("dd MMMM", Locale.getDefault()).format(new Date());
        String day = new SimpleDateFormat("EEEE", Locale.getDefault()).format(new Date());
        String year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());

        tanggalBulan.setText(date);
        hari.setText(day + ",");
        tahun.setText(year);

    }

    public static String getStringImage(Bitmap bmp) {
        int bitmap_size = 60; // range 1 - 100
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public static String EncriptToBase64(String Text){
        return Base64.encodeToString((Text).getBytes(), Base64.NO_WRAP);
    }

    public static String Hash_SHA256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }







    public static String changeToRupiah(String text)
    {
        String rp = text.replace(".00","");
        //conversi currency
        int number = Integer.parseInt(rp);
        String currency = NumberFormat.getNumberInstance(Locale.US).format(number);
        return "Rp "+currency;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String changeDate(String text){

        DateTimeFormatter parser = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy", Locale.ENGLISH);
        System.out.println(formatter.format(parser.parse( text))); // Monday, July 9, 2018

        return "";
    }


    public static void writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    public static Bitmap cropToSquare(Bitmap bitmap){
        int width  = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width)? height - ( height - width) : height;
        int cropW = (width - height) / 2;
        cropW = (cropW < 0)? 0: cropW;
        int cropH = (height - width) / 2;
        cropH = (cropH < 0)? 0: cropH;
        Bitmap cropImg = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);

        return cropImg;
    }


    public static String ConvertResponseDataLoginToJson(DataLogin model) {
        Gson gson = new Gson();
        String stringUser = gson.toJson(model);
        return stringUser;
    }



    public static DataLogin DecodeFromJsonResponseLogin (String json){
        String  jsonString =json; //http request
        DataLogin data =new DataLogin() ;
        Gson gson = new Gson();
        data= gson.fromJson(jsonString,DataLogin.class);
        return data ;
    }



}
