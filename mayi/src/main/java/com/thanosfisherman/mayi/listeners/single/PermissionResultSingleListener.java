package com.thanosfisherman.mayi.listeners.single;

import android.support.annotation.NonNull;

import com.thanosfisherman.mayi.PermissionBean;

@FunctionalInterface
public interface PermissionResultSingleListener
{
    void permissionResult(@NonNull PermissionBean permission);
}
