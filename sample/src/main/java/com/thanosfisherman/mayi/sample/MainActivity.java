package com.thanosfisherman.mayi.sample;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.thanosfisherman.mayi.Mayi;
import com.thanosfisherman.mayi.PermissionBean;
import com.thanosfisherman.mayi.PermissionToken;
import com.thanosfisherman.mayi.listeners.multi.PermissionResultMultiListener;
import com.thanosfisherman.mayi.listeners.multi.RationaleMultiListener;
import com.thanosfisherman.mayi.listeners.single.PermissionResultSingleListener;
import com.thanosfisherman.mayi.listeners.single.RationaleSingleListener;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonContacts = (Button) findViewById(R.id.contacts_permission_button);
        buttonContacts.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.i("MAIN", "BUTTON CONTACTS CLICKED");
                Mayi.withActivity(MainActivity.this).withPermission(Manifest.permission.READ_CONTACTS).onPermissionResult(new PermissionResultSingleListener()
                {
                    @Override
                    public void permissionResult(PermissionBean permission)
                    {
                        Log.i("Main", "PERMISSION RESULT " + permission.toString());
                    }
                }).onPermissionRationaleShouldBeShown(new RationaleSingleListener()
                {
                    @Override
                    public void onRationale(PermissionToken token)
                    {
                        Log.i("Main", "show rationale");
                        token.cancelPermissionRequest();
                    }
                }).onErrorListener(null).check();
            }
        });

        Button buttonAll = (Button) findViewById(R.id.all_permissions_button);
        buttonAll.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Mayi.withActivity(MainActivity.this)
                    .withPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION)
                    .onPermissionRationaleShouldBeShown(new RationaleMultiListener()
                    {
                        @Override
                        public void onRationale(PermissionBean[] permissions, PermissionToken token)
                        {
                            Log.i("Main", "Rationales for multiple permissions " + Arrays.deepToString(permissions));
                            token.continuePermissionRequest();
                        }
                    })
                    .onPermissionResult(new PermissionResultMultiListener()
                    {
                        @Override
                        public void permissionResults(PermissionBean[] permissions)
                        {
                            Log.i("Main", "MULTI PERMISSIONS RESULT " + Arrays.deepToString(permissions));
                        }
                    })
                    .onErrorListener(null)
                    .check();
            }
        });
    }
}