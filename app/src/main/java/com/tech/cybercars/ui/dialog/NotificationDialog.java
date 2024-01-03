package com.tech.cybercars.ui.dialog;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;


import com.tech.cybercars.databinding.DialogNotificationBinding;

public class NotificationDialog extends Dialog {
    private DialogNotificationBinding view_binding;
    public NotificationDialog(@NonNull Context context) {
        super(context);
        view_binding = DialogNotificationBinding.inflate(getLayoutInflater());
        this.setContentView(view_binding.getRoot());
        this.setCancelable(false);
    }
}
