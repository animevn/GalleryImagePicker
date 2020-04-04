package com.haanhgs.galleryimagepicker.viewmodel;

import android.app.Application;
import android.content.Intent;
import com.haanhgs.galleryimagepicker.model.Model;
import com.haanhgs.galleryimagepicker.model.Repo;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ViewModel extends AndroidViewModel {

    private Repo repo;

    public ViewModel(@NonNull Application application) {
        super(application);
        repo = new Repo(application.getApplicationContext());
        repo.setLiveData();
    }

    public LiveData<Model> getData(){
        return repo.getLiveData();
    }

    public void updateModel(Intent intent){
        repo.updateModel(intent);
    }

    public void shareData(){
        repo.shareData();
    }
}
