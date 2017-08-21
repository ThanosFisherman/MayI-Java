package com.thanosfisherman.mayi.sample;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.thanosfisherman.mayi.Mayi;
import com.thanosfisherman.mayi.PermissionBean;
import com.thanosfisherman.mayi.PermissionToken;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonContacts = findViewById(R.id.contacts_permission_button);
        buttonContacts.setOnClickListener(v -> Mayi.withActivity(this)
                                                   .withPermission(Manifest.permission.READ_CONTACTS)
                                                   .onResult(this::permissionResultSingle)
                                                   .onRationale(this::permissionRationaleSingle)
                                                   .check());

        Button buttonLocation = findViewById(R.id.location_permission_button);
        buttonLocation.setOnClickListener(v -> Mayi.withActivity(this)
                                                   .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                                                   .onResult(this::permissionResultSingle)
                                                   .onRationale(this::permissionRationaleSingle)
                                                   .check());

        Button buttonAll = findViewById(R.id.all_permissions_button);
        buttonAll.setOnClickListener(v -> Mayi.withActivity(this)
                                              .withPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION)
                                              .onRationale(this::permissionRationaleMulti)
                                              .onResult(this::permissionResultMulti)
                                              .onErrorListener(this::inCaseOfError)
                                              .check());
    }

    private void permissionResultSingle(PermissionBean permission)
    {
        Toast.makeText(MainActivity.this, "PERMISSION RESULT " + permission, Toast.LENGTH_LONG).show();
    }

    private void permissionRationaleSingle(PermissionBean bean, PermissionToken token)
    {
        if (bean.getSimpleName().toLowerCase().contains("contacts"))
        {
            Toast.makeText(MainActivity.this, "Should show rationale for " + bean.getSimpleName() + " permission", Toast.LENGTH_LONG).show();
            token.skipPermissionRequest();
        }
        else
        {
            Toast.makeText(MainActivity.this, "Should show rationale for " + bean.getSimpleName() + " permission", Toast.LENGTH_LONG).show();
            token.continuePermissionRequest();
        }
    }

    private void permissionResultMulti(PermissionBean[] permissions)
    {
        Toast.makeText(MainActivity.this, "MULTI PERMISSION RESULT " + Arrays.deepToString(permissions), Toast.LENGTH_LONG).show();
    }

    private void permissionRationaleMulti(PermissionBean[] permissions, PermissionToken token)
    {
        Toast.makeText(MainActivity.this, "Rationales for Multiple Permissions " + Arrays.deepToString(permissions), Toast.LENGTH_LONG).show();
        token.continuePermissionRequest();
    }

    private void inCaseOfError(Exception e)
    {
        Toast.makeText(MainActivity.this, "ERROR " + e.toString(), Toast.LENGTH_SHORT).show();
    }
}