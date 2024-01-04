package com.tech.cybercars.ui.signup.password;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.BuildConfig;
import com.tech.cybercars.R;
import com.tech.cybercars.ui.base.BaseViewModel;

import java.util.Objects;

public class SetPasswordViewModel extends BaseViewModel {
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<String> password_error = new MutableLiveData<>();
    public MutableLiveData<String> confirm_password = new MutableLiveData<>();
    public MutableLiveData<String> confirm_password_error = new MutableLiveData<>();

    public SetPasswordViewModel(@NonNull Application application) {
        super(application);
    }

    public void SetPasswordHandle() {
        is_loading.setValue(true);
        if (ValidateData()) {
            is_success.setValue(true);
        }


        is_success.setValue(false);
        is_loading.setValue(false);
    }


    private boolean ValidateData(){
        boolean is_valid = true;
        if (password.getValue() == null || password.getValue().isEmpty()) {
            password_error.setValue(getApplication().getString(R.string.password) + " " + getApplication().getString(R.string.can_not_empty));
            is_valid = false;
        } else if (password.getValue().contains(" ")) {
            password_error.setValue(getApplication().getString(R.string.password) + " " + getApplication().getString(R.string.can_not_contain_space));
            is_valid = false;
        } else if (password.getValue().length() < 8) {
            password_error.setValue(getApplication().getString(R.string.password) + " " + getApplication().getString(R.string.must_be_greater_than_or_equal_to_8_characters));
            is_valid = false;
        } else {
            if (confirm_password.getValue() == null || confirm_password.getValue().isEmpty()) {
                password_error.setValue(getApplication().getString(R.string.confirm_password) + " " + getApplication().getString(R.string.can_not_empty));
                is_valid = false;
            } else if (!confirm_password.getValue().equals(password.getValue())) {
                String pass_not_match = getApplication().getString(R.string.password) + " " +
                        getApplication().getString(R.string.does_not_match) + " " +
                        getApplication().getString(R.string.confirm_password);
                confirm_password_error.setValue(pass_not_match);
                is_valid = false;
            }
        }
        return is_valid;
    }

}
