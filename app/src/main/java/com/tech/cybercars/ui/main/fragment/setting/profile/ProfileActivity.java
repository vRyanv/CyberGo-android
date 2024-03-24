package com.tech.cybercars.ui.main.fragment.setting.profile;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tech.cybercars.R;
import com.tech.cybercars.constant.ActivityResult;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.data.local.user.User;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.databinding.ActivityProfileBinding;
import com.tech.cybercars.ui.main.fragment.setting.profile.edit_phone.EditPhoneActivity;
import com.tech.cybercars.ui.main.fragment.setting.profile.edit_profile.EditProfileActivity;
import com.tech.cybercars.ui.main.fragment.setting.profile.edit_id_card.EditIdentityCardActivity;
import com.tech.cybercars.utils.FileUtil;
import com.tech.cybercars.utils.PermissionUtil;

import java.io.FileNotFoundException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfileActivity extends BaseActivity<ActivityProfileBinding, ProfileViewModel> {
    private final PermissionUtil permission_util = new PermissionUtil();
    @NonNull
    @Override
    protected ProfileViewModel InitViewModel() {
        return new ViewModelProvider(this).get(ProfileViewModel.class);
    }

    @Override
    protected ActivityProfileBinding InitBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {

        binding.btnOpenEditProfile.setOnClickListener(view -> {
            Intent edit_profile_intent = new Intent(this, EditProfileActivity.class);
            edit_profile_intent.putExtra(FieldName.USER, view_model.user_profile);
            edit_profile_launcher.launch(edit_profile_intent);
        });

        binding.btnOpenViewIdCard.setOnClickListener(view -> {
            permission_util.SetPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE})
                    .SetPermissionListener(this,
                            () -> {
                                Intent view_id_card_intent = new Intent(this, EditIdentityCardActivity.class);
                                view_id_card_intent.putExtra(FieldName.USER, view_model.user_profile);
                                edit_id_card_launcher.launch(view_id_card_intent);
                            },
                            denied_permissions -> {
                            })
                    .Start();
        });

        binding.btnOutScreen.setOnClickListener(view -> {
            OnBackPress();
        });

        binding.btnOpenEditPhone.setOnClickListener(view -> {
            Intent edit_phone_intent = new Intent(this, EditPhoneActivity.class);
            edit_phone_intent.putExtra(FieldName.PHONE_NUMBER, view_model.phone_number.getValue());
            edit_phone_intent.putExtra(FieldName.COUNTRY_NAME_CODE, view_model.user_profile.country_name_code);
            edit_phone_launcher.launch(edit_phone_intent);
        });
    }

    @Override
    protected void InitObserve() {
        view_model.avatar.observe(this, avatar -> {
            if (avatar != null) {
                String full_path_res = URL.BASE_URL + URL.AVATAR_RES_PATH + avatar;
                Glide.with(this)
                        .load(full_path_res)
                        .into(binding.imgAvatar);
            }
        });
    }

    @Override
    protected void InitCommon() {
        binding.skeletonLoading.startShimmerAnimation();
        view_model.LoadProfileInformation();
        new Handler().postDelayed(()->{
            binding.skeletonLoading.stopShimmerAnimation();
        }, 3000);
    }

    @Override
    protected void OnBackPress() {
        finish();
    }

    private final ActivityResultLauncher<Intent> edit_id_card_launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result == null) {
                    return;
                }
                Intent result_intent = result.getData();
                if (result_intent != null && result.getResultCode() == ActivityResult.UPDATED) {
                    view_model.user_profile = (User) result_intent.getSerializableExtra(FieldName.USER);
                    Toast.makeText(this, R.string.successfully_updated, Toast.LENGTH_SHORT).show();
                }
            }
    );

    private final ActivityResultLauncher<Intent> edit_phone_launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result == null) {
                    return;
                }
                Intent result_intent = result.getData();
                if (result_intent != null && result.getResultCode() == ActivityResult.UPDATED) {
                    String phone_number = result_intent.getStringExtra(FieldName.PHONE_NUMBER);
                    view_model.phone_number.setValue(phone_number);
                }
            }
    );

    private final ActivityResultLauncher<Intent> edit_profile_launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result == null) {
                    return;
                }
                Intent result_intent = result.getData();
                if (result_intent != null && result.getResultCode() == ActivityResult.UPDATED) {
                    try {
                        UpdateInfo(result_intent);
                        Toast.makeText(this, R.string.successfully_updated, Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
    );

    private void UpdateInfo(Intent result_intent) throws FileNotFoundException {
        User user_updated = (User) result_intent.getSerializableExtra(FieldName.USER);
        assert user_updated != null;
        String avatar_full_path = URL.BASE_URL + URL.AVATAR_RES_PATH + user_updated.avatar;
        Glide.with(this)
                .load(avatar_full_path)
                .into(binding.imgAvatar);

        view_model.full_name.setValue(user_updated.full_name);
        view_model.gender.setValue(user_updated.gender);
        view_model.identity_number.setValue(user_updated.id_number);
        view_model.address.setValue(user_updated.address);

        view_model.user_profile = user_updated;
    }

}