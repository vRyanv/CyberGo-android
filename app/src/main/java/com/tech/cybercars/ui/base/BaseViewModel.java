package com.tech.cybercars.ui.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class BaseViewModel extends AndroidViewModel {
    public MutableLiveData<Boolean> is_loading = new MutableLiveData<>();
    public MutableLiveData<Boolean> is_success = new MutableLiveData<>();
    public BaseViewModel(@NonNull Application application) {
        super(application);
    }
}
