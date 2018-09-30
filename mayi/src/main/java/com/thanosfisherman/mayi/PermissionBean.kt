package com.thanosfisherman.mayi

data class PermissionBean(val name: String, val isGranted: Boolean = false, val isPermanentlyDenied: Boolean = false) {
    val simpleName: String = try {
        name.split(Regex("\\."))[2]
    } catch (e: Exception) {
        e.printStackTrace()
        name
    }
}