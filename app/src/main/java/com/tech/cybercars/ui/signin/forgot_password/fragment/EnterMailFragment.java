package com.tech.cybercars.ui.signin.forgot_password.fragment;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.tech.cybercars.R;
import com.tech.cybercars.databinding.FragmentEnterMailBinding;
import com.tech.cybercars.ui.base.BaseFragment;
import com.tech.cybercars.ui.component.dialog.NotificationDialog;
import com.tech.cybercars.ui.signin.forgot_password.ForgotPasswordViewModel;

public class EnterMailFragment extends BaseFragment<FragmentEnterMailBinding, ForgotPasswordViewModel> {


    @NonNull
    @Override
    protected ForgotPasswordViewModel InitViewModel() {
        return new ViewModelProvider(requireActivity()).get(ForgotPasswordViewModel.class);
    }

    @Override
    protected FragmentEnterMailBinding InitBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_enter_mail, container, false);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {

        binding.btnNext.setOnClickListener(view -> {
            view_model.HandleForgotPassword();
        });
    }

    @Override
    protected void InitObserve() {
        view_model.is_send_mail_success.observe(this, is_send_mail_success -> {
            NotificationDialog.Builder(requireContext())
                    .SetIcon(R.drawable.ic_success)
                    .SetTitle(getString(R.string.success))
                    .SetSubtitle(getString(R.string.we_have_sent_the_otp_code_to_your_email))
                    .SetTextMainButton(getResources().getString(R.string.next))
                    .SetOnMainButtonClicked(dialog-> {
                        dialog.dismiss();
                        view_model.current_step.setValue(view_model.ENTER_OTP_STEP);
                    }).show();

        });

        view_model.email_error.observe(this, email_error -> {
            binding.inputEmailSignIn.setError(email_error);
        });

        view_model.email.observe(this, email -> {
            if(!email.equals("")){
                binding.inputEmailSignIn.setError(null);
            }
        });
    }

    @Override
    protected void InitCommon() {

    }
}