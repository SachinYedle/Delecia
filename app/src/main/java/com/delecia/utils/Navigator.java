package com.delecia.utils;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.delecia.R;

/**
 * Created by admin1 on 5/12/16.
 */

public class Navigator {
    public static void navigateToRegisterFragment(FragmentManager fragmentManager){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        /*ContactsFragment contactsFragment = new ContactsFragment();
        fragmentTransaction.replace(R.id.map,contactsFragment);*/
        fragmentTransaction.addToBackStack("contacts");
        fragmentTransaction.commit();
    }
}

