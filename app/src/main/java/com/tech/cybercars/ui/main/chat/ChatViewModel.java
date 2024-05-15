package com.tech.cybercars.ui.main.chat;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.DelayTime;
import com.tech.cybercars.constant.StatusCode;
import com.tech.cybercars.data.local.AppDBContext;
import com.tech.cybercars.data.local.chat.ChatDAO;
import com.tech.cybercars.data.models.chat.Chat;
import com.tech.cybercars.data.remote.chat.ChatListResponse;
import com.tech.cybercars.data.repositories.ChatRepository;
import com.tech.cybercars.ui.base.BaseViewModel;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class ChatViewModel extends BaseViewModel {
    public MutableLiveData<List<Chat>> chat_list = new MutableLiveData<>();
    private final ChatRepository chat_repo;
    private final ChatDAO chat_dao;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        chat_repo = ChatRepository.GetInstance();
        chat_dao = AppDBContext.GetInstance(application).ChatDAO();

    }

    public void HandleLoadChatList() {
        List<Chat> chats = chat_dao.GetAllChat();
        if (chats.size() > 0) {
            chat_list.setValue(chats);
            return;
        }

        LoadDataFromServer();
    }

    public void LoadDataFromServer() {
        is_loading.setValue(true);
        String user_token = SharedPreferencesUtil.GetString(getApplication(), SharedPreferencesUtil.USER_TOKEN_KEY);
        chat_repo.GetChatList(
                user_token,
                this::CallGetChatListSuccess,
                this::CallGetChatListFailed
        );
    }

    private void CallGetChatListSuccess(Response<ChatListResponse> response) {
        new Handler().postDelayed(() -> {
            if (!response.isSuccessful() || response.body() == null) {
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
                is_loading.postValue(false);
                return;
            }
            if (response.body().code == StatusCode.OK) {
                ExecutorService executor_service = Executors.newSingleThreadExecutor();
                executor_service.execute(() -> {
                    chat_dao.ClearTable();
                    chat_dao.InsertChat(response.body().chat_list);
                    chat_list.postValue(response.body().chat_list);
                });
                executor_service.shutdown();
            } else{
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
            }
            is_loading.postValue(false);
        }, DelayTime.CALL_API);
    }


    private void CallGetChatListFailed(Throwable error) {
        is_loading.postValue(false);
        error_call_server.postValue(getApplication().getString(R.string.can_not_connect_to_server));
    }
}
