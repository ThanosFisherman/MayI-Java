package com.thanosfisherman.mayi.listeners

import com.thanosfisherman.mayi.listeners.multi.PermissionResultMultiListener
import com.thanosfisherman.mayi.listeners.multi.RationaleMultiListener
import com.thanosfisherman.mayi.listeners.single.PermissionResultSingleListener
import com.thanosfisherman.mayi.listeners.single.RationaleSingleListener

interface IPermissionBuilder {
    fun onErrorListener(errorListener: MayiErrorListener): IPermissionBuilder
    fun check()

    interface Permission {
        fun withPermission(permission: String): SinglePermissionBuilder

        fun withPermissions(vararg permissions: String): IPermissionBuilder.MultiPermissionBuilder

        //IPermissionBuilder.MultiPermissionBuilder withPermissions(Collection<String> permissions);
    }

    interface SinglePermissionBuilder : IPermissionBuilder {
        fun onResult(response: PermissionResultSingleListener): SinglePermissionBuilder
        fun onRationale(rationale: RationaleSingleListener): SinglePermissionBuilder
    }

    interface MultiPermissionBuilder : IPermissionBuilder {
        fun onResult(response: PermissionResultMultiListener): MultiPermissionBuilder
        fun onRationale(rationale: RationaleMultiListener): MultiPermissionBuilder
    }
}
