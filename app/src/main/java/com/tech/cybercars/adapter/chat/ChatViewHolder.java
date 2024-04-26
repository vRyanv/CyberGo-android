package com.tech.cybercars.adapter.chat;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.tech.cybercars.R;

public class ChatViewHolder extends RecyclerView.ViewHolder {
    public ImageView img_avatar;
    public TextView txt_full_name;
    public TextView txt_last_message;
    public TextView txt_last_message_time;
    public ConstraintLayout icon_has_message;
    public ConstraintLayout icon_online_status;
    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);
        img_avatar = itemView.findViewById(R.id.img_avatar);
        txt_full_name = itemView.findViewById(R.id.txt_full_name);
        txt_last_message = itemView.findViewById(R.id.txt_last_message);
        txt_last_message_time = itemView.findViewById(R.id.txt_last_message_time);
        icon_has_message = itemView.findViewById(R.id.icon_has_message);
        icon_online_status = itemView.findViewById(R.id.icon_online_status);
    }
}
