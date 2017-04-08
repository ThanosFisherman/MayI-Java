package com.thanosfisherman.mayi;

final class PermissionRationaleToken implements PermissionToken
{
    private final MayiFragment mayiFragment;
    private boolean isTokenResolved = false;

    PermissionRationaleToken(MayiFragment mayiFragment)
    {
        this.mayiFragment = mayiFragment;
    }

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
    public void cancelPermissionRequest()
    {
        if (!isTokenResolved)
        {
            mayiFragment.onCancelPermissionRequest();
            isTokenResolved = true;
        }
    }
}
