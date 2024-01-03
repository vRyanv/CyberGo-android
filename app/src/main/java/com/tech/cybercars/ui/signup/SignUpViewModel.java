package com.tech.cybercars.ui.signup;

import android.app.Application;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.R;
import com.tech.cybercars.utils.PhoneUtil;

public class SignUpViewModel extends AndroidViewModel {
    public final MutableLiveData<String> full_name = new MutableLiveData<String>("");
    public final MutableLiveData<String> full_name_error = new MutableLiveData<>("");
    public final MutableLiveData<String> email = new MutableLiveData<>("");
    public final MutableLiveData<String> email_error = new MutableLiveData<>("");
    public final MutableLiveData<String> phone_number = new MutableLiveData<>("");
    public final MutableLiveData<String> phone_number_error = new MutableLiveData<>("");
    public final MutableLiveData<String> country_name_code = new MutableLiveData<>("");
    public final MutableLiveData<String> gender = new MutableLiveData<>("");
    public final MutableLiveData<String> gender_error = new MutableLiveData<>("");
    public final MutableLiveData<Boolean> is_loading = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> is_login_success = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> agree_term_policy = new MutableLiveData<>(false);
    public final MutableLiveData<String> agree_term_policy_error = new MutableLiveData<>("");

    public SignUpViewModel(@NonNull Application application) {
        super(application);
    }
    public void handleLogin(){
        is_loading.setValue(true);
        if(ValidateUserData()){

        } else {

        }
        is_loading.setValue(false);
    }

    private boolean ValidateUserData(){
        String error_mess = "";
        boolean is_valid_info = true;
        Resources res = getApplication().getResources();
        if(full_name.getValue().equals("")){
            error_mess = res.getString(R.string.full_name) + " " + res.getString(R.string.can_not_empty);
            full_name_error.setValue(error_mess);
            is_valid_info = false;
        }

        if(email.getValue().equals("")){
            error_mess = res.getString(R.string.email) + " " + res.getString(R.string.can_not_empty);
            is_valid_info = false;
            email_error.setValue(error_mess);
        } else {
            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getValue()).matches()){
                error_mess =  res.getString(R.string.invalid_email);
                is_valid_info = false;
                email_error.setValue(error_mess);
            }
        }

        if(phone_number.getValue().equals("")){
            error_mess = res.getString(R.string.phone_number) + " " + res.getString(R.string.can_not_empty);
            phone_number_error.setValue(error_mess);
        } else {
            String phone_number_national = PhoneUtil.getInstance().formatToPhoneNational(phone_number.getValue(), country_name_code.getValue());
            if(phone_number_national.equals("")){
                error_mess =  res.getString(R.string.invalid_phone_number);
                phone_number_error.setValue(error_mess);
            }
        }

        if(gender.getValue().equals("")){
            error_mess = res.getString(R.string.gender) + " " + res.getString(R.string.can_not_empty);
            is_valid_info = false;
            gender_error.setValue(error_mess);
        }

        if(!agree_term_policy.getValue()){
            error_mess = res.getString(R.string.please_agree_to_our_privacy_policy_and_terms);
            agree_term_policy_error.setValue(error_mess);
            is_valid_info = false;
        }

        return is_valid_info;
    }
}

