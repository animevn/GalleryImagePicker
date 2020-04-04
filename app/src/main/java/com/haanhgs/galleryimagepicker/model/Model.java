package com.haanhgs.galleryimagepicker.model;

import android.graphics.Bitmap;

public class Model {

    private String uri = "";
    private String path = "";
    private Bitmap img = null;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
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
