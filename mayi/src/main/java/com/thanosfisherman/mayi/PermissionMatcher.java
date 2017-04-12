package com.thanosfisherman.mayi;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

class PermissionMatcher
{
    private List<String> mDeniedPermissions, mGrantedPermissions;
    private boolean isAllGranted = true;

    PermissionMatcher(String[] permissions, WeakReference<AppCompatActivity> activity)
    {
        if (permissions == null || permissions.length <= 0)
            throw new RuntimeException("You must have at least 1 permission specified");
        this.mDeniedPermissions = new LinkedList<>();
        this.mGrantedPermissions = new LinkedList<>();
        for (String perm : permissions)
        {
            if (ActivityCompat.checkSelfPermission(activity.get(), perm) != PackageManager.PERMISSION_GRANTED)
            {
                mDeniedPermissions.add(perm);
                isAllGranted = false;
            }
            else
            {
                mGrantedPermissions.add(perm);
            }
        }
    }

    boolean areAllPermissionsGranted()
    {
        return isAllGranted;
    }

    List<String> getDeniedPermissions()
    {
        return mDeniedPermissions;
    }

    List<String> getGrantedPermissions()
    {
        return mGrantedPermissions;
    }
}
