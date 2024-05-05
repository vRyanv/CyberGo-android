package com.tech.cybercars.ui.main.feedback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.tech.cybercars.R;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.data.models.TripManagement;
import com.tech.cybercars.databinding.ActivityFeedbackBinding;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.component.dialog.NotificationDialog;
import com.tech.cybercars.ui.main.fragment.go.find_trip.FindTripViewModel;

public class FeedbackActivity extends BaseActivity<ActivityFeedbackBinding, FeedbackViewModel> {

    @NonNull
    @Override
    protected FeedbackViewModel InitViewModel() {
        return new ViewModelProvider(this).get(FeedbackViewModel.class);
    }

    @Override
    protected ActivityFeedbackBinding InitBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_feedback);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {
        binding.ratingBar.setRating(5);

        binding.btnOutScreen.setOnClickListener(view -> {
            OnBackPress();
        });

        binding.btnSend.setOnClickListener(view -> {
            ShowSuccessFeedback();
        });
    }


    @Override
    protected void InitObserve() {

    }

    @Override
    protected void InitCommon() {
        String avatar = getIntent().getStringExtra(FieldName.AVATAR);
        String avatar_full_path = URL.BASE_URL + URL.AVATAR_RES_PATH + avatar;
        Glide.with(this).load(avatar_full_path).into(binding.imgAvatar);

        String full_name = getIntent().getStringExtra(FieldName.FULL_NAME);
        binding.txtFullName.setText(full_name);
    }

    @Override
    protected void OnBackPress() {
        finish();
    }

    private void ShowSuccessFeedback() {
        NotificationDialog.Builder(this)
                .SetIcon(R.drawable.ic_success)
                .SetTitle(getResources().getString(R.string.success))
                .SetSubtitle(getResources().getString(R.string.feedback_has_been_sent))
                .SetTextMainButton(getResources().getString(R.string.go_back))
                .SetOnMainButtonClicked(dialog -> {
                    finish();
                }).show();
    }

}