package com.tech.cybercars.ui.main.chat;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tech.cybercars.R;
import com.tech.cybercars.adapter.chat.ChatAdapter;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.data.models.chat.Chat;
import com.tech.cybercars.data.models.chat.Message;
import com.tech.cybercars.databinding.ActivityChatBinding;
import com.tech.cybercars.services.eventbus.ChatEvent;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.main.chat.message.MessageActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
        EventBus.getDefault().register(this);
    }

    @Override
    protected void InitView() {
        chat_adapter = new ChatAdapter(this, new ArrayList<>());
        chat_adapter.SetOnChatSelected(chat -> {
            Intent message_intent = new Intent(this, MessageActivity.class);
            message_intent.putExtra(FieldName.RECEIVER_ID, chat.receiver_id);
            message_intent.putExtra(FieldName.AVATAR, chat.receiver_avatar);
            message_intent.putExtra(FieldName.FULL_NAME, chat.receiver_full_name);
            startActivity(message_intent);
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rcvChat.setLayoutManager(layoutManager);
        binding.rcvChat.setAdapter(chat_adapter);

        binding.swipeRefresh.setColorSchemeColors(getColor(R.color.orange));
        binding.swipeRefresh.setOnRefreshListener(() -> {
            view_model.LoadDataFromServer();
        });

        binding.headerPrimary.btnOutScreen.setOnClickListener(view -> {
            finish();
        });
    }

    @Override
    protected void InitObserve() {
        view_model.chat_list.observe(this, chat_list -> {
            chat_adapter.UpdateData(chat_list);
        });

        view_model.is_loading.observe(this, is_loading -> {
            if (is_loading) {
                binding.skeletonLoading.startShimmerAnimation();
            } else {
                binding.swipeRefresh.setRefreshing(false);
                binding.skeletonLoading.stopShimmerAnimation();
            }
        });

        view_model.error_call_server.observe(this, this::ShowErrorDialog);
    }

    @Override
    protected void InitCommon() {
        view_model.HandleLoadChatList();
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

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void OnNewChatEvent(ChatEvent chat_event) {
        if (chat_event.action.equals(ChatEvent.NEW_CHAT)) {
            Chat chat = view_model.chat_list.getValue().stream()
                    .filter(c -> c.chat_id.equals(chat_event.chat.chat_id))
                    .findAny()
                    .orElse(null);

            if (chat != null) {
                return;
            }
            view_model.chat_list.getValue().add(chat_event.chat);

            Handler main_handler = new Handler(Looper.getMainLooper());
            main_handler.post(() -> {
                chat_adapter.UpdateData(view_model.chat_list.getValue());
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void OnMessageEvent(Message message) {
        String chat_id = message.chat_id;
        Chat chat = view_model.chat_list.getValue().stream()
                .filter(c -> c.chat_id.equals(chat_id))
                .findAny()
                .orElse(null);
        if(chat != null){
            chat.last_message = message.content;
            chat.last_message_time = message.send_time;

            Handler main_handler = new Handler(Looper.getMainLooper());
            main_handler.post(() -> {
                chat_adapter.UpdateData(view_model.chat_list.getValue());
            });
            return;
        }

        Handler main_handler = new Handler(Looper.getMainLooper());
        main_handler.post(() -> {
            view_model.LoadDataFromServer();
        });

    }
}