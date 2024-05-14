package com.tech.cybercars.ui.main.user_profile;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.tech.cybercars.R;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.data.models.User;
import com.tech.cybercars.databinding.ActivityUserProfileBinding;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.main.chat.message.MessageActivity;
import com.tech.cybercars.ui.main.rating_report.RatingReportActivity;

public class UserProfileActivity extends BaseActivity<ActivityUserProfileBinding, UserProfileViewModel> {

    @NonNull
    @Override
    protected UserProfileViewModel InitViewModel() {
        return new ViewModelProvider(this).get(UserProfileViewModel.class);
    }

    @Override
    protected ActivityUserProfileBinding InitBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {
        binding.swipeRefresh.setColorSchemeColors(getColor(R.color.orange));
        binding.swipeRefresh.setOnRefreshListener(() -> {
            view_model.LoadDataFromServer();
        });

        binding.btnOutScreen.setOnClickListener(view -> {
            finish();
        });
    }

    @Override
    protected void InitObserve() {
        view_model.user_profile.observe(this, this::BindDataToUI);

        view_model.is_loading.observe(this, is_loading -> {
            if (is_loading) {
                binding.skeletonLoading.startShimmerAnimation();
            } else {
                binding.skeletonLoading.stopShimmerAnimation();
                binding.swipeRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    protected void InitCommon() {
        view_model.user_id = getIntent().getStringExtra(FieldName.USER_ID);
        view_model.HandleLoadUserProfile();
    }

    @Override
    protected void OnBackPress() {
        finish();
    }

    private void BindDataToUI(User user) {
        binding.ratingBar.setRating(user.rating);
        binding.txtFullName.setText(user.full_name);
        binding.txtEmail.setText(user.email);
        binding.txtPhone.setText(user.phone_number);
        binding.txtGender.setText(user.gender);
        binding.txtBirthday.setText(user.birthday);
        binding.txtAddress.setText(user.address);

        String avatar_full_path = URL.BASE_URL + URL.AVATAR_RES_PATH + user.avatar;
        Glide.with(this).load(avatar_full_path).into(binding.imgAvatar);

        binding.btnViewRatingReport.setOnClickListener(view -> {
            Intent rating_intent = new Intent(this, RatingReportActivity.class);
            rating_intent.putExtra(FieldName.USER_ID, user.user_id);
            rating_intent.putExtra(FieldName.AVATAR, user.avatar);
            rating_intent.putExtra(FieldName.FULL_NAME, user.full_name);
            startActivity(rating_intent);
        });

        binding.btnOpenChatUser.setOnClickListener(view -> {
            String user_receive_id = view_model.user_id;
            Intent message_intent = new Intent(this, MessageActivity.class);
            message_intent.putExtra(FieldName.USER_ID, user_receive_id);
            message_intent.putExtra(FieldName.FULL_NAME, view_model.user_profile.getValue().full_name);
            message_intent.putExtra(FieldName.AVATAR, view_model.user_profile.getValue().avatar);
            startActivity(message_intent);
        });
    }

}