package com.tech.cybercars.ui.main.chat;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tech.cybercars.R;
import com.tech.cybercars.adapter.chat.ChatAdapter;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.databinding.ActivityChatBinding;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.main.chat.message.MessageActivity;

import java.util.ArrayList;

public class ChatActivity extends BaseActivity<ActivityChatBinding, ChatViewModel> {
    private ChatAdapter chat_adapter;
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
        chat_adapter = new ChatAdapter(this, new ArrayList<>());
        chat_adapter.SetOnChatSelected(chat -> {
            Intent message_intent = new Intent(this, MessageActivity.class);
            message_intent.putExtra(FieldName.CHAT, chat);
            startActivity(message_intent);
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rcvChat.setLayoutManager(layoutManager);
        binding.rcvChat.setAdapter(chat_adapter);
    }

    @Override
    protected void InitObserve() {

    }

    @Override
    protected void InitCommon() {
        view_model.HandleLoadChatList();
    }

    @Override
    protected void OnBackPress() {
        finish();
    }
}