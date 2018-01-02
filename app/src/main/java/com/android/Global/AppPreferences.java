package com.android.Global;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Pon Long Bong on 1/2/2018.
 */

public class AppPreferences {
    private static AppPreferences instance = null;
    public static AppPreferences getInstance(Context context) {
        if (instance == null) {
            if (context == null) {
                throw new IllegalStateException(AppPreferences.class.getSimpleName() +
                        " is not initialized, call getInstance(Context) with a VALID Context first.");
            }
            instance = new AppPreferences(context.getApplicationContext());
        }
        return instance;
    }

    private SharedPreferences preferences;

    private AppPreferences(Context context) {
        preferences = context.getSharedPreferences(context.getPackageName() + "_preferences", Context.MODE_PRIVATE); // From Android sources for getDefaultSharedPreferences
    }

    public boolean isLogin() {
        return preferences.getBoolean("isLogin", false);
    }

    public void setLogin(boolean isLogin) {
        preferences.edit().putBoolean("isLogin", isLogin).apply();
    }

    public boolean isLoginWithGoogle() {
        return preferences.getBoolean("isLoginWithGoogle", false);
    }

    public void setLoginWithGoogle(boolean isLoginWithGoogle) {
        preferences.edit().putBoolean("isLoginWithGoogle", isLoginWithGoogle).apply();
    }

    public String getUserId(){
        return  preferences.getString("userId",null);
    }

    public void setUserId(String userId){
        preferences.edit().putString("userId",userId).apply();
    }
}
