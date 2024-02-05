package com.tech.cybercars.ui.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;

import com.tech.cybercars.R;

public abstract class BaseBottomDialog extends Dialog {
    protected abstract void SetupView(Dialog dialog);
    public BaseBottomDialog(@NonNull Context context, int layout_id) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(layout_id);
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.getWindow().getAttributes().windowAnimations = R.style.BottomDialogAnimation;
        this.getWindow().setGravity(Gravity.BOTTOM);
        this.setCancelable(false);
        SetupView(this);
    }
}
