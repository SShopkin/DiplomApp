package com.example.simeo.cardesk;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class ExpImpActivity extends ActivityHelper {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exp_imp);

        ToolBar("Export and import");
        AdGenerator();


        }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(ExpImpActivity.this, SettingsActivity.class));
                return true;
            case R.id.action_export:
                startActivity(new Intent(ExpImpActivity.this, ExpImpActivity.class));
                return true;
            default:
                Intent myIntent = new Intent(ExpImpActivity.this, MainActivity.class);
                ExpImpActivity.this.startActivity(myIntent);
                return true;
        }
    }
}


