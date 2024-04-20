package com.tech.cybercars.adapter.member;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tech.cybercars.R;
import com.tech.cybercars.adapter.notification.NotificationViewHolder;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.data.models.TripFound;

import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberViewHolder> {
    private List<TripFound.User> member_list;
    private Context context;
    private MemberClickedCallback member_clicked_callback;

    public MemberAdapter(Context context, List<TripFound.User> member_list) {
        this.member_list = member_list;
        this.context = context;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip_member, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        TripFound.User member = member_list.get(position);

        String avatar_full_path = URL.BASE_URL + URL.AVATAR_RES_PATH + member.avatar;
        Glide.with(context)
                .load(avatar_full_path)
                .into(holder.img_avatar);
        holder.txt_full_name.setText(member.full_name);
        holder.rating_bar.setRating(member.rating);
        holder.itemView.setOnClickListener(view -> {
            member_clicked_callback.OnCLicked(member.user_id);
        });
    }

    @Override
    public int getItemCount() {
        return member_list.size();
    }

    public void UpdateData(List<TripFound.User> member_list){
        this.member_list = member_list;
        this.notifyDataSetChanged();
    }

    public void SetMemberClicked(MemberClickedCallback member_clicked_callback){
        this.member_clicked_callback = member_clicked_callback;
    }

    public interface MemberClickedCallback{
        void OnCLicked(String user_id);
    }
}
