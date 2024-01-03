package com.tech.cybercars.ui.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Toast;

import com.tech.cybercars.R;
import com.tech.cybercars.databinding.ActivitySignUpBinding;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.signup.verification.PhoneVerificationActivity;


public class SignUpActivity extends BaseActivity<ActivitySignUpBinding, SignUpViewModel>{
    private AlertDialog.Builder gender_dialog_builder;
    private AlertDialog gender_dialog;
    private String[] gender_choices;

    @NonNull
    @Override
    protected SignUpViewModel InitViewModel() {
        return new ViewModelProvider(this).get(SignUpViewModel.class);
    }

    @Override
    public ActivitySignUpBinding InitBinding(ViewModel view_model) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        binding.setSignUpViewModel((SignUpViewModel) view_model);
        return binding;
    }

    @Override
    public void InitView() {
        // check box terms and policy
        binding.txtAgreeTermPolicy.setText(SetUpCheckBoxOfTermsAndPolicyClickable());
        binding.txtAgreeTermPolicy.setMovementMethod(LinkMovementMethod.getInstance());

        // text view already account
        binding.txtAlreadyAccount.setText(SetUpSignInTextClickable());
        binding.txtAlreadyAccount.setMovementMethod(LinkMovementMethod.getInstance());

        //gender selection
        binding.inputGenderSignUp.getEditText().setOnClickListener(view -> {
            gender_dialog.show();
        });

        // country picker
        binding.countryCodePickerSignUp.registerCarrierNumberEditText(binding.inputPhoneSignUp);

        if (!view_model.country_name_code.getValue().equals("")) {
            binding.countryCodePickerSignUp.setCountryForNameCode(view_model.country_name_code.getValue());
        } else {
            view_model.country_name_code.setValue(binding.countryCodePickerSignUp.getSelectedCountryNameCode());
        }

        binding.countryCodePickerSignUp.setOnCountryChangeListener(() -> {
//            String number_prefix = data_binding.countryCodePickerSignUp.getSelectedCountryCodeWithPlus();
//            String country_code = data_binding.countryCodePickerSignUp.getSelectedCountryNameCode();
//            String country_name = data_binding.countryCodePickerSignUp.getSelectedCountryName();
            String country_code = binding.countryCodePickerSignUp.getSelectedCountryNameCode();
            String country_lang = binding.countryCodePickerSignUp.getSelectedCountryName();
            Toast.makeText(this, country_code + " " + country_lang, Toast.LENGTH_SHORT).show();
            view_model.country_name_code.setValue(binding.countryCodePickerSignUp.getSelectedCountryNameCode());
        });

        binding.btnSignUpGg.setOnClickListener(view -> {
            startActivity(new Intent(this, PhoneVerificationActivity.class));
        });
    }

    @Override
    public void InitObserve() {
        view_model.is_login_success.observe(this, is_login_success -> {

        });

        view_model.full_name.observe(this, full_name -> {
            if (!full_name.equals("")) {
                view_model.full_name_error.setValue(null);
            }
        });
        view_model.full_name_error.observe(this, full_name_error_string -> {
            binding.inputFullNameSignUp.setError(full_name_error_string);
        });

        view_model.email.observe(this, email -> {
            if (!email.equals("")) {
                view_model.email_error.setValue(null);
            }
        });
        view_model.email_error.observe(this, email_error_string -> {
            binding.inputEmailSignUp.setError(email_error_string);
        });


        view_model.phone_number.observe(this, phone_number -> {
            if (!phone_number.equals("")) {
                view_model.phone_number_error.setValue("");
            }
        });
        view_model.phone_number_error.observe(this, phone_error_string -> {
            if (!phone_error_string.equals("")) {
                binding.txtPhoneErrorSignUp.setText(phone_error_string);
                StateListDrawable drawable = (StateListDrawable) binding.phoneNumberWrapper.getBackground();
                GradientDrawable gradientDrawable = (GradientDrawable) drawable.getCurrent();
                int color = ResourcesCompat.getColor(getResources(), R.color.md_theme_light_error, getTheme());
                gradientDrawable.setStroke(3, color);
            } else {
                binding.txtPhoneErrorSignUp.setText("");
                StateListDrawable stateListDrawable = (StateListDrawable) AppCompatResources.getDrawable(this, R.drawable.shape_input_outline);
                binding.phoneNumberWrapper.setBackground(stateListDrawable);
            }
        });

        view_model.gender.observe(this, gender_string -> {
            binding.inputGenderSignUp.getEditText().setText(gender_string);
            view_model.gender_error.setValue(null);
        });
        view_model.gender_error.observe(this, gender_error_string -> {
            binding.inputGenderSignUp.setError(gender_error_string);
        });

        view_model.agree_term_policy.observe(this, agree_term_policy -> {
            if (agree_term_policy) {
                {
                    view_model.agree_term_policy_error.setValue("");
                }
            }
        });
        view_model.agree_term_policy_error.observe(this, agree_term_policy_error_string -> {
            if (!agree_term_policy_error_string.equals("")) {
                {
                    Toast.makeText(this, agree_term_policy_error_string, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void InitCommon() {
        InitDialog();
    }

    private void InitDialog() {
        gender_choices = getResources().getStringArray(R.array.gender_items);
        gender_dialog_builder = new AlertDialog.Builder(this);
        gender_dialog_builder
                .setTitle(getResources().getString(R.string.select_gender))
                .setItems(gender_choices, (dialog, which) -> {
                    view_model.gender.setValue(gender_choices[which]);
                    dialog.dismiss();
                });
        gender_dialog = gender_dialog_builder.create();
    }


    private Spannable SetUpCheckBoxOfTermsAndPolicyClickable() {
        //Get resource
        String full_text = getString(R.string.agree_term_policy);
        int color = ResourcesCompat.getColor(getResources(), R.color.orange, getTheme());

        Spannable agree_term_policy_spannable = new SpannableString(full_text);
        String text_term = getString(R.string.terms);

        //get index start and end of terms word
        int index_text_start = full_text.indexOf(text_term);
        int index_text_end = full_text.indexOf(text_term) + text_term.length();

        ClickableSpan terms_word_clickable_span = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Toast.makeText(SignUpActivity.this, "text_term clicked", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };

        //set span for terms word
        agree_term_policy_spannable.setSpan(
                new ForegroundColorSpan(color),
                index_text_start,
                index_text_end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        agree_term_policy_spannable.setSpan(terms_word_clickable_span,
                index_text_start,
                index_text_end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        String text_policy = getString(R.string.privacy_policy);

        //get index start and end of policy word
        index_text_start = full_text.indexOf(text_policy);
        index_text_end = full_text.indexOf(text_policy) + text_policy.length();

        ClickableSpan policy_word_clickable_span = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Toast.makeText(SignUpActivity.this, "text_policy clicked", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };

        //set span for policy word
        agree_term_policy_spannable.setSpan(
                new ForegroundColorSpan(color),
                index_text_start,
                index_text_end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        agree_term_policy_spannable.setSpan(policy_word_clickable_span,
                index_text_start,
                index_text_end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return agree_term_policy_spannable;
    }

    private Spannable SetUpSignInTextClickable() {
        String full_text = getString(R.string.already_have_an_account);
        int color = ResourcesCompat.getColor(getResources(), R.color.orange, getTheme());

        Spannable sign_in_spannable = new SpannableString(full_text);
        String text_sign_in = getString(R.string.sign_in);

        //get index start and end of sign in word
        int index_text_start = full_text.indexOf(text_sign_in);
        int index_text_end = full_text.indexOf(text_sign_in) + text_sign_in.length();

        ClickableSpan terms_word_clickable_span = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Toast.makeText(SignUpActivity.this, "text_term clicked", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };

        //set span for terms word
        sign_in_spannable.setSpan(
                new ForegroundColorSpan(color),
                index_text_start,
                index_text_end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sign_in_spannable.setSpan(terms_word_clickable_span,
                index_text_start,
                index_text_end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sign_in_spannable;
    }
}