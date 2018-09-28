package com.thanosfisherman.mayi

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import java.lang.ref.WeakReference

@RequiresApi(api = Build.VERSION_CODES.M)
internal class KermissionMatcher(permissions: Array<String>, activity: WeakReference<Activity>) {

    var isAllGranted = true
        private set
    val grantedPermissions = mutableListOf<String>()
    val deniedPermissions = mutableListOf<String>()


    init {
        if (permissions.isEmpty())
            throw RuntimeException("You must have at least 1 permission specified")

        for (perm in permissions) {
            if (activity.get()?.checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(perm)
                isAllGranted = false
            } else
                grantedPermissions.add(perm)
        }
    }
}