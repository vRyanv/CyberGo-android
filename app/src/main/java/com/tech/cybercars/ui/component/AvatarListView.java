package com.tech.cybercars.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tech.cybercars.R;

import org.w3c.dom.Text;

import java.util.List;

public class AvatarListView extends LinearLayout {
    private final Context context;
    private final LinearLayout wrapper_avatar_list;

    public AvatarListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        inflate(context, R.layout.view_avatar_list, this);
        wrapper_avatar_list = findViewById(R.id.wrapper_avatar_list);
    }

    public void SetAvatarList(List<String> avatar_list) {
        if(avatar_list.size() == 0){
            TextView txt_no_member_join = findViewById(R.id.txt_no_member_join);
            txt_no_member_join.setVisibility(VISIBLE);
            return;
        }
        for (String avatar : avatar_list) {
            RoundedImageView img_avatar = CreateAvatarImageView(avatar);
            wrapper_avatar_list.addView(img_avatar);
        }
    }


    private RoundedImageView CreateAvatarImageView(String avatar) {
        int size = (int) context.getResources().getDimension(com.intuit.sdp.R.dimen._20sdp);
        LinearLayout.LayoutParams img_layout = new LinearLayout.LayoutParams(
                size,
                size
        );
        img_layout.gravity = Gravity.CENTER;

        img_layout.setMarginStart((int)context.getResources().getDimension(com.intuit.sdp.R.dimen._2sdp));

        RoundedImageView img_avatar = new RoundedImageView(context);
        img_avatar.setLayoutParams(img_layout);
        img_avatar.setOval(true);
        img_avatar.mutateBackground(true);
        img_avatar.setCornerRadius(30);
        Glide.with(context)
                .load(avatar)
                .into(img_avatar);
        return img_avatar;
    }
}
