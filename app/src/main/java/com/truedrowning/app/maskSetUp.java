package com.truedrowning.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Objects;

public class maskSetUp extends AppCompatActivity {

    //////Paint initialize////
    ImageView placeHolder;
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint = new Paint();
    private float floatStartX = -1, floatStartY = -1,
            floatEndX = -1, floatEndY = -1;
    ///////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mask_set_up);

        placeHolder = findViewById(R.id.canvas);

        //downloadFileImage();
    }
    @Override
    protected void onResume() {
        super.onResume();

        downloadFileOImage();
        downloadFilePImage();
    }
    @Override
    protected void onStart() {
        super.onStart();

        ImageView imageView = findViewById(R.id.poolImage);

        String filePathImg = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/originalImage.jpg";
        saving maskUp = new saving();
        maskUp.importImage(this, filePathImg,imageView);

        //uploadFile();

        /*
        statusPage fileSearcher = new statusPage();
        File file = new File(getApplicationContext().getFilesDir(), fileSearcher.getTextFile());
        String filePath = file.getAbsolutePath();
        TextView myTextView = fileSearcher.getTextView();
        fileSearcher.searchFileForWords(filePath, myTextView);

         */



    }

    public void sendMask(View view){

        saving savingRef = new saving();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) placeHolder.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        savingRef.saveImageToExternalStorage(this,"mask", bitmap);
        //saveImageToExternalStorage("mask",bitmap);
        Toast.makeText(this, "Mask Sent Successfully", Toast.LENGTH_SHORT).show();

        uploadFile();
        //downloadFileImage();

    }
    public void save(View view){
        uploadFile();
        textRef();

    }
    /*
    private void uploadFile() {

        File uploadFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "mask.jpg");

        Amplify.Storage.uploadFile(
                "mask.jpg",
                uploadFile,
                result -> Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey()),
                storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
        );
    }

     */

    private void uploadFile() {
        File uploadFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "mask.jpg");

        Amplify.Storage.uploadFile(
                "mask.jpg",
                uploadFile,
                result -> Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey()),
                storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
        );
    }
    private boolean saveImageToExternalStorage(String imgName, Bitmap bmp){

        Uri ImageCollection = null;
        ContentResolver resolver = getContentResolver();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){

            ImageCollection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);

            Log.d("FilePath", "Value " + ImageCollection);

        }else {

            ImageCollection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, imgName + ".jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg");
        Uri imageUri = resolver.insert(ImageCollection, contentValues);

        try {

            OutputStream outputStream = resolver.openOutputStream(Objects.requireNonNull(imageUri));
            bmp.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            Objects.requireNonNull(outputStream);
            return true;

        }
        catch (Exception e){

            Toast.makeText(this,"Image not saved: \n" + e,Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        }

        return false;


    }

    public void downloadFileOImage(){

        Amplify.Storage.downloadFile(
                "image1.jpg",
                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/originalImage.jpg"),
                result -> Log.i("images", "Successfully downloaded: " + result.getFile().getName()),
                error -> Log.e("imagef",  "Download Failure", error)
        );


    }
    public void downloadFilePImage() {

        Amplify.Storage.downloadFile(
                "ProcessedImage.jpg",
                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/test.jpg"),
                result -> Log.i("images", "Successfully downloaded: " + result.getFile().getName()),
                error -> Log.e("imagef", "Download Failure", error)
        );
    }

    private void textRef(){
        String fileName = "download.txt";
        File file = new File(getApplicationContext().getFilesDir(), fileName);

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }

            bufferedReader.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /////////////drawing section begin////////////////
    private void drawPaintSketchImage(){

        if (bitmap == null){
            bitmap = Bitmap.createBitmap(placeHolder.getWidth(),
                    placeHolder.getHeight(),
                    Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            paint.setColor(Color.RED);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(30);
        }
        canvas.drawLine(floatStartX,
                floatStartY-220,
                floatEndX,
                floatEndY-220,
                paint);
        placeHolder.setImageBitmap(bitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            floatStartX = event.getX();
            floatStartY = event.getY();
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE){
            floatEndX = event.getX();
            floatEndY = event.getY();
            drawPaintSketchImage();
            floatStartX = event.getX();
            floatStartY = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_UP){
            floatEndX = event.getX();
            floatEndY = event.getY();
            drawPaintSketchImage();
        }
        return super.onTouchEvent(event);
    }

    public void eraser(View view){
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    public void largesize(View view){
        paint.setStrokeWidth(200);
    }

    public void smallsize(View view){
        paint.setStrokeWidth(70);
    }

    //////////drawing section end///////////////
}