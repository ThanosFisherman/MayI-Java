package com.thanosfisherman.mayi;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.thanosfisherman.mayi.listeners.IPermissionBuilder;
import com.thanosfisherman.mayi.listeners.MayiErrorListener;
import com.thanosfisherman.mayi.listeners.single.PermissionResultSingleListener;
import com.thanosfisherman.mayi.listeners.single.RationaleSingleListener;

import java.lang.ref.WeakReference;

public class Mayi implements IPermissionBuilder, IPermissionBuilder.Permission, IPermissionBuilder.SinglePermissionBuilder
{
    private static final String TAG = Mayi.class.getSimpleName();
    private String mSinglePermission;
    private PermissionResultSingleListener mPermissionsResultListener;
    private RationaleSingleListener mRationaleSingleListener;
    private MayiErrorListener mErrorListener;
    private MayiFragment mFrag;
    private WeakReference<AppCompatActivity> mActivity;

    private Mayi(AppCompatActivity activity)
    {
        this.mActivity = new WeakReference<>(activity);
    }

    public static IPermissionBuilder.Permission withActivity(AppCompatActivity activity)
    {
        return new Mayi(activity);
    }


    @Override
    public SinglePermissionBuilder withPermission(String permission)
    {
        mSinglePermission = permission;
        return this;
    }

    @Override
    public SinglePermissionBuilder onPermissionResult(PermissionResultSingleListener response)
    {
        mPermissionsResultListener = response;
        return this;
    }

    @Override
    public SinglePermissionBuilder onPermissionRationaleShouldBeShown(RationaleSingleListener rationale)
    {
        mRationaleSingleListener = rationale;
        return this;
    }

    @Override
    public IPermissionBuilder onErrorListener(MayiErrorListener errorListener)
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
                mFrag.checkSinglePermission(mSinglePermission);
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
            mActivity.get().getSupportFragmentManager().beginTransaction().add(mFrag, MayiFragment.TAG).commitNow();
        }
        mFrag.setCallbackListener(mPermissionsResultListener);
        mFrag.setRationaleListener(mRationaleSingleListener);
    }
}