package com.tech.cybercars.ui.main.fragment.setting.driver_register;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayoutMediator;
import com.tech.cybercars.R;
import com.tech.cybercars.adapter.driver_registration.DriverRegistrationAdapter;
import com.tech.cybercars.databinding.ActivityDriverRegistrationBinding;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.component.dialog.NotificationDialog;
import com.tech.cybercars.ui.signup.verification.PhoneVerificationActivity;
import com.tech.cybercars.utils.PermissionUtil;

import java.io.IOException;

public class DriverRegistrationActivity extends BaseActivity<ActivityDriverRegistrationBinding, DriverRegistrationViewModel> {
    private final PermissionUtil permission_util = new PermissionUtil();
    @NonNull
    @Override
    protected DriverRegistrationViewModel InitViewModel() {
        return new ViewModelProvider(this).get(DriverRegistrationViewModel.class);
    }

    @Override
    protected ActivityDriverRegistrationBinding InitBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_driver_registration);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {
        permission_util.SetPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE})
                .SetPermissionListener(this,
                        () -> {

                        },
                        denied_permissions -> {
                            finish();
                        })
                .Start();
    }

    @Override
    protected void InitView() {
        binding.btnRegisterAsDriver.setOnClickListener(view -> {
            try {
                view_model.DriverRegistrationHandle();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    protected void InitObserve() {
        view_model.is_lost_image_error.observe(this, is_img_error -> {
            if(is_img_error){
                ShowErrorImgDialog();
            }
        });

        view_model.error_call_server.observe(this, error_call_server -> {
            if(error_call_server != null){
                ShowCallServerError(error_call_server);
            }
        });

        view_model.is_success.observe(this, is_success -> {
            if(is_success){
                ShowSuccessRegistration();
            }
        });

    }

    @Override
    protected void InitCommon() {


        DriverRegistrationAdapter driver_registration_adapter = new DriverRegistrationAdapter(getSupportFragmentManager(), this.getLifecycle());
        binding.paperDriverRegistration.setAdapter(driver_registration_adapter);
        String tab_name[] = new String[]{
                getString(R.string.my_information),
                getString(R.string.driving_license),
                getString(R.string.my_transport)};
        new TabLayoutMediator(binding.tabShareTripInfo, binding.paperDriverRegistration, (tab, position) -> {
            tab.setText(tab_name[position]);
        }).attach();


    }

    @Override
    protected void OnBackPress() {
        this.finish();
    }

    private void ShowErrorImgDialog(){
        NotificationDialog.Builder(this)
                .SetIcon(R.drawable.ic_warning)
                .SetTitle(getResources().getString(R.string.missing_some_information))
                .SetSubtitle(getResources().getString(R.string.please_enter_all_fields_including_text_and_images))
                .SetTextMainButton(getResources().getString(R.string.close))
                .SetOnMainButtonClicked(Dialog::dismiss).show();
    }

    private void ShowCallServerError(String error_call_server){
        NotificationDialog.Builder(this)
                .SetIcon(R.drawable.ic_error)
                .SetTitle(getResources().getString(R.string.something_went_wrong))
                .SetSubtitle(error_call_server)
                .SetTextMainButton(getResources().getString(R.string.close))
                .SetOnMainButtonClicked(Dialog::dismiss).show();
    }

    private void ShowSuccessRegistration(){
        NotificationDialog.Builder(this)
                .SetIcon(R.drawable.ic_success)
                .SetTitle(getResources().getString(R.string.sign_up_success))
                .SetSubtitle(getResources().getString(R.string.the_information_has_been_sent_we_will_review_and_respond_later))
                .SetTextMainButton(getResources().getString(R.string.go_back))
                .SetOnMainButtonClicked(dialog->{ finish(); }).show();
    }
}