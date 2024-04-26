package com.tech.cybercars.adapter.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tech.cybercars.R;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.data.models.Chat;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {
    private List<Chat> chat_list;
    private Context context;
    private ChatSelectionCallback chat_selection_callback;

    public ChatAdapter(Context context, List<Chat> chat_list) {
        this.chat_list = chat_list;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chat_list.get(position);

        String avatar_full_path = URL.BASE_URL + URL.AVATAR_RES_PATH + "seed/" + chat.sender_avatar;
        Glide.with(context).load(avatar_full_path).into(holder.img_avatar);

        holder.txt_full_name.setText(chat.sender_full_name);
        holder.txt_last_message.setText(chat.last_message);
        holder.txt_last_message_time.setText(chat.last_message_time);

        if (chat.has_message) {
            holder.icon_has_message.setVisibility(View.VISIBLE);
        }

        if (chat.is_online) {
            holder.icon_online_status.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(view -> {
            chat_selection_callback.OnSelected(chat);
        });
    }

    @Override
    public int getItemCount() {
        return chat_list.size();
    }

    public void SetOnChatSelected(ChatSelectionCallback chat_selection_callback) {
        this.chat_selection_callback = chat_selection_callback;
    }

    public interface ChatSelectionCallback {
        void OnSelected(Chat chat);
    }
}
