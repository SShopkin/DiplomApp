package com.example.simeo.cardesk;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import fr.ganfra.materialspinner.MaterialSpinner;
import mehdi.sakout.fancybuttons.FancyButton;


public class SettingsActivity extends ActivityHelper {
    DatabaseHelper myDb;
    FancyButton btnApply;
    String newLiquid,newDistance,newCurrency;
    MaterialSpinner spinnerLiquid,spinnerDistance,spinnerCurrency;
    public static final String TABlE_NAME;
    static {
        TABlE_NAME = "settings_table";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        myDb=new DatabaseHelper(this);

        btnApply = (FancyButton)findViewById(R.id.button_apply);

        String[] LIQUIDS = {"litre", "gallon","Automatic"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, LIQUIDS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLiquid = (MaterialSpinner)findViewById(R.id.spinner1);
        spinnerLiquid.setAdapter(adapter);
        spinnerLiquid.setSelection(3);

        String[] Distances = {"kilometer", "mile","Automatic"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Distances);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDistance = (MaterialSpinner)findViewById(R.id.spinner2);
        spinnerDistance.setAdapter(adapter1);
        spinnerDistance.setSelection(3);

        String[] Currencies = {"euros", "levs", "dollars","Automatic"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Currencies);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCurrency = (MaterialSpinner)findViewById(R.id.spinner3);
        spinnerCurrency.setAdapter(adapter2);
        spinnerCurrency.setSelection(4);

        ToolBar("Settings");
        AdGenerator();


        btnApply.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newLiquid=spinnerLiquid.getSelectedItem().toString();
                        if("Automatic".equals(newLiquid)){
                            newLiquid=getResources().getString(R.string.auto_liquid_measure);
                        }
                        newDistance=spinnerDistance.getSelectedItem().toString();
                        if("Automatic".equals(newDistance)){
                            newDistance=getResources().getString(R.string.auto_distance_measure);
                        }
                        newCurrency=spinnerCurrency.getSelectedItem().toString();
                        if("Automatic".equals(newCurrency)){
                            newCurrency=getResources().getString(R.string.auto_currency);
                        }

                        myDb.updateSettings(newLiquid,newDistance,newCurrency);

                        Intent myIntent = new Intent(SettingsActivity.this, MainActivity.class);
                        SettingsActivity.this.startActivity(myIntent);
                    }
                });



    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return true;
    }
}
