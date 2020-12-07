package com.duitku.e_study.Menu;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.duitku.e_study.Api.ApiService;
import com.duitku.e_study.Api.Server;
import com.duitku.e_study.BuildConfig;
import com.duitku.e_study.Constant.Constant;
import com.duitku.e_study.Model.json.JsonMateri;
import com.duitku.e_study.Model.json.JsonQuiz;
import com.duitku.e_study.Model.response.ResponseData;
import com.duitku.e_study.R;
import com.duitku.e_study.Session.SessionManager;
import com.duitku.e_study.Util.FilePath;
import com.duitku.e_study.Util.Helper;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64OutputStream;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class InputMateri extends AppCompatActivity {

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
    TextView txtFilename ;

    private static final int PDF_REQUEST=777;
    private Bitmap bitmap;

    public int PDF_REQ_CODE = 1;

    String PdfNameHolder, PdfPathHolder, PdfID;
    Uri uri;

    Button choosePDF;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_materi);

        API = Server.getAPIService();
        mContext = this ;
        sessionManager = new SessionManager(mContext);

        _title = findViewById(R.id.title);
        _materi = findViewById(R.id.materi);

        rlprogress = findViewById(R.id.rlprogress);
        btnSubmit = findViewById(R.id.btnSubmit);
        txtFilename = findViewById(R.id.txtfilename);

        ivImage = findViewById(R.id.ivimage);

        choosePDF = findViewById(R.id.choosePDF);

        choosePDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //selectImage();
                selectPdf();
            }
        });



        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                showPictureDialog();
                //takePhotoFromCamera();
            }
        });

        Helper.requestMultiplePermissions(this);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String title	= _title.getText().toString();
                String materi 		= _materi.getText().toString();
                String image = "" ;

                //check image is include
                if (ivImage.getTag().toString().equals("ISI")) {
                    image = Helper.getStringImage(Helper.imageView2Bitmap(ivImage));
                }

                if (title.equals("")){
                    _title.setError("REQUIRED");
                }else if (uri == null){
                    Toast.makeText(mContext, "Pilih PDF Materi", Toast.LENGTH_SHORT).show();
                }
                else if (image.equals("")){
                    Toast.makeText(mContext, "Pilih Gambar", Toast.LENGTH_SHORT).show();
                }
                else{

                   /* JsonMateri json = new JsonMateri();
                    json.setMateri(materi);
                    json.setTitle_materi(title);
                    json.setImage_string(image);
                    addMateri(json);*/

                   PdfUploadFunction(title,image);


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
                ivImage.setTag("ISI");

        }

        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {

                    Bitmap thumbnail  = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), contentURI);


                    ivImage.setImageBitmap(thumbnail);
                    ivImage.setTag("ISI");
                


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        }



        if (requestCode == PDF_REQ_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();
            File file= new File(uri.getPath());


            txtFilename.setText(""+file.getName());

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



    private void addMateri(JsonMateri json){
        showProgress(true);
        Call<ResponseData> call = API.requestAddMateri(json);
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
        ivImage.setTag("");


    }


    private void selectPdf()
    {
        Intent intent = new Intent();

        intent.setType("application/pdf");

        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PDF_REQ_CODE);
    }


    public void AllowRunTimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE))
        {

            Toast.makeText(this,"READ_EXTERNAL_STORAGE permission Access Dialog", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] Result) {

        switch (RC) {

            case 1:

                if (Result.length > 0 && Result[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this,"Permission Granted", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(this,"Permission Canceled", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    private void uploadPDF() throws MalformedURLException, FileNotFoundException {
        PdfID = UUID.randomUUID().toString();
        PdfPathHolder = FilePath.getPath(this, uri);


        MultipartUploadRequest uploadRequest = null;

            uploadRequest = new MultipartUploadRequest(this, PdfID, Constant.BASE_URL_API+"siswa/addMateri")
                    .addFileToUpload(PdfPathHolder, "pdf")
                    .addParameter("name", PdfNameHolder)
                    //.addParameter("username",username)
                    .setMaxRetries(5);


// For Android > 8, we need to set an Channel to the UploadNotificationConfig.
// So, here, we create the channel and set it to the MultipartUploadRequest
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("Upload", "Upload service", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);

            UploadNotificationConfig notificationConfig = new UploadNotificationConfig();
            notificationConfig.setNotificationChannelId("Upload");

            uploadRequest.setNotificationConfig(notificationConfig);
        } else {
            // If android < Oreo, just set a simple notification (or remove if you don't wanna any notification
            // Notification is mandatory for Android > 8
            uploadRequest.setNotificationConfig(new UploadNotificationConfig());
        }

        uploadRequest.startUpload();







    }


    public void PdfUploadFunction(String title , String image) {

        PdfPathHolder = FilePath.getPath(this, uri);

        //File file=new File(uri.getPath());
        RequestBody Title=RequestBody.create(MediaType.parse("text/plain"),title);
        RequestBody Pdf=RequestBody.create(MediaType.parse("application/pdf"), PdfPathHolder);



        //Create a file object using file path
        File file = new File(uri.getPath());
        // Parsing any Media type file
      //  RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), PdfPathHolder);
      //  MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("filename", file.getName(), requestBody);
      //  RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
      //  RequestBody _title = RequestBody.create(MediaType.parse("text/plain"), title);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/pdf"), PdfPathHolder);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        RequestBody _title = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody _image = RequestBody.create(MediaType.parse("text/plain"), image);


        ApiService API_ = Server.getAPIServicePDF();
        showProgress(true);
        Call<ResponseData> call = API_.PdfUploadFunction(fileToUpload,filename,_title , _image);
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















}
