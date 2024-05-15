package com.tech.cybercars.data.repositories;

import com.tech.cybercars.data.remote.chat.ChatListResponse;
import com.tech.cybercars.data.remote.chat.ChatServiceRetrofit;
import com.tech.cybercars.data.remote.chat.PrivateChatResponse;
import com.tech.cybercars.data.remote.rating.RatingListResponse;
import com.tech.cybercars.data.remote.rating.RatingServiceRetrofit;
import com.tech.cybercars.data.remote.retrofit.ResFailCallback;
import com.tech.cybercars.data.remote.retrofit.ResSuccessCallback;
import com.tech.cybercars.data.remote.retrofit.RetrofitRequest;
import com.tech.cybercars.data.remote.retrofit.RetrofitResponse;

public class ChatRepository {
    private final ChatServiceRetrofit chat_service;
    private static ChatRepository chat_repository;
    public static ChatRepository GetInstance(){
        if(chat_repository == null){
            chat_repository = new ChatRepository();
        }
        return chat_repository;
    }

    private ChatRepository() {
        chat_service = RetrofitRequest.getInstance().create(ChatServiceRetrofit.class);
    }

    public void GetPrivateChat(String user_token, String receiver_id, ResSuccessCallback<PrivateChatResponse> success_callback, ResFailCallback fail_callback){
        chat_service.GetPrivateChatRequest(user_token, receiver_id)
                .enqueue(new RetrofitResponse<PrivateChatResponse>().GetResponse(success_callback, fail_callback));
    }

    public void GetChatList(String user_token, ResSuccessCallback<ChatListResponse> success_callback, ResFailCallback fail_callback){
        chat_service.GetChatListRequest(user_token)
                .enqueue(new RetrofitResponse<ChatListResponse>().GetResponse(success_callback, fail_callback));
    }
}
