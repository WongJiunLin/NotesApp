package com.example.notesapp.utils;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentUtils {
    private static FragmentUtils fragmentUtils;
    private Activity mainActivity;

    public FragmentUtils() {

    }
    public static synchronized FragmentUtils getInstance() {
        if (fragmentUtils == null)
            fragmentUtils = new FragmentUtils();
        return fragmentUtils;
    }

    public static synchronized void resetInstance() {
        fragmentUtils = null;
    }

    /**
     * This method is used to replace the given container ID with the target Fragment object
     * @param fragmentManager
     * @param fragment
     * @param containerId
     */
    public void loadFragment(FragmentManager fragmentManager, Fragment fragment, int containerId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(containerId, fragment);
        transaction.commit();
    }

    public Activity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(Activity mainActivity) {
        this.mainActivity = mainActivity;
    }
}
