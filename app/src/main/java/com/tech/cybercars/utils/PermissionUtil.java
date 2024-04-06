package com.tech.cybercars.utils;

import android.content.Context;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.tech.cybercars.R;

import java.util.List;

public class PermissionUtil {
    private PermissionListener permissionlistener;
    private String[] permissions;

    public PermissionUtil SetPermission(String[] permissions) {
        this.permissions = permissions;
        return this;
    }

    public PermissionUtil SetPermissionListener(Context context, PermissionAccept accept, PermissionDenied denied) {
        permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                accept.onAccepted();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                denied.onDenied(deniedPermissions);
            }
        };
        return this;
    }

    public void Start() {
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage(R.string.permission_mess)
                .setPermissions(permissions)
                .setGotoSettingButton(true)
                .setGotoSettingButtonText(R.string.setting)
                .setDeniedCloseButtonText(R.string.close)
                .check();
    }

    public interface PermissionAccept{
        void onAccepted();

    }
    public interface PermissionDenied{
        void onDenied(List<String> denied_permissions);

    }
}
