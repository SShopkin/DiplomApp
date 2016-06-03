package com.monimont.cardesk;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.github.lguipeng.library.animcheckbox.AnimCheckBox;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import mehdi.sakout.fancybuttons.FancyButton;


public class FuelActivity extends ActivityHelper implements DatePickerDialog.OnDateSetListener{
    DatabaseHelper myDb;
    EditText editQuantity,editPrice,editMileage;
    FancyButton btnAddData,btnHistory,dateButton;
    AnimCheckBox isItFull;
    String tank="";
    boolean checked=false;
    public static final String TABlE_NAME;

    static {
        TABlE_NAME = "fuel_table";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel);
        myDb=new DatabaseHelper(this);

        editQuantity = (EditText)findViewById(R.id.editText_quantity);
        editPrice = (EditText)findViewById(R.id.editText_price);
        editMileage = (EditText)findViewById(R.id.editText_mileage);
        btnAddData = (FancyButton)findViewById(R.id.button_add);
        btnHistory = (FancyButton)findViewById(R.id.button_history);
        dateButton = (FancyButton)findViewById(R.id.date_button);
        isItFull = (AnimCheckBox)findViewById(R.id.checkbox);

        editMileage.setText(myDb.currentMileage());

        isItFull.setChecked(checked);
        isItFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checked = !checked;
                isItFull.setChecked(checked);
                if (((AnimCheckBox) v).isChecked()) {
                    tank = getString(R.string.fuel_full_tank);
                } else {
                    tank = "";
                }

            }
        });


        setStatusBarColor(findViewById(R.id.statusBarBackground), getResources().getColor(R.color.accentOrange));
        toolBar(getString(R.string.fuel_toolbar));
        getCurrentDate(dateButton);
        adGenerator();

        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isMileageOk(FuelActivity.this,myDb.currentMileage(),editMileage.getText().toString())){
                            long id = addDataToTheBase(FuelActivity.this, myDb, editQuantity.getText().toString(), editPrice.getText().toString(),
                                    dateForBase(dateButton.getText().toString()), editMileage.getText().toString(), tank, TABlE_NAME);
                            if (id != -1) {
                                final String value = TABlE_NAME + "\n" + id;
                                Intent myIntent = new Intent(FuelActivity.this, ViewOne.class);
                                myIntent.putExtra("key", value);
                                FuelActivity.this.startActivity(myIntent);
                            }
                        }
                    }
                });

        btnHistory.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        history(FuelActivity.this, "fuel_table");
                    }
                });

        dateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setDateButton(FuelActivity.this);
                    }
                });

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String day,month;
        if(((dayOfMonth)>=1)&&((dayOfMonth)<=9)){
            day = "0"+dayOfMonth;
        } else {
            day=dayOfMonth+"";
        }
        if(((monthOfYear)>=0)&&((monthOfYear)<=8)){
            month = "0"+(++monthOfYear);
        } else {
            month=(++monthOfYear)+"";
        }
        String date = day + "." + month + "." + year;
        dateButton.setText(date);
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
                startActivity(new Intent(FuelActivity.this, SettingsActivity.class));
                return true;
            case R.id.action_export:
                startActivity(new Intent(FuelActivity.this, ExpImpActivity.class));
                return true;
            default:
                Intent myIntent = new Intent(FuelActivity.this, MainActivity.class);
                FuelActivity.this.startActivity(myIntent);
                return true;
        }
    }



}
