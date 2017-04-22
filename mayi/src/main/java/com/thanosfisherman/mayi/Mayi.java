package com.thanosfisherman.mayi;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.thanosfisherman.mayi.listeners.IPermissionBuilder;
import com.thanosfisherman.mayi.listeners.MayiErrorListener;
import com.thanosfisherman.mayi.listeners.multi.PermissionResultMultiListener;
import com.thanosfisherman.mayi.listeners.multi.RationaleMultiListener;
import com.thanosfisherman.mayi.listeners.single.PermissionResultSingleListener;
import com.thanosfisherman.mayi.listeners.single.RationaleSingleListener;

import java.lang.ref.WeakReference;
import java.util.Arrays;

public class Mayi implements IPermissionBuilder,
                             IPermissionBuilder.Permission,
                             IPermissionBuilder.SinglePermissionBuilder,
                             IPermissionBuilder.MultiPermissionBuilder
{
    private static final String TAG = Mayi.class.getSimpleName();
    @NonNull private String[] mPermissions;
    @Nullable private PermissionResultSingleListener mPermissionResultListener;
    @Nullable private RationaleSingleListener mRationaleSingleListener;
    @Nullable private PermissionResultMultiListener mPermissionsResultMultiListener;
    @Nullable private RationaleMultiListener mRationaleMultiListener;
    private MayiErrorListener mErrorListener;
    private MayiFragment mFrag;
    private WeakReference<AppCompatActivity> mActivity;
    private boolean isRationaleCalled = false, isResultCalled = false;

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
        mPermissions = new String[]{permission};
        return this;
    }

    @Override
    public MultiPermissionBuilder withPermissions(String... permissions)
    {
        mPermissions = permissions;
        return this;
    }

    @Override
    public SinglePermissionBuilder onResult(PermissionResultSingleListener response)
    {
        if (!isResultCalled)
        {
            mPermissionResultListener = response;
            isResultCalled = true;
        }
        return this;
    }

    @Override
    public SinglePermissionBuilder onRationale(RationaleSingleListener rationale)
    {
        if (!isRationaleCalled)
        {
            mRationaleSingleListener = rationale;
            isRationaleCalled = true;
        }
        return this;
    }

    @Override
    public MultiPermissionBuilder onResult(PermissionResultMultiListener response)
    {
        if (!isResultCalled)
        {
            mPermissionsResultMultiListener = response;
            isResultCalled = true;
        }
        return this;
    }

    @Override
    public MultiPermissionBuilder onRationale(RationaleMultiListener rationale)
    {
        if (!isRationaleCalled)
        {
            mRationaleMultiListener = rationale;
            isRationaleCalled = true;
        }
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
            if (mPermissions == null || mPermissions.length == 0)
                throw new IllegalArgumentException("You must specify at least one valid permission to check");
            if (Arrays.asList(mPermissions).contains(null))
                throw new IllegalArgumentException("Permssions arguments must NOT contain null values");
            final PermissionMatcher matcher = new PermissionMatcher(mPermissions, mActivity);
            if (matcher.areAllPermissionsGranted())
            {
                final PermissionBean[] beans = new PermissionBean[mPermissions.length];

                for (int i = 0; i < mPermissions.length; i++)
                {
                    beans[i] = new PermissionBean(mPermissions[i]);
                    beans[i].setGranted(true);
                    beans[i].setShouldShowRequestPermissionRationale(false);
                    beans[i].setPermanentlyDenied(false);
                }

                if (mPermissionResultListener != null)
                    mPermissionResultListener.permissionResult(beans[0]);
                else if (mPermissionsResultMultiListener != null)
                    mPermissionsResultMultiListener.permissionResults(beans);
            }
            else
            {
                initializeFragment();
                mFrag.checkPermissions(mPermissions, matcher.getDeniedPermissions(), matcher.getGrantedPermissions());
            }

        }
        catch (Exception e)
        {
            if (mErrorListener != null)
                mErrorListener.onError(e);
            else
                throw new IllegalArgumentException("You must specify at least one permission to check");
        }
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
        mFrag.setListeners(mPermissionResultListener, mPermissionsResultMultiListener, mRationaleSingleListener, mRationaleMultiListener);
    }
}