package com.tech.cybercars.ui.main.chat;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tech.cybercars.R;
import com.tech.cybercars.adapter.chat.ChatAdapter;
import com.tech.cybercars.adapter.notification.NotificationAdapter;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.data.models.Chat;
import com.tech.cybercars.data.models.Message;
import com.tech.cybercars.databinding.ActivityChatBinding;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.main.chat.message.MessageActivity;
import com.tech.cybercars.ui.main.chat.message.MessageViewModel;

import java.util.ArrayList;
import java.util.List;

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
        String[] avatars = new String[]{"ryan.jpg", "truc.jpg", "lap.png", "vinh.png","nhan.png","chuong.png","thai.png","hung.png","duy.png","phuongduy.png"};
        String[] full_names = new String[]{"Ryan", "Le Truc", "Lucky", "Owen","Yummy","Chuong","Thai","Hung","ĐDuy","PDuy"};
        String[] messages = new String[]{"hello", "hi!", "thanks", "hi ^_^", "hi", "hello", "hello", "hello", "hello","hello"};
        List<Chat> chat_list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Chat chat = new Chat(
                    String.valueOf(i),
                    String.valueOf(i),
                    avatars[i],
                    full_names[i],
                    messages[i],
                    "21:08",
                    false,
                    true
            );
            chat_list.add(chat);
        }


        chat_adapter = new ChatAdapter(this, chat_list);
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

    }

    @Override
    protected void OnBackPress() {
        finish();
    }
}