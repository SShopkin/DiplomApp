package com.example.simeo.cardesk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainActivity extends ActivityHelper {
    DatabaseHelper myDb;
    ImageButton btn_fuel,ins_button,search_button,wash_button,other_button,sos_button;
    EditText editMileage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb=new DatabaseHelper(this);

        btn_fuel = (ImageButton)findViewById(R.id.fuel_button);
        search_button = (ImageButton)findViewById(R.id.lookFor_button);
        ins_button = (ImageButton)findViewById(R.id.ins_button);
        wash_button = (ImageButton)findViewById(R.id.wash_button);
        sos_button = (ImageButton)findViewById(R.id.sos_button);
        other_button = (ImageButton)findViewById(R.id.other_button);
        editMileage = (EditText)findViewById(R.id.editMileage);

        /*if("null:null:null".equals(GetSetting(myDb))){
            myDb.updateSettings(getResources().getString(R.string.auto_liquid_measure),
                    getResources().getString(R.string.auto_distance_measure),
                    getResources().getString(R.string.auto_currency));
            myDb.setMileage("0");
        }*/
        AdGenerator();

        Toolbar myToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        //editMileage.setText(GetMileage(myDb));

        btn_fuel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDb.updateMileage(editMileage.getText().toString(),GetMileage(myDb));
                        startActivity(new Intent(MainActivity.this, FuelActivity.class));
                    }
                }
        );

        search_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, SearchActivity.class));
                    }
                }
        );

        sos_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, SOSActivity.class));
                    }
                }
        );

        ins_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, InsActivity.class));
                    }
                }
        );

        wash_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, WashActivity.class));
                    }
                }
        );

        other_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, ServiceActivity.class));
                    }
                }
        );

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
