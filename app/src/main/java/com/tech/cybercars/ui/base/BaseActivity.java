package com.tech.cybercars.ui.base;

import android.app.Dialog;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;

import com.tech.cybercars.R;
import com.tech.cybercars.ui.component.dialog.NotificationDialog;


public abstract class BaseActivity<BINDING extends ViewDataBinding, VM extends ViewModel> extends AppCompatActivity{
    protected VM view_model;
    protected BINDING binding;
    @NonNull
    protected abstract VM InitViewModel();

    protected abstract BINDING InitBinding();

    /**
     * start before init View
     */
    protected abstract void InitFirst();

    protected abstract void InitView();

    protected abstract void InitObserve();

    protected abstract void InitCommon();
    protected abstract void OnBackPress();
    protected Bundle savedInstanceState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        InitFirst();
        view_model = InitViewModel();
        binding = InitBinding();
        binding.setLifecycleOwner(this);
        InitView();
        InitObserve();
        InitCommon();
        binding.executePendingBindings();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                OnBackPress();
            }
        });
    }

    public void ShowErrorDialog(String error_call_server) {
        NotificationDialog.Builder(this)
                .SetIcon(R.drawable.ic_error)
                .SetTitle(getResources().getString(R.string.something_went_wrong))
                .SetSubtitle(error_call_server)
                .SetTextMainButton(getResources().getString(R.string.close))
                .SetOnMainButtonClicked(Dialog::dismiss).show();

    }



    private void ShowSuccessDialog(String title, String sub_title, String text_button, NotificationDialog.DialogCallback callback) {
        NotificationDialog.Builder(this)
                .SetIcon(R.drawable.ic_success)
                .SetTitle(title)
                .SetSubtitle(sub_title)
                .SetTextMainButton(text_button)
                .SetOnMainButtonClicked(callback)
                .show();
    }
}
