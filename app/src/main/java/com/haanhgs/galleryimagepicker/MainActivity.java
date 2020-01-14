package com.haanhgs.galleryimagepicker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.util.concurrent.ExecutorService;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.ivImage)
    ImageView ivImage;
    @BindView(R.id.tvUri)
    TextView tvUri;
    @BindView(R.id.tvReal)
    TextView tvReal;
    @BindView(R.id.bnChange)
    Button bnChange;
    @BindView(R.id.bnShare)
    Button bnShare;

    private static final int REQUEST = 999;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST);
    }

    private void loadImageFromGallery(Intent intent){
        uri = intent.getData();
        if (uri != null){
            try{
                tvUri.setText(uri.toString());
                tvReal.setText(Repo.getRealPathFromUri(this, uri));
                Bitmap bitmap = Repo.decodeUri(this, uri, 500);
                if (bitmap != null)ivImage.setImageBitmap(bitmap);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST &&resultCode == RESULT_OK && data != null){
            loadImageFromGallery(data);
        }
    }

    private void shareImage(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(intent, "Choose app to share image"));
    }

    @OnClick({R.id.bnChange, R.id.bnShare})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bnChange:
                openGallery();
                break;
            case R.id.bnShare:
                shareImage();
                break;
        }
    }
}
