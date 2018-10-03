package com.thanosfisherman.mayi

import android.os.Build
import androidx.annotation.RequiresApi

internal class PermissionRationaleToken(private val permissionToken: PermissionToken) : PermissionToken {

    private var isTokenResolved = false

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun continuePermissionRequest() {
        if (!isTokenResolved) {
            permissionToken.let {
                it.continuePermissionRequest()
                isTokenResolved = true
            }
        }
    }

    override fun skipPermissionRequest() {
        if (!isTokenResolved) {
            permissionToken.let {
                it.skipPermissionRequest()
                isTokenResolved = true
            }
        }
    }
}
