package com.haanhgs.galleryimagepicker.viewmodel;

import android.app.Application;
import com.haanhgs.galleryimagepicker.model.Repo;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class ViewModel extends AndroidViewModel {

    private Repo repo;

    public ViewModel(@NonNull Application application) {
        super(application);
        repo = new Repo(application.getApplicationContext());
    }
}
