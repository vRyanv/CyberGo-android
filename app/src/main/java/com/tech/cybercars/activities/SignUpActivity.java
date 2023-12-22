package com.tech.cybercars.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;


import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.tech.cybercars.R;
import com.tech.cybercars.databinding.ActivitySignUpBinding;
import com.tech.cybercars.viewmodels.SignUpViewModel;


public class SignUpActivity extends AppCompatActivity {
    private ArrayAdapter<CharSequence> gender_adapter;
    private SignUpViewModel sign_up_view_model;
    private ActivitySignUpBinding data_binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data_binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        sign_up_view_model = new ViewModelProvider(this).get(SignUpViewModel.class);
        data_binding.setSignUpViewModel(sign_up_view_model);
        data_binding.setLifecycleOwner(this);
        data_binding.executePendingBindings();

        SetUpCheckBoxOfTermsAndPolicyClickable();
        SetUpSignInTextClickable();
    }

    private void Init(){

    }

    private void EventListener() {
        country_code_picker_sign_up.registerCarrierNumberEditText(input_phone_sign_up);
//        btn_sign_up.setOnClickListener(view -> {
//            String number_prefix = country_code_picker_sign_up.getSelectedCountryCodeWithPlus();
//            String country_code = country_code_picker_sign_up.getSelectedCountryNameCode();
//            String country_name = country_code_picker_sign_up.getSelectedCountryName();
//
//            Toast.makeText(this, "number_prefix: " + number_prefix + "\n" + "country_code: " + country_code + "\n" + "country_name: " + country_name, Toast.LENGTH_SHORT).show();
//
//        });

    }

    private void SetUpCheckBoxOfTermsAndPolicyClickable() {
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


        cb_term_policy.setText(agree_term_policy_spannable);
        cb_term_policy.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void SetUpSignInTextClickable() {
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

        txt_already_account.setText(sign_in_spannable);
        txt_already_account.setMovementMethod(LinkMovementMethod.getInstance());
    }
}