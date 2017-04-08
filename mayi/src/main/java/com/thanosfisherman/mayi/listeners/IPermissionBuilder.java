package com.thanosfisherman.mayi.listeners;

import com.thanosfisherman.mayi.listeners.single.PermissionResultSingleListener;
import com.thanosfisherman.mayi.listeners.single.RationaleSingleListener;

public interface IPermissionBuilder
{
    IPermissionBuilder onErrorListener(MayiErrorListener errorListener);
    void check();

    interface Permission
    {
        SinglePermissionBuilder withPermission(String permission);

        //IPermissionBuilder.MultiPermissionBuilder withPermissions(String... permissions);

        //IPermissionBuilder.MultiPermissionBuilder withPermissions(Collection<String> permissions);
    }

    interface SinglePermissionBuilder extends IPermissionBuilder
    {
        SinglePermissionBuilder onPermissionResult(PermissionResultSingleListener response);
        SinglePermissionBuilder onPermissionRationaleShouldBeShown(RationaleSingleListener rationale);
    }

    interface MultiPermissionBuilder extends IPermissionBuilder
    {

    }
}
