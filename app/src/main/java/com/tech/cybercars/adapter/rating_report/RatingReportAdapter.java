package com.tech.cybercars.adapter.rating_report;

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
import com.tech.cybercars.data.models.Rating;
import com.tech.cybercars.utils.DateConverter;
import com.tech.cybercars.utils.DateUtil;
import com.tech.cybercars.utils.Helper;

import java.time.LocalDateTime;
import java.util.List;

public class RatingReportAdapter extends RecyclerView.Adapter<RatingReportViewHolder> {
    private List<Rating> rating_list;
    private Context context;
    public RatingReportAdapter(Context context, List<Rating> rating_list){
        this.rating_list = rating_list;
        this.context = context;
    }
    @NonNull
    @Override
    public RatingReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rating_report, parent, false);
        return new RatingReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingReportViewHolder holder, int position) {
        Rating rating = rating_list.get(position);

        String avatar_full_path = URL.BASE_URL + URL.AVATAR_RES_PATH + rating.user_send.avatar;
        Glide.with(context).load(avatar_full_path).into(holder.img_avatar);
        holder.txt_full_name.setText(rating.user_send.full_name);
        LocalDateTime datetime = DateConverter.TimestampsToDate(rating.date, TimeZone.VIETNAM);
        String date = Helper.PadStart(datetime.getMonthValue(), 2) + "/"
                + Helper.PadStart(datetime.getDayOfMonth(), 2) + "/"
                + datetime.getYear();
        holder.txt_date.setText(date);
        holder.txt_content.setText(rating.comment);
        holder.rating_bar.setRating(rating.star);
    }

    public void UpdateData(List<Rating> rating_list){
        this.rating_list = rating_list;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return rating_list.size();
    }
}
