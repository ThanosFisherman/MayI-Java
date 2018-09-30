package com.thanosfisherman.mayi.listeners.multi;

import com.thanosfisherman.mayi.PermissionBean;

import androidx.annotation.NonNull;

@FunctionalInterface
public interface PermissionResultMultiListener
{
    void permissionResults(@NonNull PermissionBean[] permissions);
}
