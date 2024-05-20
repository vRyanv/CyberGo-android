package com.tech.cybercars.ui.main.fragment.account.profile.edit_profile;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.tech.cybercars.R;
import com.tech.cybercars.constant.ActivityResult;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.data.models.User;
import com.tech.cybercars.databinding.ActivityEditProfileBinding;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.component.dialog.NotificationDialog;
import com.tech.cybercars.utils.DateTimePicker;
import com.tech.cybercars.utils.FileUtil;
import com.tech.cybercars.utils.KeyBoardUtil;
import com.tech.cybercars.utils.PermissionUtil;


public class EditProfileActivity extends BaseActivity<ActivityEditProfileBinding, EditProfileViewModel> {
    private AlertDialog gender_dialog;
    private String[] gender_choices;
    private final PermissionUtil permission_util = new PermissionUtil();
    private DateTimePicker date_time_picker;

    @NonNull
    @Override
    protected EditProfileViewModel InitViewModel() {
        return new ViewModelProvider(this).get(EditProfileViewModel.class);
    }

    @Override
    protected ActivityEditProfileBinding InitBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {
        InitAlertDialog();
        InitDatePicker();
        binding.inputBirthday.getEditText().setOnClickListener(view -> {
            date_time_picker.Run();
        });

        binding.btnUpdateProfile.setOnClickListener(view -> {
            KeyBoardUtil.HideKeyBoard(this);
            view_model.HandleUpdateProfile();
        });

        binding.btnOutScreen.setOnClickListener(view -> {
            OnBackPress();
        });

        binding.btnUploadAvatar.setOnClickListener(view -> {
            Intent take_photo = new Intent(Intent.ACTION_PICK);
            take_photo.setType("image/*");
            permission_util.SetPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE})
                    .SetPermissionListener(this,
                            () -> {
                                take_img_launcher.launch(take_photo);
                            },
                            denied_permissions -> {

                            })
                    .Start();
        });

    }

    private void InitDatePicker() {
        date_time_picker = new DateTimePicker(getSupportFragmentManager(), DateTimePicker.M_D_Y);
        date_time_picker.SetOnDateTimePicked((calendar, date_time_format) -> {
            view_model.birthday.setValue(date_time_format);
        });
    }

    @Override
    protected void InitObserve() {
        view_model.full_name.observe(this, email -> {
            view_model.full_name_error.setValue(null);
        });
        view_model.full_name_error.observe(this, full_name_error -> {
            binding.inputFullName.setError(full_name_error);
        });

        view_model.is_success.observe(this, is_success -> {
            if (is_success) {
                UpdateProfileSuccess();
            }
        });

        view_model.error_call_server.observe(this, error_call_server -> {
            if (!error_call_server.equals("")) {
                ShowErrorCallServer(error_call_server);
            }
        });

        view_model.error_update_profile.observe(this, error_update_profile -> {
            if (!error_update_profile.equals("")) {
                ShowErrorUpdateProfile();
            }
        });
    }

    private void UpdateProfileSuccess() {
        Intent result_intent = new Intent();
        result_intent.putExtra(FieldName.USER, view_model.edit_user);
        setResult(ActivityResult.UPDATED, result_intent);
        finish();
    }

    @Override
    protected void InitCommon() {
        BindData();
    }

    @Override
    protected void OnBackPress() {
        finish();
    }

    private void ShowErrorUpdateProfile() {
        NotificationDialog.Builder(this)
                .SetIcon(R.drawable.ic_warning)
                .SetTitle(getResources().getString(R.string.email_has_been_used))
                .SetSubtitleVisibility(View.INVISIBLE)
                .SetTextMainButton(getResources().getString(R.string.close))
                .SetOnMainButtonClicked(Dialog::dismiss).show();
    }

    private void ShowErrorCallServer(String error_call_server) {
        NotificationDialog.Builder(this)
                .SetIcon(R.drawable.ic_error)
                .SetTitle(getResources().getString(R.string.something_went_wrong))
                .SetSubtitle(error_call_server)
                .SetTextMainButton(getResources().getString(R.string.close))
                .SetOnMainButtonClicked(Dialog::dismiss).show();
    }


    private final ActivityResultLauncher<Intent> take_img_launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data != null) {
                    Uri img_uri = data.getData();
                    if (img_uri != null) {
                        try {
                            Bitmap img_bitmap = FileUtil.CreateBitMapFromUri(this, img_uri);
                            view_model.avatar_uri = img_uri;
                            binding.imgAvatar.setImageBitmap(img_bitmap);
                        } catch (Exception e) {
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
    );

    private void InitAlertDialog() {
        gender_choices = getResources().getStringArray(R.array.gender_items);
        AlertDialog.Builder gender_dialog_builder = new AlertDialog.Builder(this);
        gender_dialog_builder
                .setTitle(getResources().getString(R.string.select_gender))
                .setItems(gender_choices, (dialog, which) -> {
                    view_model.gender.setValue(gender_choices[which]);
                    dialog.dismiss();
                });
        gender_dialog = gender_dialog_builder.create();

        binding.inputGender.getEditText().setOnClickListener(view -> {
            gender_dialog.show();
        });
    }

    private void BindData() {
        view_model.edit_user = (User) getIntent().getSerializableExtra(FieldName.USER);

        assert view_model.edit_user != null;
        String full_path_res = URL.BASE_URL + URL.AVATAR_RES_PATH + view_model.edit_user.avatar;
        Glide.with(this)
                .load(full_path_res)
                .into(binding.imgAvatar);

        view_model.full_name.setValue(view_model.edit_user.full_name);
        view_model.gender.setValue(view_model.edit_user.gender);
        view_model.birthday.setValue(view_model.edit_user.birthday.equals("") ? getString(R.string.not_update) : view_model.edit_user.birthday);
        view_model.identity_number.setValue(view_model.edit_user.id_number.equals("") ? getString(R.string.not_update) : view_model.edit_user.id_number);
        view_model.address.setValue(view_model.edit_user.address.equals("") ? getString(R.string.not_update) : view_model.edit_user.address);
    }
}