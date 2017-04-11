package com.thanosfisherman.mayi;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.thanosfisherman.mayi.listeners.multi.PermissionResultMultiListener;
import com.thanosfisherman.mayi.listeners.multi.RationaleMultiListener;
import com.thanosfisherman.mayi.listeners.single.PermissionResultSingleListener;
import com.thanosfisherman.mayi.listeners.single.RationaleSingleListener;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class MayiFragment extends Fragment
{
    public static final String TAG = MayiFragment.class.getSimpleName();
    public static final int PERMISSION_REQUEST_CODE = 1001;
    @Nullable private PermissionResultSingleListener mPermissionResultListener;
    @Nullable private RationaleSingleListener mRationaleSingleListener;
    @Nullable private PermissionResultMultiListener mPermissionsResultMultiListener;
    @Nullable private RationaleMultiListener mRationaleMultiListener;
    private List<PermissionBean> mBeans;
    private String[] mPermissions;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mBeans = new LinkedList<>();
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
            PermissionBean[] beans = new PermissionBean[permissions.length];

            for (int i = 0; i < permissions.length; i++)
            {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED)
                {
                    beans[i].setGranted(false);
                    if (shouldShowRequestPermissionRationale(permissions[0]))
                    {
                        beans[i].setShouldShowRequestPermissionRationale(true);
                        beans[i].setPermanentlyDenied(false);
                    }
                    else
                    {
                        beans[i].setShouldShowRequestPermissionRationale(false);
                        beans[i].setPermanentlyDenied(true);
                    }
                }
                else
                {
                    beans[i].setGranted(true);
                    beans[i].setShouldShowRequestPermissionRationale(false);
                    beans[i].setPermanentlyDenied(false);
                }
            }


            //resultsListener.permissionResult(bean);
        }
    }

    void checkPermissions(@NonNull String... permissions)
    {
        mPermissions = permissions;
        mBeans.clear();
        boolean shouldRationale = false;
        for (int i = 0; i < permissions.length; i++)
        {
            mBeans.add(i, new PermissionBean(permissions[i]));
            mBeans.get(i).setGranted(false);
            mBeans.get(i).setPermanentlyDenied(false); //Although we don't know yet, we set it to false
            if (shouldShowRequestPermissionRationale(mBeans.get(i).getName()))
            {
                shouldRationale = true;
                mBeans.get(i).setShouldShowRequestPermissionRationale(true);
            }
            else
            {
                mBeans.get(i).setShouldShowRequestPermissionRationale(false);
            }
        }
        if (mRationaleSingleListener != null && shouldRationale)
            mRationaleSingleListener.onRationale(new PermissionRationaleToken(this));
        else if (mRationaleMultiListener != null && shouldRationale)
            mRationaleMultiListener.onRationale(mBeans.toArray(new PermissionBean[mBeans.size()]), new PermissionRationaleToken(this));
        else
            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
    }

    void onContinuePermissionRequest()
    {
        Log.i(TAG, "Continue with request");
        requestPermissions(mPermissions, PERMISSION_REQUEST_CODE);
    }

    void onCancelPermissionRequest()
    {
        Log.i(TAG, "Cancel request and call resultListener");
    }

    void setListeners(PermissionResultSingleListener listenerResult, PermissionResultMultiListener listenerResultMulti, RationaleSingleListener rationaleSingle,
                      RationaleMultiListener rationaleMulti)
    {
        mPermissionResultListener = listenerResult;
        mPermissionsResultMultiListener = listenerResultMulti;
        mRationaleSingleListener = rationaleSingle;
        mRationaleMultiListener = rationaleMulti;
    }
}
