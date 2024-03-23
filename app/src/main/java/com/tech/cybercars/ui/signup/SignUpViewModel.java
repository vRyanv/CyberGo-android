package com.tech.cybercars.ui.signup;

import android.app.Application;
import android.content.res.Resources;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.DelayTime;
import com.tech.cybercars.constant.Gender;
import com.tech.cybercars.constant.StatusCode;
import com.tech.cybercars.data.remote.user.signup.SignUpBody;
import com.tech.cybercars.data.remote.user.signup.SignUpResponse;
import com.tech.cybercars.data.repositories.UserRepository;
import com.tech.cybercars.ui.base.BaseViewModel;
import com.tech.cybercars.utils.PhoneUtil;

import retrofit2.Response;

public class SignUpViewModel extends BaseViewModel {
    public final MutableLiveData<String> full_name = new MutableLiveData<String>();

    public LiveData<String> getFullNameLive() {
        return full_name;
    }

    public final MutableLiveData<String> full_name_error = new MutableLiveData<>();

    public LiveData<String> getFullNameErrorLive() {
        return full_name_error;
    }

    public final MutableLiveData<String> email = new MutableLiveData<>();

    public LiveData<String> getEmailLive() {
        return email;
    }

    public final MutableLiveData<String> email_error = new MutableLiveData<>();

    public LiveData<String> getEmailErrorLive() {
        return email_error;
    }

    public final MutableLiveData<String> phone_number = new MutableLiveData<>();

    public LiveData<String> getPhoneNumberLive() {
        return phone_number;
    }

    public final MutableLiveData<String> phone_number_error = new MutableLiveData<>();

    public LiveData<String> getPhoneNumberErrorLive() {
        return phone_number_error;
    }

    public final MutableLiveData<String> country_name_code = new MutableLiveData<>();
    public final MutableLiveData<String> gender = new MutableLiveData<>();

    public LiveData<String> getGenderLive() {
        return gender;
    }

    public final MutableLiveData<String> gender_error = new MutableLiveData<>();

    public LiveData<String> getGenderErrorLive() {
        return gender_error;
    }

    public MutableLiveData<String> password = new MutableLiveData<>();

    public LiveData<String> getPasswordLive() {
        return password;
    }

    public MutableLiveData<String> password_error = new MutableLiveData<>();

    public LiveData<String> getPasswordErrorLive() {
        return password_error;
    }

    public MutableLiveData<String> confirm_password = new MutableLiveData<>();

    public LiveData<String> getConfirmPasswordLive() {
        return confirm_password;
    }

    public MutableLiveData<String> confirm_password_error = new MutableLiveData<>();

    public LiveData<String> getConfirmPasswordErrorLive() {
        return confirm_password_error;
    }

    public final MutableLiveData<Boolean> agree_term_policy = new MutableLiveData<>();

    public LiveData<Boolean> getAgreeTermPolicyLive() {
        return agree_term_policy;
    }

    public final MutableLiveData<String> agree_term_policy_error = new MutableLiveData<>();

    public LiveData<String> getAgreeTermPolicyErrorLive() {
        return agree_term_policy_error;
    }

    public final MutableLiveData<Boolean> is_verify_account = new MutableLiveData<>();

    public LiveData<Boolean> getIsVerifyAccountLive() {
        return is_verify_account;
    }

    private final UserRepository user_repository = UserRepository.GetInstance();

    public SignUpViewModel(@NonNull Application application) {
        super(application);
        Seed();
    }

    public void Seed() {
        email.setValue("khangok1610@gmail.com");
        full_name.setValue("khang");
        gender.setValue(getApplication().getString(R.string.male));
        phone_number.setValue("374463592");
        password.setValue("123123123");
        confirm_password.setValue("123123123");
        agree_term_policy.setValue(true);
    }

    public void SignUpHandle() {
        if (ValidateUserData()) {
            is_loading.setValue(true);

            int gender_int = Gender.OTHER;
            if (gender.getValue().equals(getApplication().getString(R.string.female))) {
                gender_int = Gender.MALE;
            } else if (gender.getValue().equals(getApplication().getString(R.string.male))) {
                gender_int = Gender.FEMALE;
            }

            String number_prefix = "+" + PhoneUtil.getInstance().GetPrefixOfPhoneNumber(
                    phone_number.getValue(),
                    country_name_code.getValue()
            );
            SignUpBody sign_up_body = new SignUpBody(
                    email.getValue(),
                    full_name.getValue(),
                    phone_number.getValue(),
                    number_prefix,
                    gender_int,
                    password.getValue(),
                    confirm_password.getValue()
            );

            SignUpRemote(sign_up_body);
        }
    }

    private void SignUpRemote(SignUpBody sign_up_body) {
        user_repository.CreateUser(
                sign_up_body,
                this::CallSignUpSuccess,
                this::CallSignUpFail
        );
    }

    private void CallSignUpSuccess(Response<SignUpResponse> response) {

        new Handler().postDelayed(() -> {
            if(!response.isSuccessful() || response.body() == null){
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
                is_loading.postValue(false);
                return;
            }

            if (response.body().getCode() == StatusCode.CREATED) {
                is_success.postValue(true);
            } else if (response.body().getCode() == StatusCode.VERIFY) {
                is_verify_account.postValue(true);
            } else if (response.body().getCode() == StatusCode.BAD_REQUEST) {
                if (response.body().isIs_email_used()) {
                    email_error.postValue(getApplication().getString(R.string.email_has_been_used));
                }
                if (response.body().isIs_phone_used()) {
                    phone_number_error.postValue(getApplication().getString(R.string.phone_number_has_been_used));
                }
                if (!response.body().isIs_phone_used() && !response.body().isIs_email_used()) {
                    error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
                }
            }
            is_loading.postValue(false);
        }, DelayTime.CALL_API);
    }

    private void CallSignUpFail(Throwable error) {
        is_loading.setValue(false);
        error_call_server.postValue(getApplication().getString(R.string.can_not_connect_to_server));
    }

    private boolean ValidateUserData() {
        String error_mess = "";
        boolean is_valid_info = true;
        Resources res = getApplication().getResources();
        if (full_name.getValue() == null || full_name.getValue().trim().equals("")) {
            error_mess = res.getString(R.string.full_name) + " " + res.getString(R.string.can_not_empty);
            full_name_error.setValue(error_mess);
            is_valid_info = false;
        }

        if (email.getValue() == null || email.getValue().trim().equals("")) {
            error_mess = res.getString(R.string.email) + " " + res.getString(R.string.can_not_empty);
            is_valid_info = false;
            email_error.setValue(error_mess);
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getValue()).matches()) {
            error_mess = res.getString(R.string.invalid_email);
            is_valid_info = false;
            email_error.setValue(error_mess);
        }

        if (phone_number.getValue() == null || phone_number.getValue().trim().equals("")) {
            error_mess = res.getString(R.string.phone_number) + " " + res.getString(R.string.can_not_empty);
            phone_number_error.setValue(error_mess);
        } else {
            boolean is_valid_phone = PhoneUtil.getInstance().IsValidPhoneNumber(phone_number.getValue(), country_name_code.getValue());
            if (!is_valid_phone) {
                error_mess = res.getString(R.string.invalid_phone_number);
                phone_number_error.setValue(error_mess);
                is_valid_info = false;
            }
        }

        if (gender.getValue() == null || gender.getValue().trim().equals("")) {
            error_mess = res.getString(R.string.gender) + " " + res.getString(R.string.can_not_empty);
            is_valid_info = false;
            gender_error.setValue(error_mess);
        }

        if (password.getValue() == null || password.getValue().equals("")) {
            error_mess = res.getString(R.string.password) + " " + res.getString(R.string.can_not_empty);
            is_valid_info = false;
            password_error.setValue(error_mess);
        } else if (password.getValue().contains(" ")) {
            error_mess = res.getString(R.string.password) + " " + res.getString(R.string.can_not_contain_space);
            is_valid_info = false;
            password_error.setValue(error_mess);
        } else if (password.getValue().length() < 8) {
            error_mess = res.getString(R.string.password) + " " + res.getString(R.string.must_be_greater_than_or_equal_to_8_characters);
            is_valid_info = false;
            password_error.setValue(error_mess);
        }


        if (confirm_password.getValue() == null || confirm_password.getValue().equals("")) {
            error_mess = res.getString(R.string.confirm_password) + " " + res.getString(R.string.can_not_empty);
            is_valid_info = false;
            confirm_password_error.setValue(error_mess);
        } else if (password.getValue() != null && !password.getValue().equals(confirm_password.getValue())) {
            error_mess = res.getString(R.string.password) + " " + res.getString(R.string.and) + " " + res.getString(R.string.confirm_password) + " " + res.getString(R.string.does_not_match);
            is_valid_info = false;
            confirm_password_error.setValue(error_mess);
        }


        if (agree_term_policy.getValue() == null) {
            error_mess = res.getString(R.string.please_agree_to_our_privacy_policy_and_terms);
            agree_term_policy_error.setValue(error_mess);
            is_valid_info = false;
        }


        return is_valid_info;
    }
}

