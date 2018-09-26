package com.thanosfisherman.mayi.listeners.multi;

import androidx.annotation.NonNull;

import com.thanosfisherman.mayi.PermissionBean;

@FunctionalInterface
public interface PermissionResultMultiListener
{
    void permissionResults(@NonNull PermissionBean[] permissions);
}
