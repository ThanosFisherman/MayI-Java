package com.thanosfisherman.mayi;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.lang.ref.WeakReference;

final class PermissionRationaleToken implements PermissionToken
{
    private final WeakReference<MayiFragment> mayiFragment;
    private boolean isTokenResolved = false;

    PermissionRationaleToken(MayiFragment mayiFragment)
    {
        this.mayiFragment = new WeakReference<>(mayiFragment);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void continuePermissionRequest()
    {
        if (!isTokenResolved)
        {
            mayiFragment.get().onContinuePermissionRequest();
            isTokenResolved = true;
        }
    }

    @Override
    public void skipPermissionRequest()
    {
        if (!isTokenResolved)
        {
            mayiFragment.get().onSkipPermissionRequest();
            isTokenResolved = true;
        }
    }
}
