package com.thanosfisherman.mayi.sample

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.thanosfisherman.mayi.MayI
import com.thanosfisherman.mayi.PermissionBean
import com.thanosfisherman.mayi.PermissionToken
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonContacts = findViewById<Button>(R.id.contacts_permission_button)
        buttonContacts.setOnClickListener {
            MayI.withActivity(this)
                    .withPermission(Manifest.permission.READ_CONTACTS)
                    .onResult(this::permissionResultSingle)
                    .onRationale(this::permissionRationaleSingle)
                    .check()
        }

        val buttonLocation = findViewById<Button>(R.id.location_permission_button)
        buttonLocation.setOnClickListener {
            MayI.withActivity(this)
                    .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    .onResult(this::permissionResultSingle)
                    .onRationale(this::permissionRationaleSingle)
                    .check()
        }

        val buttonAll = findViewById<Button>(R.id.all_permissions_button)
        buttonAll.setOnClickListener {
            MayI.withActivity(this)
                    .withPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION)
                    .onRationale(this::permissionRationaleMulti)
                    .onResult(this::permissionResultMulti)
                    .onErrorListener(this::inCaseOfError)
                    .check()
        }
    }

    private fun permissionResultSingle(permission: PermissionBean) {
        Toast.makeText(this, "PERMISSION RESULT $permission", Toast.LENGTH_LONG).show()
        Log.i("MainActivity", "PERMISSION RESULT $permission")
    }

    private fun permissionRationaleSingle(bean: PermissionBean, token: PermissionToken) {
        if (bean.simpleName.toLowerCase().contains("contacts")) {
            Toast.makeText(this, "Should show rationale for " + bean.simpleName + " permission", Toast.LENGTH_LONG).show()
            Log.i("MainActivity", "Should show rationale for ${bean.simpleName}")
            token.skipPermissionRequest()
        } else {
            Toast.makeText(this, "Should show rationale for " + bean.simpleName + " permission", Toast.LENGTH_LONG).show()
            Log.i("MainActivity", "Should show rationale for ${bean.simpleName}")
            token.continuePermissionRequest()
        }
    }

    private fun permissionResultMulti(permissions: Array<PermissionBean>) {
        Toast.makeText(this, "MULTI PERMISSION RESULT " + Arrays.deepToString(permissions), Toast.LENGTH_LONG).show()
        Log.i("MainActivity", "MULTI PERMISSION RESULT " + Arrays.deepToString(permissions))

    }

    private fun permissionRationaleMulti(permissions: Array<PermissionBean>, token: PermissionToken) {
        Toast.makeText(this, "Rationales for Multiple Permissions " + Arrays.deepToString(permissions), Toast.LENGTH_LONG).show()
        Log.i("MainActivity", "Rationales for Multiple Permissions " + Arrays.deepToString(permissions))

        token.continuePermissionRequest()
    }

    private fun inCaseOfError(e: Exception) {
        Toast.makeText(this, "ERROR " + e.toString(), Toast.LENGTH_SHORT).show()
        Log.e("MainActivity", "ERROR " + e.toString())

    }
}