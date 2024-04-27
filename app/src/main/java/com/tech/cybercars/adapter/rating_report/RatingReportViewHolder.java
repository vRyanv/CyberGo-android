package com.tech.cybercars.adapter.rating_report;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tech.cybercars.R;

public class RatingReportViewHolder extends RecyclerView.ViewHolder {
    public ImageView img_avatar;
    public TextView txt_full_name;
    public TextView txt_date;
    public TextView txt_content;
    public RatingReportViewHolder(@NonNull View itemView) {
        super(itemView);
        img_avatar = itemView.findViewById(R.id.img_avatar);
        txt_full_name = itemView.findViewById(R.id.txt_full_name);
        txt_date = itemView.findViewById(R.id.txt_date);
        txt_content = itemView.findViewById(R.id.txt_content);
    }
}
