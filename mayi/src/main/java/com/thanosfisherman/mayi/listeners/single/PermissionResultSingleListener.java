package com.thanosfisherman.mayi.listeners.single;

import com.thanosfisherman.mayi.PermissionBean;

import androidx.annotation.NonNull;

@FunctionalInterface
public interface PermissionResultSingleListener
{
    void permissionResult(@NonNull PermissionBean permission);
}
