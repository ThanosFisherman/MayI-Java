package com.thanosfisherman.mayi

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import java.lang.ref.WeakReference

@RequiresApi(api = Build.VERSION_CODES.M)
internal class PermissionMatcher(val permissions: Array<out String>, activity: WeakReference<Activity>) {

    val deniedPermissions = permissions.filter { activity.get()?.checkSelfPermission(it) == PackageManager.PERMISSION_DENIED }
    val grantedPermissions = permissions.filter { activity.get()?.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED }
    var isAllGranted = deniedPermissions.isEmpty()
        private set
}