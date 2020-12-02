package com.duitku.e_study.Menu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.duitku.e_study.Api.ApiService;
import com.duitku.e_study.Api.Server;
import com.duitku.e_study.BuildConfig;
import com.duitku.e_study.Constant.Constant;
import com.duitku.e_study.Model.json.JsonMateri;
import com.duitku.e_study.Model.response.ResponseData;
import com.duitku.e_study.R;
import com.duitku.e_study.Session.SessionManager;
import com.duitku.e_study.Util.Helper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditMateri extends AppCompatActivity {

    private EditText _title , _materi ;

    private Button btnSubmit ;
    private ApiService API;
    private Context mContext ;
    private SessionManager sessionManager ;
    private RelativeLayout rlprogress;
    private ImageView ivImage ;
    private int GALLERY = 4, CAMERA = 2;
    static final int REQUEST_TAKE_PHOTO = 2;
    String currentPhotoPath;
    private static String imageID  = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_materi);

        API = Server.getAPIService();
        mContext = this ;
        sessionManager = new SessionManager(mContext);

        _title = findViewById(R.id.title);
        _materi = findViewById(R.id.materi);

        rlprogress = findViewById(R.id.rlprogress);
        btnSubmit = findViewById(R.id.btnSubmit);
        
        ivImage = findViewById(R.id.ivimage);

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                showPictureDialog();
                //takePhotoFromCamera();
            }
        });

        Helper.requestMultiplePermissions(this);

        Bundle bundle=getIntent().getExtras();
        String XidMateri = bundle.getString("id_materi");
        String Xtitle = bundle.getString("title");
        String Xmateri = bundle.getString("materi");
        String Xurl = bundle.getString("url");

        imageID = "ISI";
        Glide.with(mContext).load(Constant.BASE_URL_IMAGE_MATERI+Xurl).into(ivImage);
        _materi.setText(Xmateri);
        _title.setText(Xtitle);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String title	= _title.getText().toString();
                String materi 		= _materi.getText().toString();
                String image = "" ;

                //check image is include
                if (imageID.equals("ISI")) {
                    image = Helper.getStringImage(Helper.imageView2Bitmap(ivImage));
                }
                if (title.equals("")){
                    _title.setError("REQUIRED");
                }else if (materi.equals("")){
                    _materi.setError("REQUIRED");
                }else if (image.equals("")){
                    Toast.makeText(mContext, "Pilih Image", Toast.LENGTH_SHORT).show();
                }else{

                    JsonMateri json = new JsonMateri();
                    json.setId_materi(XidMateri);
                    json.setMateri(materi);
                    json.setTitle_materi(title);
                    json.setImage_string(image);
                    EditMateri(json);

                }



            }
        });



        Toolbar toolbar = findViewById(R.id.toolbar_pay);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    private void takePhotoFromCamera() {
        // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(intent, CAMERA);

        dispatchTakePictureIntent();

    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(mContext,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == 0) {
            return;
        }

    


        if (requestCode == CAMERA) {
            //Bitmap thumbnail = (Bitmap) data.getExtras().get("data");


            // Get the dimensions of the View
            int targetW = 1000;
            int targetH = 1000;

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;

            BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

            ExifInterface exif = null;
            try {
                exif = new ExifInterface(currentPhotoPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            Bitmap bmRotated = Helper.rotateBitmap(bitmap, orientation);

            // addStampToImage(bmRotated);
           // bmRotated = Helper.waterMark(bmRotated, Helper.getDateTimeNow(),  Color.RED, 90, 90, true);

                ivImage.setImageBitmap(bmRotated);
                imageID = "ISI" ;

        }

        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {

                    Bitmap thumbnail  = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), contentURI);


                    ivImage.setImageBitmap(thumbnail);
                    imageID = "ISI";
                


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        }





    }


    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(mContext);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select berkas from gallery",
                "Capture berkas from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }



    private void EditMateri(JsonMateri json){
        showProgress(true);
        Call<ResponseData> call = API.requestEditMateri(json);
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                if(response.isSuccessful()) {
                    showProgress(false);
                    if (response.body().getMetadata() != null) {

                        String message = response.body().getMetadata().getMessage() ;
                        String status = response.body().getMetadata().getCode() ;

                        if(status.equals(Constant.ERR_200)){

                            refresh();
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                        }else{

                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                else{
                    showProgress(false);
                    //Log.e("TAG", "onResponse: "+response.body().toString() );
                    Toast.makeText(mContext, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                showProgress(false);
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showProgress(Boolean bool){

        if (bool){
            rlprogress.setVisibility(View.VISIBLE);
        }else {
            rlprogress.setVisibility(View.GONE);
        }
    }

    private void refresh(){

        _title.setText("");
        _materi.setText("");
        ivImage.setImageResource(R.drawable.emptyimage);
        imageID = "" ;

    }




}
