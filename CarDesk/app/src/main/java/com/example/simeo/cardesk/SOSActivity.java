package com.example.simeo.cardesk;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import mehdi.sakout.fancybuttons.FancyButton;

public class SOSActivity extends ActivityHelper {
    FancyButton btnEurope, btnNAmerica, btnSAmerica, btnAustralia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);
        btnEurope = (FancyButton) findViewById(R.id.europe);
        btnNAmerica = (FancyButton) findViewById(R.id.northAmerica);
        btnSAmerica = (FancyButton) findViewById(R.id.southAmerica);
        btnAustralia = (FancyButton) findViewById(R.id.australia);

        ToolBar("Road assistance");
        AdGenerator();

        btnEurope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call("tel:NUMBER");
            }
        });
        btnNAmerica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call("tel:NUMBER");
            }
        });
        btnSAmerica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call("tel:NUMBER");
            }
        });
        btnAustralia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call("tel:NUMBER");
            }
        });
    }

    public void call(String number){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(number));
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        startActivity(callIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(SOSActivity.this, SettingsActivity.class));
                return true;
            default:
                Intent myIntent = new Intent(SOSActivity.this, MainActivity.class);
                SOSActivity.this.startActivity(myIntent);
                return true;
        }
    }
}
