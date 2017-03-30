package com.thanosfisherman.mayi.listeners;

import com.thanosfisherman.mayi.Mayi;
import com.thanosfisherman.mayi.PermissionBean;
import com.thanosfisherman.mayi.PermissionToken;

public interface IPermissionsBuilder
{
    IPermissionsBuilder withErrorListener(MayiErrorListener errorListener);

    interface Permission
    {
        IPermissionsBuilder.SinglePermissionListener withPermission(String permission);

        //IPermissionsBuilder.MultiPermissionListener withPermissions(String... permissions);

        //IPermissionsBuilder.MultiPermissionListener withPermissions(Collection<String> permissions);
    }

    interface SinglePermissionListener
    {
        SinglePermissionListener onPermissionResult(PermissionResultListener response);
        SinglePermissionListener onPermissionRationaleShouldBeShown(RationaleListener rationale);
        IPermissionsBuilder check();
    }

}
