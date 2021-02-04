package com.example.notes;

import android.content.res.Configuration;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.notes.ui.SingleNoteFragment;

public abstract class FragmentHandler {
    public static void replaceFragment(FragmentActivity activity, Fragment fragment, int fragmentIdToReplace, boolean addToBackStack, boolean popUpBeforeReplace, boolean add) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (popUpBeforeReplace){
            Fragment oldFragment = fragmentManager.findFragmentById(fragmentIdToReplace);
            if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && oldFragment instanceof SingleNoteFragment){
                fragmentManager.popBackStack();
            }
        }
        if (add){
           fragmentTransaction.add(fragment,"Some fragment");
        }else {
            fragmentTransaction.replace(fragmentIdToReplace, fragment);
        }

        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (addToBackStack){
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    public static int getIdFromOrientation(FragmentActivity activity) {
        Boolean isLandscape = activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (isLandscape) {
            return R.id.single_note;
        } else {
            return R.id.notes;
        }
    }
    public static void popBackStack(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        fragmentManager.popBackStack();
    }
}

