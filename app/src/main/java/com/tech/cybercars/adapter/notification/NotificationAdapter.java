package com.tech.cybercars.adapter.notification;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tech.cybercars.R;
import com.tech.cybercars.constant.TimeZone;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.data.models.Notification;
import com.tech.cybercars.utils.DateConverter;
import com.tech.cybercars.utils.Helper;

import java.time.LocalDateTime;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder> {
    private List<Notification> notification_list;
    private final Context context;

    public NotificationAdapter(Context context, List<Notification> notification_list) {
        this.notification_list = notification_list;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notification_list.get(position);
        LocalDateTime datetime = DateConverter.TimestampsToDate(notification.datetime, TimeZone.VIETNAM);
        String date = Helper.PadStart(datetime.getMonthValue(), 2) + "/"
                + Helper.PadStart(datetime.getDayOfMonth(), 2) + "/"
                + datetime.getYear();
        String time = Helper.PadStart(datetime.getHour(),2) + ":" + Helper.PadStart(datetime.getMinute(),2);
        String full_time = time + " " + date;
        holder.txt_time.setText(full_time);
        String avatar_full_path = URL.BASE_URL + URL.AVATAR_RES_PATH + notification.avatar;
        Glide.with(context)
                .load(avatar_full_path)
                .into(holder.img_avatar);
        holder.txt_title.setText(notification.title);
        holder.txt_content.setText(notification.content);
    }

    @Override
    public int getItemCount() {
        return notification_list.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void UpdateData(List<Notification> notification_list) {
        this.notification_list = notification_list;
        this.notifyDataSetChanged();
    }
}
