package com.tech.cybercars.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
    private final static String SHARE_PREFERENCE_KEY = "session";
    private final static String IS_REMEMBER_KEY = "is_remember";
    private final static String USER_TOKEN_KEY = "user_token";
    public static void SetRememberLogin(Context context, boolean is_remember){
        SharedPreferences session = context.getSharedPreferences(SHARE_PREFERENCE_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = session.edit();
        editor.putBoolean(IS_REMEMBER_KEY, is_remember);
        editor.apply();
    }

    public static boolean GetRememberLogin(Context context){
        SharedPreferences session  = context.getSharedPreferences(SHARE_PREFERENCE_KEY, MODE_PRIVATE);
        return session.getBoolean(IS_REMEMBER_KEY, false);
    }

    public static void SetUserToken(Context context, String token){
        SharedPreferences session = context.getSharedPreferences(SHARE_PREFERENCE_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = session.edit();
        editor.putString(USER_TOKEN_KEY, token);
        editor.apply();
    }

    public static String GetUserToken(Context context){
        SharedPreferences session  = context.getSharedPreferences(SHARE_PREFERENCE_KEY, MODE_PRIVATE);
        return session.getString(USER_TOKEN_KEY, "");
    }
}
