package com.thanosfisherman.mayi;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.Arrays;


public class MayiFragment extends Fragment
{
    public static final String TAG = MayiFragment.class.getSimpleName();
    public static final int PERMISSION_REQUEST_CODE = 1001;

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

        if (grantResults[0] == -1)
            Log.i(TAG, "FAILED PERMISSIOS REQUEST");
        if (grantResults.length == 0)
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    void checkPermissions(@NonNull String... permissions)
    {
        Log.i(TAG, "REQUESTING PERMISSIONS " + Arrays.toString(permissions));
        requestPermissions(permissions, PERMISSION_REQUEST_CODE);
    }
}
