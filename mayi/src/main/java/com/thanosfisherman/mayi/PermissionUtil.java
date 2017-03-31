package com.thanosfisherman.mayi;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Wrapper class for all the static calls to the Android permission system
 */
final class PermissionUtil
{

    /**
     * @see ContextCompat#checkSelfPermission
     */
    static int checkSelfPermission(@NonNull Context context, @NonNull String permission)
    {
        return ContextCompat.checkSelfPermission(context, permission);
    }

    /**
     * @see ActivityCompat#requestPermissions
     */
    static void requestPermissions(@Nullable Activity activity, @NonNull String[] permissions, int requestCode)
    {
        if (activity == null)
            return;

        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    /**
     * @see ActivityCompat#shouldShowRequestPermissionRationale
     */
    static boolean shouldShowRequestPermissionRationale(@Nullable Activity activity, @NonNull String permission)
    {
        return activity != null && ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }
}
