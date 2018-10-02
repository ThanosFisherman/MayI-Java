package com.thanosfisherman.mayi.listeners

import com.thanosfisherman.mayi.PermissionBean
import com.thanosfisherman.mayi.PermissionToken

interface IPermissionBuilder {
    fun onErrorListener(errorListener: (Exception) -> Unit): IPermissionBuilder
    fun check()

    interface Permission {
        fun withPermission(permission: String): SinglePermissionBuilder

        fun withPermissions(vararg permissions: String): IPermissionBuilder.MultiPermissionBuilder

        //IPermissionBuilder.MultiPermissionBuilder withPermissions(Collection<String> permissions);
    }

    interface SinglePermissionBuilder : IPermissionBuilder {
        fun onResult(response: (PermissionBean) -> Unit): SinglePermissionBuilder
        fun onRationale(rationale: (PermissionBean, PermissionToken) -> Unit): SinglePermissionBuilder
    }

    interface MultiPermissionBuilder : IPermissionBuilder {
        fun onResult(response: (Array<PermissionBean>) -> Unit): MultiPermissionBuilder
        fun onRationale(rationale: (Array<PermissionBean>, PermissionToken) -> Unit): MultiPermissionBuilder
    }
}
