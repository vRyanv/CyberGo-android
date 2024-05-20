package com.tech.cybercars.ui.signin.forgot_password.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.tech.cybercars.R;
import com.tech.cybercars.databinding.FragmentEnterOTPBinding;
import com.tech.cybercars.ui.base.BaseFragment;
import com.tech.cybercars.ui.component.dialog.NotificationDialog;
import com.tech.cybercars.ui.signin.forgot_password.ForgotPasswordViewModel;
import com.tech.cybercars.utils.KeyBoardUtil;

public class EnterOTPFragment extends BaseFragment<FragmentEnterOTPBinding, ForgotPasswordViewModel> {
    @NonNull
    @Override
    protected ForgotPasswordViewModel InitViewModel() {
        return new ViewModelProvider(requireActivity()).get(ForgotPasswordViewModel.class);
    }

    @Override
    protected FragmentEnterOTPBinding InitBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_enter_o_t_p, container, false);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {
        binding.btnPrevious.setOnClickListener(view -> {
            view_model.current_step.setValue(0);
        });

        binding.btnUpdatePass.setOnClickListener(view -> {
            KeyBoardUtil.HideKeyBoard(requireActivity());
            view_model.HandleResetPassword();
        });
    }

    @Override
    protected void InitObserve() {
        view_model.is_success.observe(this, is_success -> {
            ShowUpdatePassSuccess();
        });

        view_model.otp_code.observe(this, otp_code -> {
            if(!otp_code.equals("")){
                binding.inputOtpCode.setError(null);
            }
        });
        view_model.otp_code_error.observe(this, otp_code_error -> {
            binding.inputOtpCode.setError(otp_code_error);
        });

        view_model.new_pass.observe(this, new_pass -> {
            if(!new_pass.equals("")){
                binding.inputNewPass.setError(null);
            }
        });
        view_model.new_pass_error.observe(this, new_pass_error -> {
            binding.inputNewPass.setError(new_pass_error);
        });

        view_model.confirm_pass.observe(this, confirm_pass -> {
            if(!confirm_pass.equals("")){
                binding.inputConfirmPass.setError(null);
            }
        });
        view_model.confirm_pass_error.observe(this, confirm_pass_error -> {
            binding.inputConfirmPass.setError(confirm_pass_error);
        });
    }

    @Override
    protected void InitCommon() {

    }

    private void ShowUpdatePassSuccess() {
        NotificationDialog.Builder(requireContext())
                .SetIcon(R.drawable.ic_success)
                .SetTitle(getResources().getString(R.string.success))
                .SetSubtitle(getResources().getString(R.string.update_pass_success))
                .SetTextMainButton(getResources().getString(R.string.login))
                .SetOnMainButtonClicked(dialog -> {
                    requireActivity().finish();
                }).show();
    }
}