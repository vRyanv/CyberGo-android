package com.tech.cybercars.ui.component.dialog.bottom_dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.tech.cybercars.R;
import com.tech.cybercars.ui.base.BaseBottomDialog;

public class DoneSelectMapShareTransportBottomDialog extends BaseBottomDialog {
    private Button btn_next_share_transport;
    private Button btn_cancel_next_share_transport;
    public DoneSelectMapShareTransportBottomDialog(@NonNull Context context) {
        super(context, R.layout.dialog_bottom_done_select_map_share_trip);
    }

    @Override
    protected void SetupView(Dialog dialog) {
        btn_next_share_transport = dialog.findViewById(R.id.btn_next_share_transport);
        btn_cancel_next_share_transport = dialog.findViewById(R.id.btn_cancel_next_share_transport);
    }

    public void SetOnNextButtonClicked(View.OnClickListener on_next_btn_clicked){
        btn_next_share_transport.setOnClickListener(on_next_btn_clicked);
    }

    public void SetOnCancelButtonClicked(View.OnClickListener on_cancel_btn_clicked){
        btn_cancel_next_share_transport.setOnClickListener(on_cancel_btn_clicked);
    }
}
