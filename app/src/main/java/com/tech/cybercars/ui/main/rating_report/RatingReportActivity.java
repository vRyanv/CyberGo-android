package com.tech.cybercars.ui.main.rating_report;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.tech.cybercars.R;
import com.tech.cybercars.adapter.notification.NotificationAdapter;
import com.tech.cybercars.adapter.rating_report.RatingReportAdapter;
import com.tech.cybercars.data.models.Rating;
import com.tech.cybercars.databinding.ActivityRatingReportBinding;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.main.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class RatingReportActivity extends BaseActivity<ActivityRatingReportBinding, RatingReportViewModel> {
    private RatingReportAdapter rating_report_adapter;
    @NonNull
    @Override
    protected RatingReportViewModel InitViewModel() {
        return new ViewModelProvider(this).get(RatingReportViewModel.class);
    }

    @Override
    protected ActivityRatingReportBinding InitBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rating_report);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {
        List<Rating> rating_list = new ArrayList<>();
        Rating rating = new Rating();
        Rating.User user_rating = new Rating.User();
        user_rating.avatar = "1713681127628_72860647104981271050_cover.webp1701365331478.webp";
        user_rating.full_name = "Ryan";
        rating.user_rating = user_rating;
        rating.star = 5;
        rating.comment = "Good!";
        rating_list.add(rating);

        rating_report_adapter = new RatingReportAdapter(this, rating_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rcvRatingReport.setLayoutManager(layoutManager);
        binding.rcvRatingReport.setAdapter(rating_report_adapter);

        binding.headerPrimary.btnOutScreen.setOnClickListener(view -> {
            OnBackPress();
        });
    }

    @Override
    protected void InitObserve() {

    }

    @Override
    protected void InitCommon() {

    }

    @Override
    protected void OnBackPress() {
        finish();
    }
}