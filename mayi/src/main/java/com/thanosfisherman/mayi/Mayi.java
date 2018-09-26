package com.thanosfisherman.mayi;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.thanosfisherman.mayi.listeners.IPermissionBuilder;
import com.thanosfisherman.mayi.listeners.MayiErrorListener;
import com.thanosfisherman.mayi.listeners.multi.PermissionResultMultiListener;
import com.thanosfisherman.mayi.listeners.multi.RationaleMultiListener;
import com.thanosfisherman.mayi.listeners.single.PermissionResultSingleListener;
import com.thanosfisherman.mayi.listeners.single.RationaleSingleListener;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

public class Mayi implements IPermissionBuilder,
                             IPermissionBuilder.Permission,
                             IPermissionBuilder.SinglePermissionBuilder,
                             IPermissionBuilder.MultiPermissionBuilder
{
    private String[] mPermissions;
    @Nullable private PermissionResultSingleListener mPermissionResultListener;
    @Nullable private RationaleSingleListener mRationaleSingleListener;
    @Nullable private PermissionResultMultiListener mPermissionsResultMultiListener;
    @Nullable private RationaleMultiListener mRationaleMultiListener;
    private MayiErrorListener mErrorListener;
    private final WeakReference<Activity> mActivity;
    private boolean isRationaleCalled = false, isResultCalled = false;

    private Mayi(Activity activity)
    {
        this.mActivity = new WeakReference<>(activity);
    }

    public static IPermissionBuilder.Permission withActivity(Activity activity)
    {
        return new Mayi(activity);
    }

    @Override
    public SinglePermissionBuilder withPermission(@NonNull String permission)
    {
        mPermissions = new String[]{permission};
        return this;
    }

    @Override
    public MultiPermissionBuilder withPermissions(@NonNull String... permissions)
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

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
                grandEverything();
            else
            {
                final PermissionMatcher matcher = new PermissionMatcher(mPermissions, mActivity);
                if (matcher.areAllPermissionsGranted())
                    grandEverything();
                else
                    initializeFragmentAndCheck(mPermissions, matcher.getDeniedPermissions(), matcher.getGrantedPermissions());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            if (mErrorListener != null)
                mErrorListener.onError(e);
        }
    }

    private void grandEverything()
    {
        final PermissionBean[] beans = new PermissionBean[mPermissions.length];

        for (int i = 0; i < mPermissions.length; i++)
        {
            beans[i] = new PermissionBean(mPermissions[i]);
            beans[i].setGranted(true);
            beans[i].setPermanentlyDenied(false);
        }
        if (mPermissionResultListener != null)
            mPermissionResultListener.permissionResult(beans[0]);
        else if (mPermissionsResultMultiListener != null)
            mPermissionsResultMultiListener.permissionResults(beans);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initializeFragmentAndCheck(@NonNull String[] allPermissions, @NonNull List<String> deniedPermissions, @NonNull List<String> grantedPermissions)
    {
        MayiFragment frag = (MayiFragment) mActivity.get().getFragmentManager().findFragmentByTag(MayiFragment.TAG);
        if (frag == null)
        {
            final FragmentManager fragmentManager = mActivity.get().getFragmentManager();
            frag = new MayiFragment();
            frag.setRetainInstance(true);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
            {
                fragmentManager.beginTransaction().add(frag, MayiFragment.TAG).commit();
                fragmentManager.executePendingTransactions();
            }
            else
                fragmentManager.beginTransaction().add(frag, MayiFragment.TAG).commitNow();

        }
        frag.setListeners(mPermissionResultListener, mPermissionsResultMultiListener, mRationaleSingleListener, mRationaleMultiListener);
        frag.checkPermissions(allPermissions, deniedPermissions, grantedPermissions);
    }
}