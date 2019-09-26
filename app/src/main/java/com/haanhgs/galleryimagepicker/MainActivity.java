package com.haanhgs.galleryimagepicker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    private ImageView ivImage;
    private TextView tvUri;
    private TextView tvReal;


    static Bitmap decodeUri(Context context, Uri uri, final int size)throws FileNotFoundException{
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(
                context.getContentResolver().openInputStream(uri), null, options);
        int widthTemp = options.outWidth;
        int heightTemp = options.outHeight;
        int scale = 1;

        while (widthTemp / 2 >= size && heightTemp / 2 >= size) {
            widthTemp /= 2;
            heightTemp /= 2;
            scale *= 2;
        }
        BitmapFactory.Options newOptions = new BitmapFactory.Options();
        newOptions.inSampleSize = scale;
        return BitmapFactory.decodeStream(
                context.getContentResolver().openInputStream(uri), null, newOptions);
    }

    static String getRealPathFromUri(Context context, Uri uri){
        Cursor cursor = null;
        try{
            String[]temp = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(uri, temp, null, null,null);
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
