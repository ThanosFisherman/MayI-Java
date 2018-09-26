package com.thanosfisherman.mayi

import android.os.Build
import androidx.annotation.RequiresApi

import java.lang.ref.WeakReference

internal class PermissionRationaleToken(mayiFragment: MayiFragment) : PermissionToken {
    private val mayiFragment = WeakReference(mayiFragment)
    private var isTokenResolved = false

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun continuePermissionRequest() {
        if (!isTokenResolved) {
            mayiFragment.get()?.onContinuePermissionRequest()
            isTokenResolved = true
        }
    }

    override fun skipPermissionRequest() {
        if (!isTokenResolved) {
            mayiFragment.get()?.onSkipPermissionRequest()
            isTokenResolved = true
        }
    }
}
