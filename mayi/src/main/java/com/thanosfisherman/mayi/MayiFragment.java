package com.thanosfisherman.mayi;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.thanosfisherman.mayi.listeners.single.PermissionResultSingleListener;
import com.thanosfisherman.mayi.listeners.single.RationaleSingleListener;

import java.util.Arrays;


public class MayiFragment extends Fragment
{
    public static final String TAG = MayiFragment.class.getSimpleName();
    public static final int PERMISSION_REQUEST_CODE = 1001;
    private PermissionResultSingleListener resultsListener;
    private RationaleSingleListener rationaleListener;
    private PermissionBean[] mPermissionBeans;
    private String mSinglePermission;

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
            if (grantResults.length == 0)
            {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                return;
            }
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

            resultsListener.permissionResult(bean);
        }
    }

    void checkSinglePermission(@NonNull String permission)
    {
        mSinglePermission = permission;
        if (shouldShowRequestPermissionRationale(permission))
        {
            PermissionToken token = new PermissionRationaleToken(this);
            rationaleListener.onRationale(token);
        }
        else
        {
            Log.i(TAG, "REQUESTING PERMISSION " + permission);
            requestPermissions(new String[]{permission}, PERMISSION_REQUEST_CODE);
        }
    }

    void setCallbackListener(PermissionResultSingleListener listener)
    {
        resultsListener = listener;
    }

    void setRationaleListener(RationaleSingleListener listener)
    {
        rationaleListener = listener;
    }

    void onContinuePermissionRequest()
    {
        Log.i(TAG, "Continue with request");
        requestPermissions(new String[]{mSinglePermission}, PERMISSION_REQUEST_CODE);
    }

    void onCancelPermissionRequest()
    {
        Log.i(TAG, "Cancel request and call resultListener");
    }
}
