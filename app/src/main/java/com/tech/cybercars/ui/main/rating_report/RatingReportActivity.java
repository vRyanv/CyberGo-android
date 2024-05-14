package com.tech.cybercars.ui.main.rating_report;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tech.cybercars.R;
import com.tech.cybercars.adapter.rating_report.RatingReportAdapter;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.data.models.Rating;
import com.tech.cybercars.databinding.ActivityRatingReportBinding;
import com.tech.cybercars.ui.base.BaseActivity;

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
        rating_report_adapter = new RatingReportAdapter(this, new ArrayList<>());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rcvRatingReport.setLayoutManager(layoutManager);
        binding.rcvRatingReport.setAdapter(rating_report_adapter);

        binding.headerPrimary.btnOutScreen.setOnClickListener(view -> {
            OnBackPress();
        });


        binding.swipeRefresh.setColorSchemeColors(getColor(R.color.orange));
        binding.swipeRefresh.setOnRefreshListener(() -> {
            view_model.HandleLoadDataFromServer();
        });
    }

    @Override
    protected void InitObserve() {
        view_model.is_loading.observe(this, is_loading-> {
            if(is_loading){
                binding.skeletonLoading.startShimmerAnimation();
            } else {
                binding.swipeRefresh.setRefreshing(false);
                binding.skeletonLoading.stopShimmerAnimation();
            }
        });

        view_model.rating_list.observe(this, rating_list -> {
            String count = rating_list.size() + " " + getString(R.string.rating);
            binding.txtRatingCount.setText(count);
            rating_report_adapter.UpdateData(rating_list);
        });

        view_model.error_call_server.observe(this, this::ShowErrorDialog);
    }

    @Override
    protected void InitCommon() {
        view_model.user_receive_id = getIntent().getStringExtra(FieldName.USER_ID);
        view_model.HandleLoadDataFromServer();
    }

    @Override
    protected void OnBackPress() {
        finish();
    }
}