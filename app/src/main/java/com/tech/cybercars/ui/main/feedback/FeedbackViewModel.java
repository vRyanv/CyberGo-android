package com.tech.cybercars.ui.main.feedback;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.ui.base.BaseViewModel;

public class FeedbackViewModel extends BaseViewModel {
    public MutableLiveData<String> content = new MutableLiveData<>();
    public int rating = 0;
    public FeedbackViewModel(@NonNull Application application) {
        super(application);
    }
}
