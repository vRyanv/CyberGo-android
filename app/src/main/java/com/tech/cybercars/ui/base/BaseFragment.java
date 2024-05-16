package com.tech.cybercars.ui.base;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import com.tech.cybercars.R;
import com.tech.cybercars.ui.component.dialog.NotificationDialog;

public abstract class BaseFragment<BINDING extends ViewDataBinding, VM extends ViewModel> extends Fragment {
    protected VM view_model;
    protected BINDING binding;
    @NonNull
    protected abstract VM InitViewModel();

    protected abstract BINDING InitBinding(LayoutInflater inflater, ViewGroup container);

    protected abstract void InitFirst();
    protected abstract void InitView();

    protected abstract void InitObserve();

    protected abstract void InitCommon();
    protected Bundle savedInstanceState;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        InitFirst();
        view_model = InitViewModel();
        binding = InitBinding(inflater, container);
        binding.setLifecycleOwner(this);
        InitView();
        InitObserve();
        InitCommon();
        binding.executePendingBindings();
        return binding.getRoot();
    }

    public void ShowErrorDialog(String error_call_server) {
        NotificationDialog.Builder(requireContext())
                .SetIcon(R.drawable.ic_error)
                .SetTitle(getResources().getString(R.string.something_went_wrong))
                .SetSubtitle(error_call_server)
                .SetTextMainButton(getResources().getString(R.string.close))
                .SetOnMainButtonClicked(Dialog::dismiss).show();

    }
}
