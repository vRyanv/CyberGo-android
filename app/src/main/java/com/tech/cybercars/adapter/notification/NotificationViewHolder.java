package com.tech.cybercars.adapter.notification;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.tech.cybercars.R;

public class NotificationViewHolder extends RecyclerView.ViewHolder {
    public TextView txt_time;
    public RoundedImageView img_avatar;
    public TextView txt_title;
    public TextView txt_content;
    public NotificationViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_time = itemView.findViewById(R.id.txt_time);
        img_avatar = itemView.findViewById(R.id.img_avatar);
        txt_title = itemView.findViewById(R.id.txt_title);
        txt_content = itemView.findViewById(R.id.txt_content);
    }
}
