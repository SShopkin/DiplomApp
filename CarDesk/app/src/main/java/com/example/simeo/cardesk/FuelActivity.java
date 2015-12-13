package com.example.simeo.cardesk;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.github.lguipeng.library.animcheckbox.AnimCheckBox;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import mehdi.sakout.fancybuttons.FancyButton;


public class FuelActivity extends ActivityHelper implements DatePickerDialog.OnDateSetListener{
    DatabaseHelper myDb;
    EditText editQuantity,editPrice,editMileage;
    FancyButton btnAddData;
    FancyButton btnHistory;
    FancyButton dateButton;
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


        isItFull.setChecked(checked);
        isItFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checked=!checked;
                isItFull.setChecked(checked);
                if (((AnimCheckBox) v).isChecked()) {
                    tank="Your tank was full up.";
                } else {
                    tank="";
                }

            }
        });


        ToolBar("Fuel");
        GetCurrentDate(dateButton);
        AdGenerator();

        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long id = AddDataToTheBaseFS(FuelActivity.this, myDb, editQuantity.getText().toString(), editPrice.getText().toString(),
                                dateButton.getText().toString(),editMileage.getText().toString(),tank, TABlE_NAME);
                        if (id!=-1) {
                            final String value = " "+editQuantity.getText().toString() + "\n" + " " + editPrice.getText().toString() + "\n" +
                                    " " + dateButton.getText().toString() + "\n" + editMileage.getText().toString() +
                                    "\n" + tank + "\n" + TABlE_NAME + "\n" + id;
                            Intent myIntent = new Intent(FuelActivity.this, ViewFS.class);
                            myIntent.putExtra("key", value); //Optional parameters
                            FuelActivity.this.startActivity(myIntent);
                        }
                    }
                });

        btnHistory.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        History(FuelActivity.this, "fuel_table");
                    }
                });

        dateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SetDateButton(FuelActivity.this,"#AED581");
                    }
                });

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth + "." + (++monthOfYear) + "." + year;
        dateButton.setText(date);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return true;
    }



}
