package com.haanhgs.galleryimagepicker.model;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Repo {



    public static Bitmap decodeUri(Context ct, Uri uri, int size)throws FileNotFoundException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream inputStream = ct.getContentResolver().openInputStream(uri);
        BitmapFactory.decodeStream(inputStream, null, options);
        int tempWidth = options.outWidth;
        int tempHeight = options.outHeight;
        int scale = 1;
        while (tempWidth/2 >= size && tempHeight/2 >= size){
            tempHeight /= 2;
            tempWidth /= 2;
            scale *= 2;
        }
        BitmapFactory.Options optionsNew = new BitmapFactory.Options();
        optionsNew.inSampleSize = scale;
        InputStream inputStreamNew = ct.getContentResolver().openInputStream(uri);
        return BitmapFactory.decodeStream(inputStreamNew, null, optionsNew);
    }

    public static String getRealPathFromUri(Context ct, Uri uri){
        Cursor cursor = null;
        try{
            String[] projection = {MediaStore.Images.Media.DATA};
            cursor = ct.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null){
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(columnIndex);
            }else {
                return null;
            }
        }finally {
            if (cursor != null) cursor.close();
        }
    }

}
