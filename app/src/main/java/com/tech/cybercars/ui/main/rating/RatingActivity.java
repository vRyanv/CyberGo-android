package com.tech.cybercars.ui.main.rating;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.tech.cybercars.R;
import com.tech.cybercars.constant.ActivityResult;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.databinding.ActivityRatingBinding;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.component.dialog.NotificationDialog;
import com.willy.ratingbar.BaseRatingBar;

public class RatingActivity extends BaseActivity<ActivityRatingBinding, RatingViewModel> {

    @NonNull
    @Override
    protected RatingViewModel InitViewModel() {
        return new ViewModelProvider(this).get(RatingViewModel.class);
    }

    @Override
    protected ActivityRatingBinding InitBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rating);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {
        binding.ratingBar.setMinimumStars(1);
        binding.ratingBar.setRating(5);
        binding.ratingBar.setOnRatingChangeListener((ratingBar, rating, fromUser) -> {
            view_model.rating =  (int) rating;
        });
        binding.btnOutScreen.setOnClickListener(view -> {
            OnBackPress();
        });

        binding.btnSend.setOnClickListener(view -> {
            view_model.HandleSendRating();
        });
    }


    @Override
    protected void InitObserve() {
        view_model.is_success.observe(this, is_success -> {
            if(is_success){
                ShowSuccessFeedback();
            }
        });
    }

    @Override
    protected void InitCommon() {
        view_model.user_receive  = getIntent().getStringExtra(FieldName.USER_ID);
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
                    setResult(ActivityResult.CREATED);
                    finish();
                }).show();
    }

}