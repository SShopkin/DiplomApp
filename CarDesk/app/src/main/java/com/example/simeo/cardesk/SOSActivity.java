package com.example.simeo.cardesk;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class SOSActivity extends ActivityHelper {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        ToolBar("SOS");
        AdGenerator();

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
