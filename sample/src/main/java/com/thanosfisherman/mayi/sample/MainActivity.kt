package com.thanosfisherman.mayi.sample

import android.Manifest
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.thanosfisherman.mayi.Mayi
import com.thanosfisherman.mayi.PermissionBean
import com.thanosfisherman.mayi.PermissionToken
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonContacts = findViewById<Button>(R.id.contacts_permission_button)
        buttonContacts.setOnClickListener { _ ->
            Mayi.withActivity(this)
                    .withPermission(Manifest.permission.READ_CONTACTS)
                    .onResult { this.permissionResultSingle(it) }
                    .onRationale { bean, token -> this.permissionRationaleSingle(bean, token) }
                    .check()
        }

        val buttonLocation = findViewById<Button>(R.id.location_permission_button)
        buttonLocation.setOnClickListener { _ ->
            Mayi.withActivity(this)
                    .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    .onResult { this.permissionResultSingle(it) }
                    .onRationale { bean, token -> this.permissionRationaleSingle(bean, token) }
                    .check()
        }

        val buttonAll = findViewById<Button>(R.id.all_permissions_button)
        buttonAll.setOnClickListener { _ ->
            Mayi.withActivity(this)
                    .withPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION)
                    .onRationale { permissions, token -> this.permissionRationaleMulti(permissions, token) }
                    .onResult { this.permissionResultMulti(it) }
                    .onErrorListener { this.inCaseOfError(it) }
                    .check()
        }
    }

    private fun permissionResultSingle(permission: PermissionBean) {
        Toast.makeText(this, "PERMISSION RESULT $permission", Toast.LENGTH_LONG).show()
    }

    private fun permissionRationaleSingle(bean: PermissionBean, token: PermissionToken) {
        if (bean.simpleName.toLowerCase().contains("contacts")) {
            Toast.makeText(this, "Should show rationale for " + bean.simpleName + " permission", Toast.LENGTH_LONG).show()
            token.skipPermissionRequest()
        } else {
            Toast.makeText(this, "Should show rationale for " + bean.simpleName + " permission", Toast.LENGTH_LONG).show()
            token.continuePermissionRequest()
        }
    }

    private fun permissionResultMulti(permissions: Array<PermissionBean>) {
        Toast.makeText(this, "MULTI PERMISSION RESULT " + Arrays.deepToString(permissions), Toast.LENGTH_LONG).show()
    }

    private fun permissionRationaleMulti(permissions: Array<PermissionBean>, token: PermissionToken) {
        Toast.makeText(this, "Rationales for Multiple Permissions " + Arrays.deepToString(permissions), Toast.LENGTH_LONG).show()
        token.continuePermissionRequest()
    }

    private fun inCaseOfError(e: Exception) {
        Toast.makeText(this, "ERROR " + e.toString(), Toast.LENGTH_SHORT).show()
    }
}