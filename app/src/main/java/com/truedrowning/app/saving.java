package com.truedrowning.app;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.OutputStream;
import java.util.Objects;

public class saving extends Application {

    public boolean saveImageToExternalStorage(Context context, String imgName, Bitmap bmp){

        Uri ImageCollection = null;
        ContentResolver resolver = context.getContentResolver();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){

            ImageCollection = MediaStore.Downloads.EXTERNAL_CONTENT_URI;

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

            Toast.makeText(context,"Image not saved: \n" + e,Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        }

        return false;


    }


    public void importImage(Context context, String filePathImg, ImageView imageView) {
        Drawable myDrawable = Drawable.createFromPath(filePathImg);

        ImageView newImageView = new ImageView(context);
        newImageView.setImageDrawable(myDrawable);
        newImageView.setLayoutParams(imageView.getLayoutParams());

        ViewGroup parent = (ViewGroup) imageView.getParent();
        int index = parent.indexOfChild(imageView);
        parent.removeView(imageView);
        parent.addView(newImageView, index);
    }



}
