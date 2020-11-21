package com.duitku.e_study.Util;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.duitku.e_study.Model.Data.DataLogin;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
    public static void  requestMultiplePermissions(Activity activity){
        Dexter.withActivity(activity)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            //Toast.makeText(activity, "Akses Diizinkan", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                            Toast.makeText(activity, "Akses Tidak Diizinkan", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }


                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(activity, "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    public static Bitmap imageView2Bitmap(ImageView view){
        Bitmap bitmap = ((BitmapDrawable)view.getDrawable()).getBitmap();
        return bitmap;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }


}
