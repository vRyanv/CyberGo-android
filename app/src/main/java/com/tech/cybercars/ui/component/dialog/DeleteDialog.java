package com.tech.cybercars.ui.component.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Window;

import androidx.annotation.NonNull;

import com.tech.cybercars.databinding.DialogDeleteBinding;


public class DeleteDialog extends Dialog {
    private SelectButtonCallback select_button_callback;
    private final DialogDeleteBinding binding;
    private final String title;
    public DeleteDialog(@NonNull Context context, String title) {
        super(context);
        this.title = title;
        binding = DialogDeleteBinding.inflate(LayoutInflater.from(context));
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(binding.getRoot());
        InitView();
    }



    private void InitView(){
        binding.txtTitle.setText(title);
        binding.btnDelete.setOnClickListener(view -> {
            select_button_callback.OnDelete(this);
        });

        binding.btnCancel.setOnClickListener(view -> {
            select_button_callback.OnCancel(this);
        });
    }

    public DeleteDialog SetButtonSelectedCallback(SelectButtonCallback select_button_callback){
        this.select_button_callback = select_button_callback;
        return this;
    }

    public interface SelectButtonCallback{
        void OnDelete(Dialog dialog);
        void OnCancel(Dialog dialog);
    }
}
