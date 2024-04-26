package com.tech.cybercars.adapter.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.data.models.Message;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter {
    private String current_user_id;
    private final int RIGHT_MESSAGE = 1;
    private final int LEFT_MESSAGE = 0;
    private List<Message> message_list;
    private Context context;

    public MessageAdapter(Context context, List<Message> message_list) {
        this.message_list = message_list;
        this.context = context;
        current_user_id = SharedPreferencesUtil.GetString(context, FieldName.USER_ID);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == LEFT_MESSAGE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_left_message, parent, false);
            return new MessageLeftViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_right_message, parent, false);
            return new MessageRightViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = message_list.get(position);
        if (holder instanceof MessageLeftViewHolder) {
            MessageLeftViewHolder left_message = (MessageLeftViewHolder) holder;
            left_message.BindData(context, message);
        } else {
            MessageRightViewHolder left_message = (MessageRightViewHolder) holder;
            left_message.BindData(context, message);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = message_list.get(position);
        if (current_user_id.equals(message.sender_id)) {
            return RIGHT_MESSAGE;
        } else {
            return LEFT_MESSAGE;
        }
    }

    @Override
    public int getItemCount() {
        return message_list.size();
    }
}
