package com.thanosfisherman.mayi.listeners;

public interface IPermissionListener
{
    IPermissionListener onErrorListener(MayiErrorListener errorListener);
    void check();

    interface Permission
    {
        IPermissionListener.SinglePermissionListener withPermission(String permission);

        //IPermissionListener.MultiPermissionListener withPermissions(String... permissions);

        //IPermissionListener.MultiPermissionListener withPermissions(Collection<String> permissions);
    }

    interface SinglePermissionListener extends IPermissionListener
    {
        SinglePermissionListener onPermissionResult(PermissionResultListener response);
        SinglePermissionListener onPermissionRationaleShouldBeShown(RationaleListener rationale);
    }

    interface MultiPermissionListener extends IPermissionListener
    {

    }
}
