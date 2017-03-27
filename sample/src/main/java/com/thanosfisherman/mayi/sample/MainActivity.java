package com.thanosfisherman.mayi.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.thanosfisherman.mayi.Mayi;
import com.thanosfisherman.mayi.PermissionBean;
import com.thanosfisherman.mayi.PermissionToken;
import com.thanosfisherman.mayi.listeners.PermissionResultListener;
import com.thanosfisherman.mayi.listeners.RationaleListener;

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
                Mayi.withActivity(MainActivity.this).withPermission("lelpls").onPermissionGranted(new PermissionResultListener()
                {
                    @Override
                    public void permissionResult(PermissionBean permission)
                    {
                        Log.i("Main", "GRANTED " + permission.getName());
                    }
                }).onPermissionRationaleShouldBeShown(new RationaleListener()
                {
                    @Override
                    public void onRationale(PermissionBean permission, PermissionToken token)
                    {
                        Log.i("Main", permission.getName());
                        token.continuePermissionRequest();
                    }
                });
            }
        });
    }
}