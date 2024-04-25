package com.tech.cybercars.ui.main.chat.message;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.tech.cybercars.R;
import com.tech.cybercars.databinding.ActivityMessageBinding;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.main.MainViewModel;

public class MessageActivity extends BaseActivity<ActivityMessageBinding, MessageViewModel> {

    @NonNull
    @Override
    protected MessageViewModel InitViewModel() {
        return new ViewModelProvider(this).get(MessageViewModel.class);
    }

    @Override
    protected ActivityMessageBinding InitBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_message);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {

    }

    @Override
    protected void InitObserve() {

    }

    @Override
    protected void InitCommon() {

    }

    @Override
    protected void OnBackPress() {

    }
}