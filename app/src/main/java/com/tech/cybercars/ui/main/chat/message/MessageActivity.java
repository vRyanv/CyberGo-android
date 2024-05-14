package com.tech.cybercars.ui.main.chat.message;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tech.cybercars.R;
import com.tech.cybercars.adapter.message.MessageAdapter;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.databinding.ActivityMessageBinding;
import com.tech.cybercars.ui.base.BaseActivity;

import java.util.ArrayList;

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
        String user_receive_avatar = getIntent().getStringExtra(FieldName.AVATAR);
        String user_receive_full_name = getIntent().getStringExtra(FieldName.FULL_NAME);
        String avatar_full_path = URL.BASE_URL + URL.AVATAR_RES_PATH + user_receive_avatar;
        Glide.with(this).load(avatar_full_path).into(binding.imgAvatar);

        binding.txtFullName.setText(user_receive_full_name);


        binding.btnOutScreen.setOnClickListener(view -> {
            OnBackPress();
        });

        message_adapter = new MessageAdapter(this, new ArrayList<>());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rcvMessage.setLayoutManager(layoutManager);
        binding.rcvMessage.setAdapter(message_adapter);
    }

    @Override
    protected void InitObserve() {
        view_model.message_list.observe(this, message_list -> {
            message_adapter.UpdateData(message_list);
        });
    }

    @Override
    protected void InitCommon() {
        view_model.user_receive_id = getIntent().getStringExtra(FieldName.USER_ID);
        view_model.chat_id = getIntent().getStringExtra(FieldName.CHAT);
        view_model.HandleGetMessageList();
    }

    @Override
    protected void OnBackPress() {
        finish();
    }
}