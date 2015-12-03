package com.example.simeo.cardesk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends ActivityHelper {
    ImageButton btn_fuel;
    ImageButton ins_button;
    ImageButton lamp_button;
    ImageButton oil_button;
    ImageButton other_button;
    ImageButton tyre_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_fuel = (ImageButton)findViewById(R.id.fuel_button);
        lamp_button = (ImageButton)findViewById(R.id.lamp_button);
        ins_button = (ImageButton)findViewById(R.id.ins_button);
        oil_button = (ImageButton)findViewById(R.id.oil_button);
        tyre_button = (ImageButton)findViewById(R.id.tyre_button);
        other_button = (ImageButton)findViewById(R.id.other_button);

        AdGenerator();

        btn_fuel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Intent intent = new Intent(MainActivity.this, FuelActivity.class);
                        startActivity(new Intent(MainActivity.this, FuelActivity.class));
                    }
                }
        );

        lamp_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, LampActivity.class));
                    }
                }
        );

        tyre_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, TyreActivity.class));
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

        oil_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, OilActivity.class));
                    }
                }
        );

        other_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, OtherActivity.class));
                    }
                }
        );

    }


}
