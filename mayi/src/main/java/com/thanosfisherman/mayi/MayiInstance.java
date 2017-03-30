package com.thanosfisherman.mayi;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.thanosfisherman.mayi.listeners.PermissionResultListener;
import com.thanosfisherman.mayi.listeners.RationaleListener;

import java.lang.ref.WeakReference;

final class MayiInstance
{
    public static final int PERMISSION_REQUEST_CODE = 1001;
    private WeakReference<FragmentActivity> activity;
    private MayiFragment mFrag;

    MayiInstance(FragmentActivity activity)
    {
        setActivity(activity);
        mFrag = (MayiFragment) activity.getSupportFragmentManager().findFragmentByTag(MayiFragment.TAG);
        if (mFrag == null)
        {
            mFrag = new MayiFragment();
            mFrag.setRetainInstance(true);
            activity.getSupportFragmentManager().beginTransaction().add(mFrag, MayiFragment.TAG).commitNow();
        }
    }

    void setActivity(FragmentActivity activity)
    {
        this.activity = new WeakReference<>(activity);
    }

    void checkPermission(PermissionResultListener resultListener, RationaleListener rationaleListener, String permission)
    {

    }
}
