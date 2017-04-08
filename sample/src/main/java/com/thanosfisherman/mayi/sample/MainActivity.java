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
import com.thanosfisherman.mayi.listeners.single.PermissionResultSingleListener;
import com.thanosfisherman.mayi.listeners.single.RationaleSingleListener;

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
                        token.continuePermissionRequest();
                    }
                }).onErrorListener(null).check();
            }
        });
    }
}