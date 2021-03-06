package com.monimont.cardesk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends ActivityHelper {
    DatabaseHelper myDb;
    ImageButton btn_fuel,ins_button,search_button,wash_button,other_button,sos_button;
    EditText editMileage;
    final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    final Date date = new Date();


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

        setStatusBarColor(findViewById(R.id.statusBarBackground),getResources().getColor(R.color.accentOrange));


        if("null:null:null".equals(myDb.getSettings())){
            myDb.updateSettings(getResources().getString(R.string.auto_liquid_measure),
                    getResources().getString(R.string.auto_distance_measure),
                    getResources().getString(R.string.auto_currency));
            myDb.insertEnter("0",dateFormat.format(date),"0");
        }
        adGenerator();
        final String currentMileage=myDb.currentMileage();
        editMileage.setText(currentMileage);
        Toolbar myToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);


        btn_fuel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mileage(currentMileage);
                        startActivity(new Intent(MainActivity.this, FuelActivity.class));
                    }
                }
        );

        search_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mileage(currentMileage);
                        startActivity(new Intent(MainActivity.this, StatisticActivity.class));
                    }
                }
        );

        sos_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mileage(currentMileage);
                        startActivity(new Intent(MainActivity.this, SOSActivity.class));
                    }
                }
        );

        ins_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mileage(currentMileage);
                        startActivity(new Intent(MainActivity.this, InsActivity.class));
                    }
                }
        );

        wash_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mileage(currentMileage);
                        startActivity(new Intent(MainActivity.this, CleanActivity.class));
                    }
                }
        );

        other_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mileage(currentMileage);
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
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            case R.id.action_export:
                startActivity(new Intent(MainActivity.this, ExpImpActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void mileage(String oldMileage){
        if(isMileageOk(MainActivity.this,myDb.currentMileage(),editMileage.getText().toString())){
            if(!(oldMileage.equals(editMileage.getText().toString()))) {
                myDb.insertEnter("0", dateFormat.format(date), editMileage.getText().toString());
            }
        }
    }

}
