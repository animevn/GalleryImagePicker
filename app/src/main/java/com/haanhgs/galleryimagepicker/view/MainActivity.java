package com.haanhgs.galleryimagepicker.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.haanhgs.galleryimagepicker.R;
import com.haanhgs.galleryimagepicker.model.Constants;
import com.haanhgs.galleryimagepicker.model.Repo;
import java.io.FileNotFoundException;
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

    private Uri uri;

    private void hideActionBarInLandscapeMode(){
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270){
            if (getSupportActionBar() != null) getSupportActionBar().hide();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        hideActionBarInLandscapeMode();
    }

    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(Constants.IMAGE_TYPE);
        startActivityForResult(intent, Constants.REQUEST);
    }

    private void loadImageFromGallery(Intent intent){

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST &&resultCode == RESULT_OK && data != null){
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
