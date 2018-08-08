package com.thanosfisherman.mayi.listeners.single;


import android.support.annotation.NonNull;

import com.thanosfisherman.mayi.PermissionBean;
import com.thanosfisherman.mayi.PermissionToken;

@FunctionalInterface
public interface RationaleSingleListener
{
    void onRationale(@NonNull PermissionBean permission, @NonNull PermissionToken token);
}
