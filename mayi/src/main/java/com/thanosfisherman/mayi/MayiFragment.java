package com.thanosfisherman.mayi;

import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;

import com.thanosfisherman.mayi.listeners.multi.PermissionResultMultiListener;
import com.thanosfisherman.mayi.listeners.multi.RationaleMultiListener;
import com.thanosfisherman.mayi.listeners.single.PermissionResultSingleListener;
import com.thanosfisherman.mayi.listeners.single.RationaleSingleListener;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class MayiFragment extends Fragment implements PermissionToken {
    public static final String TAG = MayiFragment.class.getSimpleName();
    public static final int PERMISSION_REQUEST_CODE = 1001;
    @Nullable
    private PermissionResultSingleListener mPermissionResultListener;
    @Nullable
    private RationaleSingleListener mRationaleSingleListener;
    @Nullable
    private PermissionResultMultiListener mPermissionsResultMultiListener;
    @Nullable
    private RationaleMultiListener mRationaleMultiListener;
    private List<String> mDeniedPermissions, mGrantedPermissions;
    private final List<String> mRationalePermissions = new LinkedList<>();
    private String[] mPermissions;
    private boolean isShowingNativeDialog;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            isShowingNativeDialog = false;
            if (grantResults.length == 0)
                return;
            final List<PermissionBean> beansResultList = new LinkedList<>();

            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    if (shouldShowRequestPermissionRationale(permissions[i]))
                        beansResultList.add(new PermissionBean(permissions[i], false, false));
                    else
                        beansResultList.add(new PermissionBean(permissions[i], false, true));
                } else {
                    beansResultList.add(new PermissionBean(permissions[i], true, false));
                }
            }

            if (mPermissionResultListener != null)
                mPermissionResultListener.permissionResult(beansResultList.get(0));
            else if (mPermissionsResultMultiListener != null) {
                final List<PermissionBean> beansTotal = new LinkedList<>();
                for (String perm : mGrantedPermissions) {
                    final PermissionBean bean = new PermissionBean(perm, true, false);
                    beansTotal.add(bean);
                }
                beansTotal.addAll(beansResultList);
                mPermissionsResultMultiListener.permissionResults(beansTotal.toArray(new PermissionBean[beansTotal.size()]));
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void checkPermissions(@NonNull String[] allPermissions, @NonNull List<String> deniedPermissions, @NonNull List<String> grantedPermissions) {
        mPermissions = allPermissions;
        mDeniedPermissions = deniedPermissions;
        mGrantedPermissions = grantedPermissions;
        mRationalePermissions.clear();
        final List<PermissionBean> rationaleBeanList = new LinkedList<>();
        for (String deniedPermission : deniedPermissions) {
            if (shouldShowRequestPermissionRationale(deniedPermission)) {
                final PermissionBean beanRationale = new PermissionBean(deniedPermission, false, false);
                rationaleBeanList.add(beanRationale);
                mRationalePermissions.add(deniedPermission);
            }
        }
        if (rationaleBeanList.isEmpty()) {
            if (!isShowingNativeDialog)
                requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), PERMISSION_REQUEST_CODE);
            isShowingNativeDialog = true;
        } else {
            if (mRationaleSingleListener != null)
                mRationaleSingleListener.onRationale(rationaleBeanList.get(0), new PermissionRationaleToken(this));
            else if (mRationaleMultiListener != null)
                mRationaleMultiListener.onRationale(rationaleBeanList.toArray(new PermissionBean[rationaleBeanList.size()]),
                        new PermissionRationaleToken(this));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void continuePermissionRequest() {
        if (!isShowingNativeDialog)
            requestPermissions(mDeniedPermissions.toArray(new String[mDeniedPermissions.size()]), PERMISSION_REQUEST_CODE);
        isShowingNativeDialog = true;
    }

    @Override
    public void skipPermissionRequest() {
        isShowingNativeDialog = false;
        if (mPermissionResultListener != null) {
            final PermissionBean beanRationale = new PermissionBean(mRationalePermissions.get(0), false, false);
            mPermissionResultListener.permissionResult(beanRationale);
        } else if (mPermissionsResultMultiListener != null) {
            final List<PermissionBean> totalBeanList = new LinkedList<>();
            for (String perm : mPermissions) {
                final PermissionBean bean;

                if (mGrantedPermissions.contains(perm)) {
                    bean = new PermissionBean(perm, true, false);
                } else if (mRationalePermissions.contains(perm)) {
                    bean = new PermissionBean(perm, false, false);
                } else {
                    bean = new PermissionBean(perm, false, true);
                }
                totalBeanList.add(bean);
            }
            mPermissionsResultMultiListener.permissionResults(totalBeanList.toArray(new PermissionBean[totalBeanList.size()]));
        }
    }

    void setListeners(PermissionResultSingleListener listenerResult, PermissionResultMultiListener listenerResultMulti, RationaleSingleListener rationaleSingle,
                      RationaleMultiListener rationaleMulti) {
        mPermissionResultListener = listenerResult;
        mPermissionsResultMultiListener = listenerResultMulti;
        mRationaleSingleListener = rationaleSingle;
        mRationaleMultiListener = rationaleMulti;
    }

    /*private boolean isPermissionsDialogShowing()
    {
        final ActivityManager am = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        final ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        return "com.android.packageinstaller.permission.ui.GrantPermissionsActivity".equals(cn.getClassName());
    }*/
}
