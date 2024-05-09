package com.tech.cybercars.ui.main.fragment.account.profile.edit_id_card;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tech.cybercars.R;
import com.tech.cybercars.constant.ActivityResult;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.data.models.User;
import com.tech.cybercars.databinding.ActivityEditIdentityCardBinding;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.component.dialog.NotificationDialog;
import com.tech.cybercars.utils.FileUtil;
import com.tech.cybercars.utils.PermissionUtil;

import java.io.FileNotFoundException;

public class EditIdentityCardActivity extends BaseActivity<ActivityEditIdentityCardBinding, EditIdentityCardViewModel> {
    private final String PICK_FRONT_ID_CARD = "PICK_FRONT_ID_CARD";
    private final String PICK_BACK_ID_CARD = "PICK_BACK_ID_CARD";
    private String current_pick;
    private final PermissionUtil permissionUtil = new PermissionUtil();
    @NonNull
    @Override
    protected EditIdentityCardViewModel InitViewModel() {
        return new ViewModelProvider(this).get(EditIdentityCardViewModel.class);
    }

    @Override
    protected ActivityEditIdentityCardBinding InitBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_identity_card);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {
        binding.btnUploadFrontIdCard.setOnClickListener(view -> {
            Intent take_photo = new Intent(Intent.ACTION_PICK);
            current_pick = PICK_FRONT_ID_CARD;
            take_photo.setType("image/*");
            permissionUtil.SetPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE})
                    .SetPermissionListener(this,
                            () -> {
                                take_img_launcher.launch(take_photo);
                            },
                            denied_permissions -> {

                            })
                    .Start();
        });

        binding.btnUploadBackIdCard.setOnClickListener(view -> {
            Intent take_photo = new Intent(Intent.ACTION_PICK);
            current_pick = PICK_BACK_ID_CARD;
            take_photo.setType("image/*");
            permissionUtil.SetPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE})
                    .SetPermissionListener(this,
                            () -> {
                                take_img_launcher.launch(take_photo);
                            },
                            denied_permissions -> {

                            })
                    .Start();
        });

        binding.btnSaveIdCard.setOnClickListener(view -> {
            view_model.HandleUpdateIdCard();
        });

        binding.btnOutScreen.setOnClickListener(view -> {
            OnBackPress();
        });
    }

    @Override
    protected void InitObserve() {
        view_model.is_success.observe(this, is_success ->{
            if(is_success){
                Intent update_id_card_intent = new Intent();
                update_id_card_intent.putExtra(FieldName.USER, view_model.user_edit);
                setResult(ActivityResult.UPDATED, update_id_card_intent);
                finish();
            }
        });

        view_model.error_call_server.observe(this, this::ShowErrorCallServer);
    }

    @Override
    protected void InitCommon() {
        String base_img_res =  URL.BASE_URL + URL.ID_CARD_RES_PATH;

        view_model.user_edit = (User) getIntent().getSerializableExtra(FieldName.USER);
        assert view_model.user_edit != null;
        view_model.id_number.setValue(view_model.user_edit.id_number);

        if(view_model.user_edit.front_id_card != null &&  !view_model.user_edit.front_id_card.equals("")){
            String front_id_card_full_path = base_img_res + view_model.user_edit.front_id_card;
            Glide.with(this)
                    .load(front_id_card_full_path)
                    .placeholder(R.drawable.thumb_id_card)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                            Toast.makeText(EditIdentityCardActivity.this, getString(R.string.image_upload_failed), Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                            binding.imgFrontIdCard.setScaleType(ImageView.ScaleType.FIT_XY);
                            return false;
                        }
                    })
                    .into(binding.imgFrontIdCard);
        }


        if(view_model.user_edit.back_id_card != null && !view_model.user_edit.back_id_card.equals("")){
            String back_id_card_full_path = base_img_res + view_model.user_edit.back_id_card;
            Glide.with(this)
                    .load(back_id_card_full_path)
                    .placeholder(R.drawable.thumb_id_card)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                            Toast.makeText(EditIdentityCardActivity.this, getString(R.string.image_upload_failed), Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                            binding.imgBackIdCard.setScaleType(ImageView.ScaleType.FIT_XY);
                            return false;
                        }
                    })
                    .into(binding.imgBackIdCard);
        }
    }

    @Override
    protected void OnBackPress() {
        finish();
    }

    private final ActivityResultLauncher<Intent> take_img_launcher =  registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data != null) {
                    Uri img_uri = data.getData();
                    if (img_uri != null) {
                        try {
                            BindImgToUI(img_uri);
                        } catch (Exception e) {
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
    );

    private void BindImgToUI(Uri img_uri) throws FileNotFoundException {
        Bitmap img_bitmap = FileUtil.CreateBitMapFromUri(this, img_uri);
        if(current_pick.equals(PICK_FRONT_ID_CARD)){
            view_model.front_id_card_uri = img_uri;
            binding.imgFrontIdCard.setScaleType(ImageView.ScaleType.FIT_XY);
            binding.imgFrontIdCard.setImageBitmap(img_bitmap);
        } else {
            view_model.back_id_card_uri = img_uri;
            binding.imgBackIdCard.setScaleType(ImageView.ScaleType.FIT_XY);
            binding.imgBackIdCard.setImageBitmap(img_bitmap);
        }
    }

    public void ShowErrorCallServer(String error){
        NotificationDialog.Builder(this)
                .SetIcon(R.drawable.ic_error)
                .SetTitle(getResources().getString(R.string.something_went_wrong))
                .SetSubtitle(error)
                .SetTextMainButton(getResources().getString(R.string.close))
                .SetOnMainButtonClicked(Dialog::dismiss).show();
    }
}