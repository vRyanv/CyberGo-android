package com.tech.cybercars.ui.main.fragment.account.profile;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.tech.cybercars.R;
import com.tech.cybercars.constant.ActivityResult;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.data.models.User;
import com.tech.cybercars.databinding.ActivityProfileBinding;
import com.tech.cybercars.services.eventbus.ActionEvent;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.component.dialog.NotificationDialog;
import com.tech.cybercars.ui.main.fragment.account.profile.edit_id_card.EditIdentityCardActivity;
import com.tech.cybercars.ui.main.fragment.account.profile.edit_phone.EditPhoneActivity;
import com.tech.cybercars.ui.main.fragment.account.profile.edit_profile.EditProfileActivity;
import com.tech.cybercars.ui.main.fragment.account.profile.user_statistic.UserStatisticActivity;
import com.tech.cybercars.ui.main.rating_report.RatingReportActivity;
import com.tech.cybercars.utils.PermissionUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.FileNotFoundException;

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
        binding.btnViewRatingReport.setOnClickListener(view -> {
            startActivity(new Intent(this, RatingReportActivity.class));
        });

        binding.btnViewStatistics.setOnClickListener(view -> {
            startActivity(new Intent(this, UserStatisticActivity.class));
        });

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
            Toast.makeText(this, "feature under development", Toast.LENGTH_SHORT).show();
//            return;
//            Intent edit_phone_intent = new Intent(this, EditPhoneActivity.class);
//            edit_phone_intent.putExtra(FieldName.PHONE_NUMBER, view_model.phone_number.getValue());
//            edit_phone_intent.putExtra(FieldName.COUNTRY_NAME_CODE, view_model.user_profile.country_name_code);
//            edit_phone_launcher.launch(edit_phone_intent);
        });

        binding.swipeRefresh.setOnRefreshListener(() -> {
            view_model.LoadProfileFromServer();
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

        view_model.is_loading.observe(this, is_loading -> {
            if(is_loading){

                binding.skeletonLoading.startShimmerAnimation();
            } else {
                binding.skeletonLoading.stopShimmerAnimation();
                binding.swipeRefresh.setRefreshing(false);
            }
        });

        view_model.error_call_server.observe(this, error_call_server -> {
            if(!error_call_server.equals("")){
                ShowErrorCallServer(error_call_server);
            }
        });

    }

    @Override
    protected void InitCommon() {
        view_model.LoadProfileInformation();
    }

    @Override
    protected void OnBackPress() {
        finish();
    }

    private void ShowErrorCallServer(String error_call_server) {
        NotificationDialog.Builder(this)
                .SetIcon(R.drawable.ic_error)
                .SetTitle(getResources().getString(R.string.something_went_wrong))
                .SetSubtitle(error_call_server)
                .SetTextMainButton(getResources().getString(R.string.close))
                .SetOnMainButtonClicked(Dialog::dismiss).show();
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
                    view_model.identity_number.setValue(view_model.user_profile.id_number);
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
        view_model.birthday.setValue(user_updated.birthday);
        EventBus.getDefault().post(new ActionEvent(ActionEvent.UPDATE_DRAWER_INFO));
        view_model.address.setValue(user_updated.address);

        view_model.user_profile = user_updated;
    }

}