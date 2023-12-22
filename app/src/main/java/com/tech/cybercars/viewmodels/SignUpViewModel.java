package com.tech.cybercars.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SignUpViewModel extends ViewModel {
    public MutableLiveData<String> full_name = new MutableLiveData<String>();
    public MutableLiveData<String> email = new MutableLiveData<String>();

    public void click(){
        email.setValue(full_name.getValue());
    }
}
