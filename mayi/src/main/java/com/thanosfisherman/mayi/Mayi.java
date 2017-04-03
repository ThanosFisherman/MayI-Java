package com.thanosfisherman.mayi;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.thanosfisherman.mayi.listeners.IPermissionListener;
import com.thanosfisherman.mayi.listeners.MayiErrorListener;
import com.thanosfisherman.mayi.listeners.PermissionResultListener;
import com.thanosfisherman.mayi.listeners.RationaleListener;

import java.lang.ref.WeakReference;

public class Mayi implements IPermissionListener, IPermissionListener.Permission, IPermissionListener.SinglePermissionListener
{
    private static final String TAG = Mayi.class.getSimpleName();
    private String mSinglePermission;
    private PermissionResultListener mPermissionsResultListener;
    private RationaleListener mRationaleListener;
    private MayiErrorListener mErrorListener;
    private MayiFragment mFrag;
    private WeakReference<AppCompatActivity> mActivity;

    private Mayi(AppCompatActivity activity)
    {
        this.mActivity = new WeakReference<>(activity);
    }

    public static IPermissionListener.Permission withActivity(AppCompatActivity activity)
    {
        return new Mayi(activity);
    }


    @Override
    public SinglePermissionListener withPermission(String permission)
    {
        mSinglePermission = permission;
        return this;
    }

    @Override
    public SinglePermissionListener onPermissionResult(PermissionResultListener response)
    {
        mPermissionsResultListener = response;
        return this;
    }

    @Override
    public SinglePermissionListener onPermissionRationaleShouldBeShown(RationaleListener rationale)
    {
        mRationaleListener = rationale;
        return this;
    }

    @Override
    public IPermissionListener onErrorListener(MayiErrorListener errorListener)
    {
        mErrorListener = errorListener;
        return this;
    }

    @Override
    public void check()
    {
        try
        {
            if (areAllPermissionsGranted(mSinglePermission))
            {
                final PermissionBean bean = new PermissionBean(mSinglePermission);
                bean.setGranted(true);
                bean.setShouldShowRequestPermissionRationale(false);
                bean.setPermanentlyDenied(false);
                mPermissionsResultListener.permissionResult(bean);
            }
            else
            {
                initializeFragment();
                mFrag.checkPermissions(mSinglePermission);
            }

        }
        catch (Exception e)
        {
            if (mErrorListener != null)
                mErrorListener.onError();
        }
    }

    private boolean areAllPermissionsGranted(String... permissionsStr)
    {
        for (String perm : permissionsStr)
            if (ActivityCompat.checkSelfPermission(mActivity.get(), perm) != PackageManager.PERMISSION_GRANTED)
                return false;
        return true;
    }

    private void initializeFragment()
    {
        mFrag = (MayiFragment) mActivity.get().getSupportFragmentManager().findFragmentByTag(MayiFragment.TAG);
        if (mFrag == null)
        {
            mFrag = new MayiFragment();
            mFrag.setRetainInstance(true);
            mFrag.setCallbackListener(new PermissionResultListener()
            {
                @Override
                public void permissionResult(PermissionBean permission)
                {
                    mPermissionsResultListener.permissionResult(permission);
                }
            });
            mActivity.get().getSupportFragmentManager().beginTransaction().add(mFrag, MayiFragment.TAG).commitNow();
        }
    }
}