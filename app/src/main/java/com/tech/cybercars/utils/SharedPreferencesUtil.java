package com.tech.cybercars.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.tech.cybercars.constant.FieldName;

public class SharedPreferencesUtil {
    private final static String SHARE_PREFERENCE_KEY = "SESSION";
    public final static String IS_REMEMBER_KEY = "IS_REMEMBER_KEY";
    public final static String USER_TOKEN_KEY = "USER_TOKEN_KEY";
    public static void SetString(Context context, String key, String value){
        SharedPreferences session = context.getSharedPreferences(SHARE_PREFERENCE_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = session.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static String GetString(Context context, String key){
        SharedPreferences session  = context.getSharedPreferences(SHARE_PREFERENCE_KEY, MODE_PRIVATE);
        return session.getString(key, "");
    }
    public static void SetBoolean(Context context, String key, boolean value){
        SharedPreferences session = context.getSharedPreferences(SHARE_PREFERENCE_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = session.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    public static boolean GetBoolean(Context context, String key){
        SharedPreferences session  = context.getSharedPreferences(SHARE_PREFERENCE_KEY, MODE_PRIVATE);
        return session.getBoolean(key, false);
    }
}
