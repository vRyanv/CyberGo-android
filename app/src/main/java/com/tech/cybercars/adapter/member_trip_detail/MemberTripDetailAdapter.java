package com.tech.cybercars.adapter.member_trip_detail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tech.cybercars.R;
import com.tech.cybercars.adapter.member.MemberViewHolder;
import com.tech.cybercars.constant.MemberStatus;
import com.tech.cybercars.constant.TripStatus;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.data.models.TripFound;
import com.tech.cybercars.data.models.TripManagement;

import java.util.List;

public class MemberTripDetailAdapter extends RecyclerView.Adapter<MemberTripDetailViewHolder> {
    private List<TripManagement.Member> member_list;
    private String trip_status = "";
    private String trip_owner_id = "";
    private String current_user_id = "";
    private final Context context;
    private MemberClickedCallback member_clicked_callback;
    private MemberClickedCallback make_rating_clicked_callback;
    private MemberClickedCallback accept_clicked_callback;
    private MemberClickedCallback denied_clicked_callback;

    public MemberTripDetailAdapter(Context context, List<TripManagement.Member> member_list) {
        this.member_list = member_list;
        this.context = context;
    }

    @NonNull
    @Override
    public MemberTripDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_memeber_trip_detail, parent, false);
        return new MemberTripDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberTripDetailViewHolder holder, int position) {
        TripManagement.Member member = member_list.get(position);

        String avatar_full_path = URL.BASE_URL + URL.AVATAR_RES_PATH + member.avatar;
        Glide.with(context)
                .load(avatar_full_path)
                .into(holder.img_avatar);
        holder.txt_full_name.setText(member.full_name);
        holder.rating_bar.setRating(member.rating);
        holder.itemView.setOnClickListener(view -> {
            member_clicked_callback.OnCLicked(member);
        });


        if(trip_status.equals(TripStatus.FINISH) && !member.user_id.equals(current_user_id)){
            holder.img_join_status.setVisibility(View.GONE);
            holder.btn_make_rating.setVisibility(View.VISIBLE);
            holder.btn_make_rating.setOnClickListener(view -> {
                make_rating_clicked_callback.OnCLicked(member);
            });
        } else {
            holder.img_join_status.setVisibility(View.VISIBLE);
            if(member.status.equals(MemberStatus.JOINED)){
                holder.img_join_status.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_success));
            } else {
                holder.img_join_status.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_sand_clock));
            }
        }

        if(trip_owner_id.equals(current_user_id) && !trip_status.equals(TripStatus.FINISH) && member.status.equals(MemberStatus.QUEUE)){
            holder.btn_accept_member.setOnClickListener(view -> {
                accept_clicked_callback.OnCLicked(member);
            });

            holder.btn_denied_member.setOnClickListener(view -> {
                denied_clicked_callback.OnCLicked(member);
            });
        } else {
            holder.wrapper_control_member.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return member_list.size();
    }

    public void SetTripCondition(String trip_status, String trip_owner_id, String current_user_id){
        this.trip_status = trip_status;
        this.trip_owner_id = trip_owner_id;
        this.current_user_id = current_user_id;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void UpdateData(List<TripManagement.Member> member_list){
        this.member_list = member_list;
        this.notifyDataSetChanged();
    }

    public void SetMemberClicked(MemberClickedCallback member_clicked_callback){
        this.member_clicked_callback = member_clicked_callback;
    }

    public void SetMakeRatingMemberClicked(MemberClickedCallback make_rating_clicked_callback){
        this.make_rating_clicked_callback = make_rating_clicked_callback;
    }


    public void SetAcceptMemberClicked(MemberClickedCallback accept_clicked_callback){
        this.accept_clicked_callback = accept_clicked_callback;
    }

    public void SetDeniedMemberClicked(MemberClickedCallback denied_clicked_callback){
        this.denied_clicked_callback = denied_clicked_callback;
    }

    public interface MemberClickedCallback{
        void OnCLicked(TripManagement.Member member);
    }
}
