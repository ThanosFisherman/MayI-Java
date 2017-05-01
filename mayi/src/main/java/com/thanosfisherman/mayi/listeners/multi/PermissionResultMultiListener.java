package com.thanosfisherman.mayi.listeners.multi;

import android.support.annotation.NonNull;

import com.thanosfisherman.mayi.PermissionBean;

public interface PermissionResultMultiListener
{
    void permissionResults(@NonNull PermissionBean[] permissions);
}
