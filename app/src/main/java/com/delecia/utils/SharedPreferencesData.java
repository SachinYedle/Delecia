package com.delecia.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Sachin on 17/12/16.
 */

public class SharedPreferencesData {
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    private Context context;

    private final String PREF_NAME = "com.example.admin1.locationsharing";

    // Preferences can only be accessed by the application
    private final int PRIVATE_MODE = 0;

    //Variable Names saved in preferences
    private final String USER_ID = "userID";
    private final String USER_PHONE = "userPhone";

    public SharedPreferencesData(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void setUserId(String userId) {
       editor.putString(USER_ID, userId);
        editor.commit();
    }
    public void setUserPhone(String userPhone) {
        editor.putString(USER_PHONE, userPhone);
        editor.commit();
    }

    public String getUserId() {
        return sharedPreferences.getString(USER_ID,"");
    }
    public String getUserPhone() {
        return sharedPreferences.getString(USER_PHONE,"");
    }
}

