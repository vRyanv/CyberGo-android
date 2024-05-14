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
            ShowUpdatePassSuccess();
        });
    }

    @Override
    protected void InitObserve() {

    }

    @Override
    protected void InitCommon() {

    }

    private void ShowUpdatePassSuccess(){
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