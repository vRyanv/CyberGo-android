package com.tech.cybercars.adapter.message;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tech.cybercars.R;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.data.models.Message;

public class MessageRightViewHolder extends RecyclerView.ViewHolder {
    public ImageView img_avatar;
    public TextView txt_content;
    public TextView txt_time;
    public MessageRightViewHolder(@NonNull View itemView) {
        super(itemView);
        img_avatar = itemView.findViewById(R.id.img_avatar);
        txt_content = itemView.findViewById(R.id.txt_content);
        txt_time = itemView.findViewById(R.id.txt_time);
    }

    public void BindData(Context context, Message message){
        String avatar_full_path = URL.BASE_URL + URL.AVATAR_RES_PATH + "seed/" + message.avatar;
        Glide.with(context).load(avatar_full_path).into(img_avatar);

        txt_content.setText(message.content);
        txt_time.setText(message.time);
    }
}
