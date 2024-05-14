package com.tech.cybercars.ui.signin;

import android.app.Dialog;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.tech.cybercars.R;
import com.tech.cybercars.databinding.ActivitySignInBinding;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.component.dialog.NotificationDialog;
import com.tech.cybercars.ui.main.MainActivity;
import com.tech.cybercars.ui.signin.forgot_password.ForgotPasswordActivity;
import com.tech.cybercars.ui.signup.SignUpActivity;
import com.tech.cybercars.ui.signup.verification.PhoneVerificationActivity;
import com.tech.cybercars.utils.KeyBoardUtil;

public class SignInActivity extends BaseActivity<ActivitySignInBinding, SignInViewModel> {
    @NonNull
    @Override
    protected SignInViewModel InitViewModel() {
        return new ViewModelProvider(this).get(SignInViewModel.class);
    }

    @Override
    protected ActivitySignInBinding InitBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {
        binding.txtAlreadyAccount.setText(SetUpSignUpTextClickable());
        binding.txtAlreadyAccount.setMovementMethod(LinkMovementMethod.getInstance());
        binding.btnSignIn.setOnClickListener(view -> {
            KeyBoardUtil.HideKeyBoard(this);
            view_model.SignInHandle();
        });

        view_model.email.setValue("letruc2108@gmail.com");
        view_model.password.setValue("123123123");

        binding.txtForgetPasswordSignIn.setOnClickListener(view -> {
            startActivity(new Intent(this, ForgotPasswordActivity.class));
        });

        binding.btnLoginGg.setOnClickListener(view -> {
            view_model.email.setValue("khangok1610@gmail.com");
        });
    }

    @Override
    protected void InitObserve() {
        view_model.getEmailLive().observe(this, email -> {
            if (email != null && !email.equals("")) {
                view_model.email_error.setValue(null);
            }
        });
        view_model.getEmailErrorLive().observe(this, email_error -> {
            binding.inputEmailSignIn.setError(email_error);
        });

        view_model.getPasswordLive().observe(this, password -> {
            if (password != null && !password.equals("")) {
                view_model.password_error.setValue(null);
            }
        });
        view_model.getPasswordErrorLive().observe(this, password_error -> {
            binding.inputPasswordSignIn.setError(password_error);
        });

        view_model.getErrorLoginLive().observe(this, error_login -> {
            if(error_login != null && !error_login.equals("")){
                NotificationDialog.Builder(this)
                        .SetIcon(R.drawable.ic_warning)
                        .SetTitleVisibility(View.GONE)
                        .SetSubtitle(error_login)
                        .SetTextMainButton(getResources().getString(R.string.close))
                        .SetOnMainButtonClicked(Dialog::dismiss).show();
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

        view_model.getIsVerifyAccount().observe(this, is_verify_account->{
            if(is_verify_account != null && is_verify_account){
                NotificationDialog.Builder(this)
                        .SetIcon(R.drawable.ic_warning)
                        .SetTitle(getResources().getString(R.string.account_already_exist))
                        .SetSubtitle(getResources().getString(R.string.we_found_an_account_that_was_previously_registered_but_not_activated_please_check_your_email_to_activate_the_account))
                        .SetTextMainButton(getResources().getString(R.string.next))
                        .SetOnMainButtonClicked(dialog->{
                            Intent verify_account_intent = new Intent(this, PhoneVerificationActivity.class);
                            verify_account_intent.putExtra("email", view_model.getEmailLive().getValue());
                            startActivity(verify_account_intent);
                            dialog.dismiss();
                        }).show();
            }
        });

        view_model.getIsSuccessLive().observe(this, is_success -> {
            if (is_success != null && is_success) {
                Intent home_intent = new Intent(this, MainActivity.class);
                home_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(home_intent);
                finish();
            }
        });
    }

    @Override
    protected void InitCommon() {

    }

    @Override
    protected void OnBackPress() {

    }


    private Spannable SetUpSignUpTextClickable() {
        String full_text = getString(R.string.do_not_have_an_account);
        int color = ResourcesCompat.getColor(getResources(), R.color.orange, getTheme());

        Spannable sign_up_spannable = new SpannableString(full_text);
        String text_sign_up = getString(R.string.sign_up);

        //get index start and end of sign up word
        int index_text_start = full_text.indexOf(text_sign_up);
        int index_text_end = full_text.indexOf(text_sign_up) + text_sign_up.length();

        ClickableSpan sign_up_word_clickable_span = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };

        //set span for sign up word
        sign_up_spannable.setSpan(
                new ForegroundColorSpan(color),
                index_text_start,
                index_text_end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sign_up_spannable.setSpan(sign_up_word_clickable_span,
                index_text_start,
                index_text_end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sign_up_spannable;
    }
}