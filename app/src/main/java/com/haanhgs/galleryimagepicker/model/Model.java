package com.haanhgs.galleryimagepicker.model;

import android.graphics.Bitmap;
import android.net.Uri;

public class Model {

    private Uri uri = null;
    private String path = "";
    private Bitmap img = null;

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }
}
