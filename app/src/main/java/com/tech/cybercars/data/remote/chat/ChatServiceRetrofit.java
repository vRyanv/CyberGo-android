package com.tech.cybercars.data.remote.chat;

import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.URL;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ChatServiceRetrofit {
    @GET(URL.PRIVATE_CHAT)
    Call<PrivateChatResponse> GetPrivateChatRequest(
            @Header("authorization") String user_token,
            @Path(FieldName.RECEIVER_ID) String receiver_id
    );
    @GET(URL.CHAT_LIST)
    Call<ChatListResponse> GetChatListRequest(
            @Header("authorization") String user_token
    );
}
