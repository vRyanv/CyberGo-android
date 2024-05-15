package com.tech.cybercars.ui.main.chat.message;

import android.widget.Toast;

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
import com.tech.cybercars.data.models.chat.Message;
import com.tech.cybercars.databinding.ActivityMessageBinding;
import com.tech.cybercars.services.eventbus.SendMessEvent;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.utils.DateUtil;
import com.tech.cybercars.utils.KeyBoardUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
        EventBus.getDefault().register(this);
    }

    @Override
    protected void InitView() {
        binding.btnSend.setOnClickListener(view -> {
            SendMessage();
        });

        binding.btnOutScreen.setOnClickListener(view -> {
            OnBackPress();
        });

        message_adapter = new MessageAdapter(this, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        binding.rcvMessage.setLayoutManager(layoutManager);
        binding.rcvMessage.setAdapter(message_adapter);
    }

    @Override
    protected void InitObserve() {
        view_model.message_list.observe(this, message_list -> {
            message_adapter.UpdateData(message_list);
        });

        view_model.is_success.observe(this, is_success -> {
            binding.txtFullName.setText(view_model.receiver_full_name);
            String avatar_full_path = URL.BASE_URL + URL.AVATAR_RES_PATH + view_model.receiver_avatar;
            Glide.with(this).load(avatar_full_path).into(binding.imgAvatar);
        });
    }

    @Override
    protected void InitCommon() {
        view_model.receiver_id = getIntent().getStringExtra(FieldName.RECEIVER_ID);

        String avatar = getIntent().getStringExtra(FieldName.AVATAR);
        String avatar_full_path = URL.BASE_URL + URL.AVATAR_RES_PATH + avatar;
        Glide.with(this).load(avatar_full_path).into(binding.imgAvatar);

        String full_name = getIntent().getStringExtra(FieldName.FULL_NAME);
        binding.txtFullName.setText(full_name);

        view_model.HandleGetMessageList();
    }

    @Override
    protected void OnBackPress() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void SendMessage(){
        String mess = binding.inputMess.getText().toString();
        if(mess.equals("")){
            return;
        }

        KeyBoardUtil.HideKeyBoard(this);
        binding.inputMess.setText("");

        SendMessEvent send_mess_event = new SendMessEvent();
        send_mess_event.message = mess;
        send_mess_event.receiver_id = view_model.receiver_id;

        EventBus.getDefault().post(send_mess_event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(Message message){
        view_model.message_list.getValue().add(message);
        message_adapter.UpdateData(view_model.message_list.getValue());
        binding.rcvMessage.scrollToPosition(view_model.message_list.getValue().size() - 1);
    }
}