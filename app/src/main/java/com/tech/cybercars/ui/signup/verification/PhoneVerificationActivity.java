package com.tech.cybercars.ui.signup.verification;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Intent;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tech.cybercars.R;
import com.tech.cybercars.databinding.ActivityPhoneVerificationBinding;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.signup.password.SetPasswordActivity;
import com.tech.cybercars.utils.KeyBoardUtil;

public class PhoneVerificationActivity extends BaseActivity<ActivityPhoneVerificationBinding, PhoneVerificationViewModel> {

    @NonNull
    @Override
    protected PhoneVerificationViewModel InitViewModel() {
        return new ViewModelProvider(this).get(PhoneVerificationViewModel.class);
    }

    @Override
    public ActivityPhoneVerificationBinding InitBinding(ViewModel view_model) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone_verification);
        binding.setPhoneVerificationVm((PhoneVerificationViewModel) view_model);
        return binding;
    }

    @Override
    protected void InitView() {
        binding.txtResendAgainPhoneVerification.setText(SetUpSendAgainTextClickable());
        binding.txtResendAgainPhoneVerification.setMovementMethod(LinkMovementMethod.getInstance());

        binding.inputOtpNumber1Verification.addTextChangedListener(TextChangeListener(view_model.otp_1));
        binding.inputOtpNumber2Verification.addTextChangedListener(TextChangeListener(view_model.otp_2));
        binding.inputOtpNumber3Verification.addTextChangedListener(TextChangeListener(view_model.otp_3));
        binding.inputOtpNumber4Verification.addTextChangedListener(TextChangeListener(view_model.otp_4));
        binding.inputOtpNumber5Verification.addTextChangedListener(TextChangeListener(view_model.otp_5));
    }

    @Override
    protected void InitObserve() {
        view_model.is_loading.observe(this, is_loading -> {
            if(is_loading){
                KeyBoardUtil.HideKeyBoard(this);
            }
        });
        view_model.otp_1.observe(this, number_str -> {
            SwitchOTPCodeBox(number_str, binding.inputOtpNumber1Verification, binding.inputOtpNumber2Verification);
        });
        view_model.otp_2.observe(this, number_str -> {
            SwitchOTPCodeBox(number_str, binding.inputOtpNumber1Verification, binding.inputOtpNumber3Verification);
        });
        view_model.otp_3.observe(this, number_str -> {
            SwitchOTPCodeBox(number_str, binding.inputOtpNumber2Verification, binding.inputOtpNumber4Verification);
        });
        view_model.otp_4.observe(this, number_str -> {
            SwitchOTPCodeBox(number_str, binding.inputOtpNumber3Verification, binding.inputOtpNumber5Verification);
        });
        view_model.otp_5.observe(this, number_str -> {
            SwitchOTPCodeBox5(number_str, binding.inputOtpNumber4Verification, binding.inputOtpNumber5Verification);
        });

        view_model.is_success.observe(this, is_success -> {
            if(is_success){
                startActivity(new Intent(this, SetPasswordActivity.class));
            }
        });
    }

    private TextWatcher TextChangeListener(MutableLiveData<String> otp){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 1){
                    otp.setValue(String.valueOf(s.charAt(1)));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    private void SwitchOTPCodeBox(String number_str, EditText previous_input_otp, EditText next_input_otp){
        if(!number_str.equals("")){
            next_input_otp.requestFocus();
            next_input_otp.setSelection(next_input_otp.getText().length());
        } else {
            previous_input_otp.requestFocus();
            previous_input_otp.setSelection(previous_input_otp.getText().length());
        }
    }

    private void SwitchOTPCodeBox5(String number_str, EditText previous_input_otp, EditText input_otp_5){
        if(!number_str.equals("")){
            input_otp_5.clearFocus();
        } else {
            previous_input_otp.requestFocus();
            previous_input_otp.setSelection(previous_input_otp.getText().length());
        }
    }


    @Override
    protected void InitCommon() {

    }

    private Spannable SetUpSendAgainTextClickable() {
        String full_text = getString(R.string.did_not_receive_code);
        int color = ResourcesCompat.getColor(getResources(), R.color.orange, getTheme());

        Spannable send_again_spannable = new SpannableString(full_text);
        String text_send_again = getString(R.string.resend_again);

        //get index start and end of sign in word
        int index_text_start = full_text.indexOf(text_send_again);
        int index_text_end = full_text.indexOf(text_send_again) + text_send_again.length();

        ClickableSpan send_again_word_clickable_span = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                ConfirmResendOTPCode();
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };

        //set span for send again word
        send_again_spannable.setSpan(
                new ForegroundColorSpan(color),
                index_text_start,
                index_text_end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        send_again_spannable.setSpan(send_again_word_clickable_span,
                index_text_start,
                index_text_end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return send_again_spannable;
    }

    private void ConfirmResendOTPCode(){
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(getResources().getString(R.string.app_name))
                .setIcon(AppCompatResources.getDrawable(this, R.drawable.ic_app))
                .setMessage(getResources().getString(R.string.you_have_not_received_the_otp_code_yet))
                .setNegativeButton(R.string.resend_again, (dialog, which_button)->{
                    Toast.makeText(this, "resend click", Toast.LENGTH_SHORT).show();
                })
                .setPositiveButton(R.string.cancel,(dialog, which_button)->{
                    Toast.makeText(this, "cancel click", Toast.LENGTH_SHORT).show();
                }).show();
    }
}