package com.thanosfisherman.mayi;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

class PermissionMatcher
{
    private List<String> mDeniedPermissions, mGrantedPermissions;
    private boolean isAllGranted = true;

    @RequiresApi(api = Build.VERSION_CODES.M)
    PermissionMatcher(String[] permissions, WeakReference<Activity> activity)
    {
        if (permissions == null || permissions.length <= 0)
            throw new RuntimeException("You must have at least 1 permission specified");
        this.mDeniedPermissions = new LinkedList<>();
        this.mGrantedPermissions = new LinkedList<>();
        for (String perm : permissions)
        {
            if (activity.get().checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED)
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
