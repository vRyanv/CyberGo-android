package com.tech.cybercars.ui.component.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Window;

import androidx.annotation.NonNull;

import com.tech.cybercars.R;
import com.tech.cybercars.constant.DestinationType;
import com.tech.cybercars.databinding.DialogDestinationTypeSelectionBinding;

public class DestinationTypeSelectionDialog extends Dialog {
    private DialogDestinationTypeSelectionBinding binding;
    private TypeSelectionCallback type_selection_callback;
    public DestinationTypeSelectionDialog(@NonNull Context context) {
        super(context);
        binding = DialogDestinationTypeSelectionBinding.inflate(LayoutInflater.from(context));
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(binding.getRoot());

        InitView();
    }

    private void InitView(){
        binding.typeMultipleSelect.setOnClickListener(view -> {
            type_selection_callback.OnSelected(this, DestinationType.MULTIPLE);
        });

        binding.typeSingleSelect.setOnClickListener(view -> {
            type_selection_callback.OnSelected(this, DestinationType.SINGLE);
        });
    }

    public void SetTypeSelectionCallback(TypeSelectionCallback type_selection_callback){
        this.type_selection_callback = type_selection_callback;
    }

    public interface TypeSelectionCallback{
        void OnSelected(Dialog dialog, String destination_type);
    }
}
