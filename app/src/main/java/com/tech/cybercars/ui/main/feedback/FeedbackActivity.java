package com.tech.cybercars.ui.main.feedback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.tech.cybercars.R;
import com.tech.cybercars.databinding.ActivityFeedbackBinding;
import com.tech.cybercars.ui.base.BaseActivity;

public class FeedbackActivity extends BaseActivity<ActivityFeedbackBinding, FeedbackViewModel> {

    @NonNull
    @Override
    protected FeedbackViewModel InitViewModel() {
        return null;
    }

    @Override
    protected ActivityFeedbackBinding InitBinding() {
        return null;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {

    }

    @Override
    protected void InitObserve() {

    }

    @Override
    protected void InitCommon() {

    }

    @Override
    protected void OnBackPress() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
    }
}