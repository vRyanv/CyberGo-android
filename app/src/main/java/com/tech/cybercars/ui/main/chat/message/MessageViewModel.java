package com.tech.cybercars.ui.main.chat.message;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.DelayTime;
import com.tech.cybercars.constant.StatusCode;
import com.tech.cybercars.data.local.AppDBContext;
import com.tech.cybercars.data.local.chat.ChatDAO;
import com.tech.cybercars.data.local.chat.MessageDAO;
import com.tech.cybercars.data.models.User;
import com.tech.cybercars.data.models.chat.Message;
import com.tech.cybercars.data.remote.chat.PrivateChatResponse;
import com.tech.cybercars.data.remote.user.profile.ProfileResponse;
import com.tech.cybercars.data.repositories.ChatRepository;
import com.tech.cybercars.ui.base.BaseViewModel;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class MessageViewModel extends BaseViewModel {
    public MutableLiveData<List<Message>> message_list = new MutableLiveData<>();
    public String chat_id;
    public String user_receive_id;
    private final ChatRepository chat_repo;
    private final MessageDAO message_dao;
    private final ChatDAO chat_dao;

    public MessageViewModel(@NonNull Application application) {
        super(application);
        chat_repo = ChatRepository.GetInstance();
        message_dao = AppDBContext.GetInstance(application).MessageDAO();
        chat_dao = AppDBContext.GetInstance(application).ChatDAO();
    }

    public void HandleGetMessageList() {
        List<Message> messages;
        if(!chat_id.equals("")){
            messages = message_dao.GetMessageList(chat_id);
        } else {
            messages = message_dao.GetMessageListByReceiveId(user_receive_id);
        }

        if(messages.size() > 0){
            message_list.setValue(messages);
            return;
        }

        LoadDataFromServer();
    }

    public void LoadDataFromServer() {
        is_loading.setValue(true);
        String user_token = SharedPreferencesUtil.GetString(getApplication(), SharedPreferencesUtil.USER_TOKEN_KEY);
        chat_repo.GetPrivateChat(
                user_token,
                user_receive_id,
                this::CallPrivateChatSuccess,
                this::CallPrivateChatFailed
        );
    }

    private void CallPrivateChatSuccess(Response<PrivateChatResponse> response) {
        new Handler().postDelayed(() -> {
            if(!response.isSuccessful() || response.body() == null){
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
                is_loading.postValue(false);
                return;
            }
            if (response.body().code == StatusCode.OK) {
                ExecutorService executor_service = Executors.newSingleThreadExecutor();
                executor_service.execute(()-> {
                    message_dao.ClearTable();
                    if(response.body().is_new_chat){
                        chat_dao.InsertChat(response.body().chat);
                        // Event push new chat
                    }
                    message_dao.InsertMessage(response.body().chat.message_list);
                    message_list.postValue(response.body().chat.message_list);
                });
                executor_service.shutdown();
            } else if (response.body().code == StatusCode.NOT_FOUND) {
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
            }
            is_loading.postValue(false);
        }, DelayTime.CALL_API);
    }

    private void CallPrivateChatFailed(Throwable error) {
        is_loading.postValue(false);
        error_call_server.postValue(getApplication().getString(R.string.can_not_connect_to_server));
    }

}
