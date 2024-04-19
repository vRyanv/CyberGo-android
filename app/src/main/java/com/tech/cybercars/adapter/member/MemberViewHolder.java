package com.tech.cybercars.adapter.member;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tech.cybercars.R;
import com.willy.ratingbar.BaseRatingBar;

public class MemberViewHolder extends RecyclerView.ViewHolder {
    public ImageView img_avatar;
    public BaseRatingBar rating_bar;
    public TextView txt_full_name;
    public MemberViewHolder(@NonNull View itemView) {
        super(itemView);
        this.img_avatar = itemView.findViewById(R.id.img_avatar);
        this.rating_bar = itemView.findViewById(R.id.rating_bar);
        this.txt_full_name = itemView.findViewById(R.id.txt_full_name);

    }
}
