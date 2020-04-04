package com.haanhgs.galleryimagepicker.model;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import androidx.lifecycle.MutableLiveData;

public class Repo {

    private final Context context;
    private final Model model = new Model();
    private final MutableLiveData<Model> liveData = new MutableLiveData<>();
    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 8, 60, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>());

    public Repo(Context context) {
        this.context = context;
    }

    public MutableLiveData<Model> getLiveData() {
        return liveData;
    }

    public void setLiveData() {
        liveData.setValue(model);
    }

    private Bitmap decodeUri(Uri uri)throws FileNotFoundException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        BitmapFactory.decodeStream(inputStream, null, options);
        int tempWidth = options.outWidth;
        int tempHeight = options.outHeight;
        int scale = 1;
        while (tempWidth/2 >= Constants.SIZE && tempHeight/2 >= Constants.SIZE){
            tempHeight /= 2;
            tempWidth /= 2;
            scale *= 2;
        }
        BitmapFactory.Options optionsNew = new BitmapFactory.Options();
        optionsNew.inSampleSize = scale;
        InputStream inputStreamNew = context.getContentResolver().openInputStream(uri);
        return BitmapFactory.decodeStream(inputStreamNew, null, optionsNew);
    }

    private String getRealPathFromUri(Uri uri){
        Cursor cursor = null;
        try{
            String[] projection = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(uri, projection, null, null, null);
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

    public void updateModel(Intent intent){
        executor.execute(() -> {
            Uri uri = intent.getData();
            if (uri != null){
                try{
                    model.setUri(uri);
                    model.setPath(getRealPathFromUri(uri));
                    model.setImg(decodeUri(uri));
                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }
            }
            liveData.postValue(model);
        });
    }

    public void shareData(){
        if (model.getUri() != null){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType(Constants.IMAGE_TYPE);
            intent.putExtra(Intent.EXTRA_STREAM, model.getUri());
            Intent chooserIntent = Intent.createChooser(intent, Constants.CHOOSE_IMAGE);
            chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(chooserIntent);
        }
    }

}
