package com.thanosfisherman.mayi;

import android.os.Build;
import android.support.annotation.RequiresApi;

final class PermissionRationaleToken implements PermissionToken
{
    private final MayiFragment mayiFragment;
    private boolean isTokenResolved = false;

    PermissionRationaleToken(MayiFragment mayiFragment)
    {
        this.mayiFragment = mayiFragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void continuePermissionRequest()
    {
        if (!isTokenResolved)
        {
            mayiFragment.onContinuePermissionRequest();
            isTokenResolved = true;
        }
    }

    @Override
    public void skipPermissionRequest()
    {
        if (!isTokenResolved)
        {
            mayiFragment.onSkipPermissionRequest();
            isTokenResolved = true;
        }
    }
}
