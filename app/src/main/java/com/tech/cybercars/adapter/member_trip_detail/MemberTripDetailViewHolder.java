package com.tech.cybercars.adapter.member_trip_detail;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.tech.cybercars.R;
import com.willy.ratingbar.BaseRatingBar;

public class MemberTripDetailViewHolder extends RecyclerView.ViewHolder {
    public ImageView img_avatar;
    public BaseRatingBar rating_bar;
    public TextView txt_full_name;
    public ImageView btn_make_rating;
    public ImageView btn_open_view_location;
    public AppCompatButton btn_accept_member;
    public AppCompatButton btn_denied_member;
    public LinearLayout wrapper_control_member;
    public ImageView img_join_status;
    public MemberTripDetailViewHolder(@NonNull View itemView) {
        super(itemView);
        this.img_avatar = itemView.findViewById(R.id.img_avatar);
        this.rating_bar = itemView.findViewById(R.id.rating_bar);
        this.txt_full_name = itemView.findViewById(R.id.txt_full_name);
        this.btn_open_view_location = itemView.findViewById(R.id.btn_open_view_location);
        this.btn_make_rating = itemView.findViewById(R.id.btn_make_rating);
        this.btn_accept_member = itemView.findViewById(R.id.btn_accept_member);
        this.btn_denied_member = itemView.findViewById(R.id.btn_denied_member);
        this.wrapper_control_member = itemView.findViewById(R.id.wrapper_control_member);
        this.img_join_status = itemView.findViewById(R.id.img_join_status);

    }
}
