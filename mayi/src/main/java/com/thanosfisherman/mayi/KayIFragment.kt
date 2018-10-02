@file:Suppress("DEPRECATION")

package com.thanosfisherman.mayi

import android.app.Fragment
import android.os.Build
import androidx.annotation.RequiresApi

private const val PERMISSION_REQUEST_CODE = 1001

class KayIFragment : Fragment() {
    val TAG = this.javaClass.simpleName

    private var permissionResultSingleListener: ((PermissionBean) -> Unit)? = null
    private var rationaleSingleListener: ((PermissionBean, PermissionToken) -> Unit)? = null
    private var permissionResultMultiListener: ((Array<PermissionBean>) -> Unit)? = null
    private var rationaleMultiListener: ((Array<PermissionBean>, PermissionToken) -> Unit)? = null
    private var isShowingNativeDialog: Boolean = false
    private lateinit var rationalePermissions: List<String>
    private lateinit var permissionMatcher: PermissionMatcher

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            isShowingNativeDialog = false
            if (grantResults.isEmpty()) return

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    internal fun checkPermissions(permissionMatcher: PermissionMatcher) {
        this.permissionMatcher = permissionMatcher
        rationalePermissions = permissionMatcher.deniedPermissions.filter(this::shouldShowRequestPermissionRationale)
        val rationaleBeanList = rationalePermissions.map { PermissionBean(it) }

        if (rationaleBeanList.isEmpty()) {
            if (!isShowingNativeDialog)
                requestPermissions(permissionMatcher.deniedPermissions.toTypedArray(), PERMISSION_REQUEST_CODE)
            isShowingNativeDialog = true
        } else {
            rationaleSingleListener?.invoke(rationaleBeanList[0], PermissionRationaleToken(this))
            rationaleMultiListener?.invoke(rationaleBeanList.toTypedArray(), PermissionRationaleToken(this))
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    internal fun onContinuePermissionRequest() {
        if (!isShowingNativeDialog)
            requestPermissions(permissionMatcher.deniedPermissions.toTypedArray(), PERMISSION_REQUEST_CODE)
        isShowingNativeDialog = true
    }

    internal fun onSkipPermissionRequest() {
        isShowingNativeDialog = false
        permissionResultSingleListener?.invoke(PermissionBean(rationalePermissions[0]))


        val totalBeanGranted = permissionMatcher.grantedPermissions.map { PermissionBean(it, true) }
        val totalBeanDenied = permissionMatcher.deniedPermissions.map { PermissionBean(it) }
        val totalBeanPermanentlyDenied = permissionMatcher.permissions
                .filterNot { s -> permissionMatcher.deniedPermissions.contains(s) }
                .filterNot { s -> permissionMatcher.grantedPermissions.contains(s) }
                .map { PermissionBean(it, false, true) }
        permissionResultMultiListener?.invoke(totalBeanGranted.asSequence()
                .plus(totalBeanDenied)
                .plus(totalBeanPermanentlyDenied)
                .toList()
                .toTypedArray())
    }

    internal fun setListeners(listenerResult: (PermissionBean) -> Unit,
                              listenerResultMulti: (Array<PermissionBean>) -> Unit,
                              rationaleSingle: (PermissionBean, PermissionToken) -> Unit,
                              rationaleMulti: (Array<PermissionBean>, PermissionToken) -> Unit) {
        permissionResultSingleListener = listenerResult
        permissionResultMultiListener = listenerResultMulti
        rationaleSingleListener = rationaleSingle
        rationaleMultiListener = rationaleMulti
    }

    /*  private fun isPermissionsDialogShowing(): Boolean {
          val am = activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
          val cn = am.getRunningTasks(1).get(0).topActivity
          return "com.android.packageinstaller.permission.ui.GrantPermissionsActivity" == cn.className
      }*/
}