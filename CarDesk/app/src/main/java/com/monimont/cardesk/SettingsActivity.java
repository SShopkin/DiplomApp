package com.monimont.cardesk;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import fr.ganfra.materialspinner.MaterialSpinner;
import mehdi.sakout.fancybuttons.FancyButton;


public class SettingsActivity extends ActivityHelper {
    DatabaseHelper myDb;
    FancyButton btnApply,btnText;
    String newLiquid,newDistance,newCurrency,liquid,distance,currency;
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
        btnText = (FancyButton)findViewById(R.id.text_button);

        liquid=myDb.getSettings().split(":")[0];
        distance=myDb.getSettings().split(":")[1];
        currency=myDb.getSettings().split(":")[2];

        btnText.setText(getString(R.string.set_your)+liquid+", "+distance+", "+currency+".");

        String[] LIQUIDS = {getString(R.string.litre_currency), getString(R.string.gallon_currency),getString(R.string.automatic)};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, LIQUIDS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLiquid = (MaterialSpinner)findViewById(R.id.spinner1);
        spinnerLiquid.setAdapter(adapter);
        spinnerLiquid.setSelection(3);

        String[] Distances = {getString(R.string.kilometer_currency), getString(R.string.mile_currency),getString(R.string.automatic)};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Distances);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDistance = (MaterialSpinner)findViewById(R.id.spinner2);
        spinnerDistance.setAdapter(adapter1);
        spinnerDistance.setSelection(3);

        String[] Currencies = {getString(R.string.euros_currency), getString(R.string.levs_currency), getString(R.string.dollars_currency),getString(R.string.automatic)};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Currencies);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCurrency = (MaterialSpinner)findViewById(R.id.spinner3);
        spinnerCurrency.setAdapter(adapter2);
        spinnerCurrency.setSelection(4);

        setStatusBarColor(findViewById(R.id.statusBarBackground), getResources().getColor(R.color.accentPurple));
        toolBar(getString(R.string.service_toolbar));
        adGenerator();


        btnApply.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newLiquid=spinnerLiquid.getSelectedItem().toString();
                        if(getString(R.string.automatic).equals(newLiquid)){
                            newLiquid=getResources().getString(R.string.auto_liquid_measure);
                        }
                        newDistance=spinnerDistance.getSelectedItem().toString();
                        if(getString(R.string.automatic).equals(newDistance)){
                            newDistance=getResources().getString(R.string.auto_distance_measure);
                        }
                        newCurrency=spinnerCurrency.getSelectedItem().toString();
                        if(getString(R.string.automatic).equals(newCurrency)){
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
