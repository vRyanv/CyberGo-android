package com.tech.cybercars.ui.base;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;


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

}
