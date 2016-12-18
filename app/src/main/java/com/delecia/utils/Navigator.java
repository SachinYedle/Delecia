package com.delecia.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.delecia.R;
import com.delecia.app.MyApplication;
import com.delecia.ui.activities.HomeActivity;
import com.delecia.ui.activities.MainActivity;
import com.delecia.ui.activities.RegistrationActivity;

/**
 * Created by admin1 on 5/12/16.
 */

public class Navigator {
    public static void navigateToMainActivity(Context context){
        Intent intent = new Intent(MyApplication.getCurrentActivityContext(), MainActivity.class);
        context.startActivity(intent);
    }

    public static void navigateToRegistrationActivity(Context context){
        Intent intent = new Intent(MyApplication.getCurrentActivityContext(), RegistrationActivity.class);
        context.startActivity(intent);
    }

    public static void navigateToHomeActivity(Context context){
        Intent intent = new Intent(MyApplication.getCurrentActivityContext(), HomeActivity.class);
        context.startActivity(intent);
    }
}

