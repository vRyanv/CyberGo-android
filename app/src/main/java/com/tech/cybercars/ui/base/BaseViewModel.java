package com.tech.cybercars.ui.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public abstract class BaseViewModel extends AndroidViewModel {
    public MutableLiveData<Boolean> is_loading = new MutableLiveData<>();
    public LiveData<Boolean> getIsLoadingLive(){return is_loading;}
    public MutableLiveData<Boolean> is_success = new MutableLiveData<>();
    public LiveData<Boolean> getIsSuccessLive(){return is_success;}
    public MutableLiveData<String> error_call_server = new MutableLiveData<>();
    public LiveData<String> getErrorCallServerLive(){return error_call_server;}
    public BaseViewModel(@NonNull Application application) {
        super(application);
    }
    protected void ResetBaseViewModel(){
        is_loading.setValue(false);
        is_success.setValue(false);
        error_call_server.setValue(null);
    }
    protected void ResetPostBaseViewModel(){
        is_loading.postValue(false);
        is_success.postValue(false);
        error_call_server.postValue(null);
    }
    public abstract void ResetViewModel();
}
