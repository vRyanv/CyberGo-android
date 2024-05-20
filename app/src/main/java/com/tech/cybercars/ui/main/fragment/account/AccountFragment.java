package com.tech.cybercars.ui.main.fragment.account;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.tech.cybercars.CyberGoApplication;
import com.tech.cybercars.R;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.data.local.AppDBContext;
import com.tech.cybercars.databinding.FragmentAccountBinding;
import com.tech.cybercars.services.eventbus.ActionEvent;
import com.tech.cybercars.ui.base.BaseFragment;
import com.tech.cybercars.ui.component.dialog.NotificationDialog;
import com.tech.cybercars.ui.component.dialog.UpdatePasswordDialog;
import com.tech.cybercars.ui.main.fragment.account.my_vehicle.MyVehicleActivity;
import com.tech.cybercars.ui.main.fragment.account.profile.ProfileActivity;
import com.tech.cybercars.ui.signin.SignInActivity;
import com.tech.cybercars.utils.PermissionUtil;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AccountFragment extends BaseFragment<FragmentAccountBinding, AccountViewModel> {
    private final PermissionUtil permission_util = new PermissionUtil();

    @NonNull
    @Override
    protected AccountViewModel InitViewModel() {
        return new ViewModelProvider(this).get(AccountViewModel.class);
    }

    @Override
    protected FragmentAccountBinding InitBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {
        binding.btnOpenRegisterAsDriver.setOnClickListener(view -> {
            permission_util.SetPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE})
                    .SetPermissionListener(requireContext(),
                            () -> {
                                startActivity(new Intent(this.getActivity(), VehicleTypeSelectionActivity.class));
                            },
                            denied_permissions -> {
                            })
                    .Start();
        });

        binding.btnOpenProfile.setOnClickListener(view -> {
            profile_result_launcher.launch(new Intent(this.getActivity(), ProfileActivity.class));
        });

        binding.btnOpenMyVehicle.setOnClickListener(view -> {
            startActivity(new Intent(this.getActivity(), MyVehicleActivity.class));
        });

        binding.btnChangePass.setOnClickListener(view -> {
                new UpdatePasswordDialog(requireContext())
                        .SetButtonSelectedCallback(new UpdatePasswordDialog.SelectButtonCallback() {
                            @Override
                            public void OnUpdate(Dialog dialog, String current_pass, String new_pass, String confirm_pass) {
                                dialog.dismiss();
                                view_model.HandleUpdatePassword(current_pass, new_pass);
                            }

                            @Override
                            public void OnCancel(Dialog dialog) {
                                dialog.dismiss();
                            }
                        }).show();
        });

        binding.btnLogout.setOnClickListener(view -> {
            CyberGoApplication.instance.Logout();
        });
    }

    @Override
    protected void InitObserve() {
        view_model.is_update_success_password.observe(this, is_update_success_password-> {
            ShowUpdatePassSuccess();
        });

        view_model.wrong_current_password.observe(this, wrong_current_password-> {
            ShowWrongCurrentPass();
        });

        view_model.error_call_server.observe(this, this::ShowErrorDialog);
    }

    private void ShowWrongCurrentPass() {
        NotificationDialog.Builder(requireContext())
                .SetIcon(R.drawable.ic_warning)
                .SetTitleVisibility(View.GONE)
                .SetSubtitle(getResources().getString(R.string.invalid_current_password))
                .SetTextMainButton(getResources().getString(R.string.close))
                .SetOnMainButtonClicked(Dialog::dismiss).show();
    }

    @Override
    protected void InitCommon() {
        BindUserUI();
    }

    private void ShowUpdatePassSuccess(){
        NotificationDialog.Builder(requireContext())
                .SetIcon(R.drawable.ic_success)
                .SetTitle(getResources().getString(R.string.success))
                .SetSubtitle(getResources().getString(R.string.update_pass_success))
                .SetTextMainButton(getResources().getString(R.string.close))
                .SetOnMainButtonClicked(Dialog::dismiss).show();
    }

    private final ActivityResultLauncher<Intent> profile_result_launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                BindUserUI();
            });

    private void BindUserUI() {
        String full_name = SharedPreferencesUtil.GetString(requireContext(), FieldName.FULL_NAME);
        binding.txtFullName.setText(full_name);

        String avatar = SharedPreferencesUtil.GetString(requireContext(), FieldName.AVATAR);
        String full_path_res = URL.BASE_URL + URL.AVATAR_RES_PATH + avatar;
        Glide.with(this)
                .load(full_path_res)
                .into(binding.imgAvatar);
    }
}