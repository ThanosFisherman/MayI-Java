package com.thanosfisherman.mayi;

import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import com.thanosfisherman.mayi.listeners.multi.PermissionResultMultiListener;
import com.thanosfisherman.mayi.listeners.multi.RationaleMultiListener;
import com.thanosfisherman.mayi.listeners.single.PermissionResultSingleListener;
import com.thanosfisherman.mayi.listeners.single.RationaleSingleListener;

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
    private List<String> mDeniedPermissions, mGrantedPermissions, mRationalePermissions = new LinkedList<>();
    private String[] mPermissions;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //mRationalePermissions = new LinkedList<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == PERMISSION_REQUEST_CODE)
        {
            if (grantResults.length == 0)
                return;
            final List<PermissionBean> beansResultList = new LinkedList<>();

            for (int i = 0; i < permissions.length; i++)
            {
                beansResultList.add(i, new PermissionBean(permissions[i]));
                if (grantResults[i] == PackageManager.PERMISSION_DENIED)
                {
                    beansResultList.get(i).setGranted(false);
                    if (shouldShowRequestPermissionRationale(permissions[i]))
                    {
                        beansResultList.get(i).setShouldShowRequestPermissionRationale(true);
                        beansResultList.get(i).setPermanentlyDenied(false);
                    }
                    else
                    {
                        beansResultList.get(i).setShouldShowRequestPermissionRationale(false);
                        beansResultList.get(i).setPermanentlyDenied(true);
                    }
                }
                else
                {
                    beansResultList.get(i).setGranted(true);
                    beansResultList.get(i).setShouldShowRequestPermissionRationale(false);
                    beansResultList.get(i).setPermanentlyDenied(false);
                }
            }

            if (mPermissionResultListener != null)
                mPermissionResultListener.permissionResult(beansResultList.get(0));
            else if (mPermissionsResultMultiListener != null)
            {
                final List<PermissionBean> beansTotal = new LinkedList<>();
                for (String perm : mGrantedPermissions)
                {
                    final PermissionBean bean = new PermissionBean(perm);
                    bean.setGranted(true);
                    bean.setPermanentlyDenied(false);
                    bean.setShouldShowRequestPermissionRationale(false);
                    beansTotal.add(bean);
                }
                beansTotal.addAll(beansResultList);
                mPermissionsResultMultiListener.permissionResults(beansTotal.toArray(new PermissionBean[beansTotal.size()]));
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void checkPermissions(@NonNull String[] allPermissions, @NonNull List<String> deniedPermissions, @NonNull List<String> grantedPermissions)
    {
        mPermissions = allPermissions;
        mDeniedPermissions = deniedPermissions;
        mGrantedPermissions = grantedPermissions;
        mRationalePermissions.clear();
        final List<PermissionBean> rationaleBeanList = new LinkedList<>();
        for (String deniedPermission : deniedPermissions)
        {
            if (shouldShowRequestPermissionRationale(deniedPermission))
            {
                final PermissionBean beanRationale = new PermissionBean(deniedPermission);
                beanRationale.setGranted(false);
                beanRationale.setPermanentlyDenied(false);
                beanRationale.setShouldShowRequestPermissionRationale(true);
                rationaleBeanList.add(beanRationale);
                mRationalePermissions.add(deniedPermission);
            }
        }
        if (rationaleBeanList.isEmpty())
            requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), PERMISSION_REQUEST_CODE);
        else
        {
            if (mRationaleSingleListener != null)
                mRationaleSingleListener.onRationale(new PermissionRationaleToken(this));
            else if (mRationaleMultiListener != null)
                mRationaleMultiListener.onRationale(rationaleBeanList.toArray(new PermissionBean[rationaleBeanList.size()]),
                                                    new PermissionRationaleToken(this));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void onContinuePermissionRequest()
    {
        requestPermissions(mDeniedPermissions.toArray(new String[mDeniedPermissions.size()]), PERMISSION_REQUEST_CODE);
    }

    void onSkipPermissionRequest()
    {
        final List<PermissionBean> totalBeanList = new LinkedList<>();

        if (mPermissionResultListener != null)
        {
            final PermissionBean beanRationale = new PermissionBean(mRationalePermissions.get(0));
            beanRationale.setGranted(false);
            beanRationale.setPermanentlyDenied(false);
            beanRationale.setShouldShowRequestPermissionRationale(true);
            totalBeanList.add(beanRationale);
            mPermissionResultListener.permissionResult(totalBeanList.get(0));
        }
        else if (mPermissionsResultMultiListener != null)
        {
            for (String perm : mPermissions)
            {
                final PermissionBean bean = new PermissionBean(perm);

                if (mGrantedPermissions.contains(perm))
                {
                    bean.setGranted(true);
                    bean.setShouldShowRequestPermissionRationale(false);
                    bean.setPermanentlyDenied(false);
                    totalBeanList.add(bean);
                }
                else if (mRationalePermissions.contains(perm))
                {
                    bean.setGranted(false);
                    bean.setShouldShowRequestPermissionRationale(true);
                    bean.setPermanentlyDenied(false);
                    totalBeanList.add(bean);
                }
                else
                {
                    bean.setGranted(false);
                    bean.setShouldShowRequestPermissionRationale(false);
                    bean.setPermanentlyDenied(true);
                    totalBeanList.add(bean);
                }
            }
            mPermissionsResultMultiListener.permissionResults(totalBeanList.toArray(new PermissionBean[totalBeanList.size()]));
        }
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
