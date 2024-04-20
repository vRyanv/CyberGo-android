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
import com.tech.cybercars.constant.URL;

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
        wrapper_avatar_list.removeAllViews();
        if(avatar_list.size() == 0){
            LinearLayout.LayoutParams txt_layout = new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
            );
            int margin_top = (int) context.getResources().getDimension(com.intuit.sdp.R.dimen._10sdp);
            txt_layout.setMargins(0,margin_top,0,0);
            TextView txt_no_member_join = new TextView(context);
            txt_no_member_join.setLayoutParams(txt_layout);
            txt_no_member_join.setTypeface(getResources().getFont(R.font.sp_pro_round_regular));
            txt_no_member_join.setTextSize(context.getResources().getDimension(com.intuit.sdp.R.dimen._10sdp));
            txt_no_member_join.setText(context.getString(R.string.no_members_have_been_joined_yet));
            txt_no_member_join.setVisibility(VISIBLE);
            return;
        }

//            <TextView
//        android:visibility="gone"
//        android:layout_marginTop="@dimen/_3sdp"
//        android:fontFamily="@font/sp_pro_round_regular"
//        android:textSize="@dimen/_10ssp"
//        android:id="@+id/txt_no_member_join"
//        android:layout_width="wrap_content"
//        android:layout_height="wrap_content"
//        android:text="@string/no_members_have_been_joined_yet"
//                />
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
        img_avatar.setBackgroundColor(context.getColor(R.color.gray));
        String avatar_full_path = URL.BASE_URL + URL.AVATAR_RES_PATH + avatar;
        Glide.with(context)
                .load(avatar_full_path)
                .into(img_avatar);
        return img_avatar;
    }
}
