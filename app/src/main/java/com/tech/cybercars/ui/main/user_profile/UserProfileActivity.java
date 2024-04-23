package com.tech.cybercars.ui.main.user_profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.tech.cybercars.R;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.data.models.User;
import com.tech.cybercars.databinding.ActivityUserProfileBinding;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.main.MainViewModel;

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
        binding.btnOpenChatUser.setOnClickListener(view -> {

        });
    }

    @Override
    protected void InitObserve() {
        view_model.user_profile.observe(this, this::BindDataToUI);

        view_model.is_loading.observe(this, is_loading -> {
            if(is_loading){
                binding.skeletonLoading.startShimmerAnimation();
            } else {
                binding.skeletonLoading.stopShimmerAnimation();
            }
        });
    }

    @Override
    protected void InitCommon() {
        String user_id = getIntent().getStringExtra(FieldName.USER_ID);
        view_model.HandleLoadUserProfile(user_id);
    }

    @Override
    protected void OnBackPress() {
        finish();
    }

    private void BindDataToUI(User user){
        binding.ratingBar.setRating(user.rating);
        binding.txtFullName.setText(user.full_name);
        binding.txtEmail.setText(user.email);
        binding.txtPhone.setText(user.phone_number);
        binding.txtGender.setText(user.gender);
        binding.txtBirthday.setText(user.birthday);
        binding.txtAddress.setText(user.address);

        String avatar_full_path = URL.BASE_URL + URL.AVATAR_RES_PATH + user.avatar;
        Glide.with(this).load(avatar_full_path).into(binding.imgAvatar);
    }

}