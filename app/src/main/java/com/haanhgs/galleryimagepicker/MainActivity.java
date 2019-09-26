package com.haanhgs.galleryimagepicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST = 999;
    private ImageView ivImage;
    private TextView tvUri;
    private TextView tvReal;
    private Button bnChange;

    private void initialiseView(){
        ivImage = findViewById(R.id.ivImage);
        tvUri = findViewById(R.id.tvUri);
        tvReal = findViewById(R.id.tvReal);
        bnChange = findViewById(R.id.bnChange);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialiseView();
        bnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST);
            }
        });
    }

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
            String[]projection = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(uri, projection, null, null,null);
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

    private void loadImageFromUri(Intent intent){
        if (intent != null){
            Uri uri = intent.getData();
            if (uri != null){
                try{
                    tvUri.setText(String.format("%s", "Uri path: " + uri.toString()));
                    String realPath = getRealPathFromUri(MainActivity.this, uri);
                    tvReal.setText(String.format("%s", "Real path: " + realPath));
                    Bitmap bitmap = decodeUri(MainActivity.this, uri, 300);
                    if (bitmap != null) ivImage.setImageBitmap(bitmap);
                }catch (FileNotFoundException e){
                    e.printStackTrace();
                    tvUri.setText(String.format("%s", "file not found"));
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST && resultCode == RESULT_OK){
            loadImageFromUri(data);
        }
    }
}
