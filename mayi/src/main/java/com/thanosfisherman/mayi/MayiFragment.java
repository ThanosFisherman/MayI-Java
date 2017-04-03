package com.thanosfisherman.mayi;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.thanosfisherman.mayi.listeners.PermissionResultListener;

import java.util.Arrays;


public class MayiFragment extends Fragment
{
    public static final String TAG = MayiFragment.class.getSimpleName();
    public static final int PERMISSION_REQUEST_CODE = 1001;
    private PermissionResultListener callback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        Log.i(TAG, "onRequestPermissionsResult() " + Arrays.toString(permissions) + Arrays.toString(grantResults));
        if (requestCode == PERMISSION_REQUEST_CODE)
        {
            PermissionBean bean = new PermissionBean(permissions[0]);

            if (grantResults[0] == PackageManager.PERMISSION_DENIED)
            {
                bean.setGranted(false);
                if (shouldShowRequestPermissionRationale(permissions[0]))
                {
                    bean.setShouldShowRequestPermissionRationale(true);
                    bean.setPermanentlyDenied(false);
                }
                else
                {
                    bean.setShouldShowRequestPermissionRationale(false);
                    bean.setPermanentlyDenied(true);
                }
            }
            else
            {
                bean.setGranted(true);
                bean.setShouldShowRequestPermissionRationale(false);
                bean.setPermanentlyDenied(false);
            }
            if (grantResults.length == 0)
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            callback.permissionResult(bean);
        }
    }

    void checkPermissions(@NonNull String... permissions)
    {
        Log.i(TAG, "REQUESTING PERMISSIONS " + Arrays.toString(permissions));
        requestPermissions(permissions, PERMISSION_REQUEST_CODE);
    }

    void setCallbackListener(PermissionResultListener listener)
    {
        callback = listener;
    }

}
