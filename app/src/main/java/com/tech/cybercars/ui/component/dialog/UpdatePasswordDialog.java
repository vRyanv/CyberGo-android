package com.tech.cybercars.ui.component.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Window;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;
import com.tech.cybercars.R;
import com.tech.cybercars.databinding.DialogChangePassBinding;

public class UpdatePasswordDialog extends Dialog {
    private SelectButtonCallback select_button_callback;
    private final DialogChangePassBinding binding;
    private String title;

    public UpdatePasswordDialog(@NonNull Context context) {
        super(context);
        binding = DialogChangePassBinding.inflate(LayoutInflater.from(context));
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(binding.getRoot());
        InitView();
    }


    private void InitView() {
        binding.btnUpdate.setOnClickListener(view -> {
            if (IsValid()) {
                select_button_callback.OnUpdate(
                        this,
                        binding.inputCurrentPass.getEditText().getText().toString(),
                        binding.inputNewPass.getEditText().getText().toString(),
                        binding.inputConfirmPass.getEditText().getText().toString()
                        );
            }
        });

        binding.btnCancel.setOnClickListener(view -> {
            select_button_callback.OnCancel(this);
        });

        binding.inputCurrentPass.getEditText().addTextChangedListener(new CustomTextWatcher(binding.inputCurrentPass));
        binding.inputNewPass.getEditText().addTextChangedListener(new CustomTextWatcher(binding.inputNewPass));
        binding.inputConfirmPass.getEditText().addTextChangedListener(new CustomTextWatcher(binding.inputConfirmPass));
    }

    static class CustomTextWatcher implements TextWatcher{
        private final TextInputLayout input_layout;
        public CustomTextWatcher(TextInputLayout input_layout){
            this.input_layout = input_layout;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            input_layout.setError(null);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private boolean IsValid() {
        binding.inputCurrentPass.setError(null);
        binding.inputNewPass.setError(null);
        binding.inputConfirmPass.setError(null);
        boolean is_valid = true;
        String error = getContext().getString(R.string.can_not_empty);

        String current_pass = binding.inputCurrentPass.getEditText().getText().toString();
        String new_pass = binding.inputNewPass.getEditText().getText().toString();
        String confirm_pass = binding.inputConfirmPass.getEditText().getText().toString();
        if (current_pass.equals("")) {
            is_valid = false;
            binding.inputCurrentPass.setError(error);
        }

        if (new_pass.equals("")) {
            is_valid = false;
            binding.inputNewPass.setError(error);
        }

        if (confirm_pass.equals("")) {
            is_valid = false;
            binding.inputConfirmPass.setError(error);
        }

        if (!confirm_pass.equals(new_pass)) {
            is_valid = false;
            String pass_not_match = getContext().getString(R.string.new_password) + " " + getContext().getString(R.string.and) + " " + getContext().getString(R.string.confirm_password) + " " + getContext().getString(R.string.does_not_match);
            binding.inputConfirmPass.setError(pass_not_match);
        }

        return is_valid;
    }

    public UpdatePasswordDialog SetButtonSelectedCallback(SelectButtonCallback select_button_callback) {
        this.select_button_callback = select_button_callback;
        return this;
    }

    public interface SelectButtonCallback {
        void OnUpdate(Dialog dialog, String current_pass, String new_pass, String confirm_pass);

        void OnCancel(Dialog dialog);
    }
}
