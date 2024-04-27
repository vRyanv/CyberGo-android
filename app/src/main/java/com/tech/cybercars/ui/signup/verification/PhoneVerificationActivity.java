package com.tech.cybercars.ui.signup.verification;


import static android.view.KeyEvent.KEYCODE_FORWARD_DEL;

import android.app.AlertDialog;
import android.app.Dialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.tech.cybercars.R;
import com.tech.cybercars.databinding.ActivityPhoneVerificationBinding;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.component.dialog.NotificationDialog;
import com.tech.cybercars.utils.KeyBoardUtil;

public class PhoneVerificationActivity extends BaseActivity<com.tech.cybercars.databinding.ActivityPhoneVerificationBinding, PhoneVerificationViewModel> {

    @NonNull
    @Override
    protected PhoneVerificationViewModel InitViewModel() {
        return new ViewModelProvider(this).get(PhoneVerificationViewModel.class);
    }

    @Override
    public ActivityPhoneVerificationBinding InitBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone_verification);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {
        binding.btnPhoneVerification.setOnClickListener(view -> {
            ShowSuccessFeedback();
        });

        binding.txtResendAgainPhoneVerification.setText(SetUpSendAgainTextClickable());
        binding.txtResendAgainPhoneVerification.setMovementMethod(LinkMovementMethod.getInstance());

        binding.headerPrimary.btnOutScreen.setOnClickListener(view -> {
            OnBackPress();
        });
    }

    @Override
    protected void InitObserve() {
        view_model.is_loading.observe(this, is_loading -> {
            if(is_loading != null && is_loading){
                KeyBoardUtil.HideKeyBoard(this);
            }
        });

        view_model.getOtp1Live().observe(this, number_str -> {
            SwitchOTPCodeBox(number_str, binding.inputOtpNumber2Verification);
        });
        view_model.getOtp2Live().observe(this, number_str -> {
            SwitchOTPCodeBox(number_str, binding.inputOtpNumber3Verification);
        });
        view_model.getOtp3Live().observe(this, number_str -> {
            SwitchOTPCodeBox(number_str, binding.inputOtpNumber4Verification);
        });
        view_model.getOtp4Live().observe(this, number_str -> {
            SwitchOTPCodeBox(number_str, binding.inputOtpNumber5Verification);
        });
        view_model.getOtp5Live().observe(this, number_str -> {
            SwitchOTPCodeBox5(number_str, binding.inputOtpNumber5Verification);
        });

        view_model.getErrorOtpCodeLive().observe(this, error_otp_code -> {
            if(error_otp_code != null && !error_otp_code.equals("")){
                NotificationDialog.Builder(this)
                        .SetIcon(R.drawable.ic_warning)
                        .SetTitleVisibility(View.GONE)
                        .SetSubtitle(error_otp_code)
                        .SetTextMainButton(getResources().getString(R.string.close))
                        .SetOnMainButtonClicked(Dialog::dismiss).show();
            }
        });

        view_model.getIsSuccessLive().observe(this, is_success -> {
            if(is_success != null && is_success){
                finish();
            }
        });

        view_model.getErrorCallServerLive().observe(this, error_call_server -> {
            if(error_call_server != null && !error_call_server.equals("")){
                    NotificationDialog.Builder(this)
                            .SetIcon(R.drawable.ic_error)
                            .SetTitle(getResources().getString(R.string.something_went_wrong))
                            .SetSubtitle(error_call_server)
                            .SetTextMainButton(getResources().getString(R.string.close))
                            .SetOnMainButtonClicked(Dialog::dismiss).show();
                }
            });
    }


    private void SwitchOTPCodeBox(String number_str, EditText next_input_otp){
        if(!number_str.equals("")){
            next_input_otp.requestFocus();
            next_input_otp.setSelection(next_input_otp.getText().length());
        }
    }

    private void SwitchOTPCodeBox5(String number_str, EditText input_otp_5){
        if(!number_str.equals("")){
            input_otp_5.clearFocus();
        }
    }

    private void SwitchOTPCodePrevious(int key_code, LiveData<String> otp, EditText input_previous){
        if(key_code == KEYCODE_FORWARD_DEL && otp != null && otp.getValue().equals("")){
            input_previous.requestFocus();
        }
    }


    @Override
    protected void InitCommon() {
        String email = getIntent().getStringExtra("email");
        view_model.setEmail(email);
    }

    @Override
    protected void OnBackPress() {
        finish();
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
                .setIcon(AppCompatResources.getDrawable(this, R.drawable.ic_notify_app))
                .setMessage(getResources().getString(R.string.you_have_not_received_the_otp_code_yet))
                .setNegativeButton(R.string.resend_again, (dialog, which_button)->{
                    Toast.makeText(this, "resend click", Toast.LENGTH_SHORT).show();
                })
                .setPositiveButton(R.string.cancel,(dialog, which_button)->{
                    Toast.makeText(this, "cancel click", Toast.LENGTH_SHORT).show();
                }).show();
    }

    private void ShowSuccessFeedback() {
        NotificationDialog.Builder(this)
                .SetIcon(R.drawable.ic_success)
                .SetTitle(getResources().getString(R.string.success))
                .SetSubtitle(getResources().getString(R.string.you_can_log_in_now))
                .SetTextMainButton(getResources().getString(R.string.login))
                .SetOnMainButtonClicked(dialog -> {
                    finish();
                }).show();
    }
}