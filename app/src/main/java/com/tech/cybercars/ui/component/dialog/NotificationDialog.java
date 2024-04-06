package com.tech.cybercars.ui.component.dialog;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

import com.tech.cybercars.databinding.DialogNotificationBinding;

public class NotificationDialog extends Dialog {
    private DialogNotificationBinding view_binding;

    public static NotificationDialog Builder(Context context){
        return new NotificationDialog(context);
    }

    private NotificationDialog(@NonNull Context context) {
        super(context);
        view_binding = DialogNotificationBinding.inflate(getLayoutInflater());
        this.setContentView(view_binding.getRoot());
        this.setCancelable(false);
    }

    public NotificationDialog SetTitle(String title){
        view_binding.txtTitleNotifyDialog.setText(title);
        return this;
    }
    public NotificationDialog SetTitleVisibility(int visibility){
        view_binding.txtTitleNotifyDialog.setVisibility(visibility);
        return this;
    }

    public NotificationDialog SetIcon(int icon){
        view_binding.iconNotifyDialog.setImageResource(icon);
        return this;
    }

    public NotificationDialog SetSubtitle(String subtitle){
        view_binding.txtSubtitleNotifyDialog.setText(subtitle);
        return this;
    }
    public NotificationDialog SetSubtitleVisibility(int visibility){
        view_binding.txtSubtitleNotifyDialog.setVisibility(visibility);
        return this;
    }

    public NotificationDialog SetCancelable(boolean cancelable){
        this.setCancelable(cancelable);
        return this;
    }
    public NotificationDialog SetTextMainButton(String text_button){
        view_binding.btnMainNotifyDialog.setText(text_button);
        return this;
    }
    public NotificationDialog SetOnMainButtonClicked(DialogCallback callback){
        view_binding.btnMainNotifyDialog.setOnClickListener(view -> {
            callback.onMainButtonClicked(this);
        });
        return this;
    }
    public NotificationDialog SetMainButtonVisibility(int visibility) {
        view_binding.btnMainNotifyDialog.setVisibility(visibility);
        return this;
    }

    public interface DialogCallback{
        void onMainButtonClicked(Dialog dialog);
    }
}
