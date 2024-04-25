package com.tech.cybercars.ui.main.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.tech.cybercars.R;
import com.tech.cybercars.databinding.ActivityChatBinding;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.main.chat.message.MessageViewModel;

public class ChatActivity extends BaseActivity<ActivityChatBinding, ChatViewModel> {

    @NonNull
    @Override
    protected ChatViewModel InitViewModel() {
        return new ViewModelProvider(this).get(ChatViewModel.class);
    }

    @Override
    protected ActivityChatBinding InitBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
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