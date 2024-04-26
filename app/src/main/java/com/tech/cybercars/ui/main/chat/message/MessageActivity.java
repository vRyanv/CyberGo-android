package com.tech.cybercars.ui.main.chat.message;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tech.cybercars.R;
import com.tech.cybercars.adapter.chat.ChatAdapter;
import com.tech.cybercars.adapter.message.MessageAdapter;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.data.models.Chat;
import com.tech.cybercars.data.models.Message;
import com.tech.cybercars.databinding.ActivityMessageBinding;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.main.MainViewModel;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends BaseActivity<ActivityMessageBinding, MessageViewModel> {
    private MessageAdapter message_adapter;
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
        Chat chat = (Chat) getIntent().getSerializableExtra(FieldName.CHAT);
        assert chat != null;

        String avatar_full_path = URL.BASE_URL + URL.AVATAR_RES_PATH + "seed/" + chat.sender_avatar;
        Glide.with(this).load(avatar_full_path).into(binding.imgAvatar);
        binding.txtFullName.setText(chat.sender_full_name);


        binding.btnOutScreen.setOnClickListener(view -> {
            OnBackPress();
        });

        List<Message> message_list = new ArrayList<>();
        String[] avatars = new String[]{"truc.jpg", "ryan.jpg", "truc.jpg", "ryan.jpg"};
        String[] contents = new String[]{"hi!", "what's up?", ">.<", "-.-"};
        String[] times = new String[]{"21:08", "21:08", "22:00", "22:00"};
        String current_user_id = SharedPreferencesUtil.GetString(this, FieldName.USER_ID);
        String[] user_ids = new String[]{"1", current_user_id, "1", current_user_id};
        for (int i = 0; i < 4; i++) {
            Message mess = new Message(
                    String.valueOf(i),
                    String.valueOf(i),
                    user_ids[i],
                    avatars[i],
                    contents[i],
                    times[i]
            );
            message_list.add(mess);
        }
        message_adapter = new MessageAdapter(this, message_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rcvMessage.setLayoutManager(layoutManager);
        binding.rcvMessage.setAdapter(message_adapter);
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