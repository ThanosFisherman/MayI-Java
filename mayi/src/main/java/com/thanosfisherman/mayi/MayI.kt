package com.thanosfisherman.mayi

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import java.lang.ref.WeakReference
import java.util.*

class MayI private constructor(activity: Activity) : IPermissionBuilder,
        IPermissionBuilder.Permission,
        IPermissionBuilder.SinglePermissionBuilder,
        IPermissionBuilder.MultiPermissionBuilder {

    val activity = WeakReference<Activity>(activity)
    private var errorListener: ((Exception) -> Unit)? = null
    private var permissionResultSingleListener: ((PermissionBean) -> Unit)? = null
    private var rationaleSingleListener: ((PermissionBean, PermissionToken) -> Unit)? = null
    private var permissionResultMultiListener: ((Array<PermissionBean>) -> Unit)? = null
    private var rationaleMultiListener: ((Array<PermissionBean>, PermissionToken) -> Unit)? = null
    private lateinit var permissions: Array<out String>
    private var isRationaleCalled = false
    private var isResultCalled = false

    companion object {
        fun withActivity(activity: Activity): IPermissionBuilder.Permission {
            return MayI(activity)
        }
    }

    override fun onErrorListener(errorListener: (Exception) -> Unit): IPermissionBuilder {
        this.errorListener = errorListener
        return this
    }

    override fun check() {
        try {
            if (permissions.isEmpty())
                throw IllegalArgumentException("You must specify at least one valid permission to check")
            if (Arrays.asList<String>(*permissions).contains(null))
                throw IllegalArgumentException("Permssions arguments must NOT contain null values")

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
                grandEverything()
            else {
                val matcher = PermissionMatcher(permissions, activity)
                if (matcher.isAllGranted)
                    grandEverything()
                else
                    initializeFragmentAndCheck(matcher)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            errorListener?.invoke(e)
        }
    }

    override fun withPermission(permission: String): IPermissionBuilder.SinglePermissionBuilder {
        this.permissions = arrayOf(permission)
        return this
    }

    override fun withPermissions(vararg permissions: String): IPermissionBuilder.MultiPermissionBuilder {
        this.permissions = permissions
        return this
    }

    override fun onResult(response: (PermissionBean) -> Unit): IPermissionBuilder.SinglePermissionBuilder {
        if (!isResultCalled) {
            permissionResultSingleListener = response
            isResultCalled = true
        }
        return this
    }

    override fun onRationale(rationale: (PermissionBean, PermissionToken) -> Unit): IPermissionBuilder.SinglePermissionBuilder {
        if (!isRationaleCalled) {
            rationaleSingleListener = rationale
            isRationaleCalled = true
        }
        return this
    }

    override fun onResult(response: (Array<PermissionBean>) -> Unit): IPermissionBuilder.MultiPermissionBuilder {
        if (!isResultCalled) {
            permissionResultMultiListener = response
            isResultCalled = true
        }
        return this
    }

    override fun onRationale(rationale: (Array<PermissionBean>, PermissionToken) -> Unit): IPermissionBuilder.MultiPermissionBuilder {
        if (!isRationaleCalled) {
            rationaleMultiListener = rationale
            isRationaleCalled = true
        }
        return this
    }

    @Suppress("DEPRECATION")
    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun initializeFragmentAndCheck(permissionMatcher: PermissionMatcher) {
        var mayiFrag: MayIFragment? = activity.get()?.fragmentManager?.findFragmentByTag(MayIFragment.TAG) as MayIFragment?

        mayiFrag = mayiFrag ?: kotlin.run {
            val fragmentManager = activity.get()!!.fragmentManager
            val frag = MayIFragment().apply { retainInstance = true }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                fragmentManager.beginTransaction().add(frag, MayIFragment.TAG).commit()
                fragmentManager.executePendingTransactions()
            } else
                fragmentManager.beginTransaction().add(frag, MayIFragment.TAG).commitNow()
            return@run frag
        }

        mayiFrag.let {
            it.setListeners(permissionResultSingleListener, permissionResultMultiListener, rationaleSingleListener, rationaleMultiListener)
            it.checkPermissions(permissionMatcher)
        }
    }

    private fun grandEverything() {
        val beans = Array(permissions.size) { PermissionBean(permissions[it], true, false) }
        //TWO DIFFERENT WAYS TO CALL A FUNCTION
        permissionResultSingleListener?.let { it(beans[0]) } //SAME AS BELOW
        permissionResultMultiListener?.invoke(beans) //SAME AS ABOVE

    }
}