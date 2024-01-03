package com.tech.cybercars.ui.base;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;

public abstract class BaseActivity<BINDING extends ViewDataBinding, VM extends ViewModel> extends AppCompatActivity{
    protected VM view_model;
    protected BINDING binding;
    @NonNull
    protected abstract VM InitViewModel();

    public abstract BINDING InitBinding(ViewModel view_model);

    protected abstract void InitView();

    protected abstract void InitObserve();

    protected abstract void InitCommon();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view_model = InitViewModel();
        binding = InitBinding(view_model);
        binding.setLifecycleOwner(this);
        InitView();
        InitObserve();
        InitCommon();
        binding.executePendingBindings();
    }
}
