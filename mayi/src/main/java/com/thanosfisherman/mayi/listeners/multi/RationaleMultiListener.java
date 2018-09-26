package com.thanosfisherman.mayi.listeners.multi;

import androidx.annotation.NonNull;

import com.thanosfisherman.mayi.PermissionBean;
import com.thanosfisherman.mayi.PermissionToken;

@FunctionalInterface
public interface RationaleMultiListener
{
    void onRationale(@NonNull PermissionBean[] permissions, @NonNull PermissionToken token);
}
