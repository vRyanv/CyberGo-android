package com.tech.cybercars.ui.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class BaseViewModel extends AndroidViewModel {
    public MutableLiveData<Boolean> is_loading = new MutableLiveData<>();
    public LiveData<Boolean> getIsLoading(){return is_loading;}
    public MutableLiveData<Boolean> is_success = new MutableLiveData<>();
    public LiveData<Boolean> getIsSuccess(){return is_success;}
    public BaseViewModel(@NonNull Application application) {
        super(application);
    }
}
