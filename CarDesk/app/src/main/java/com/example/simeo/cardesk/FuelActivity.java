package com.example.simeo.cardesk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import mehdi.sakout.fancybuttons.FancyButton;


public class FuelActivity extends ActivityHelper implements DatePickerDialog.OnDateSetListener{
    DatabaseHelper myDb;
    EditText editQuantity,editPrice;
    FancyButton btnAddData;
    FancyButton btnHistory;
    Button dateButton;
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
        btnAddData = (FancyButton)findViewById(R.id.button_add);
        btnHistory = (FancyButton)findViewById(R.id.button_history);
        dateButton = (Button)findViewById(R.id.date_button);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("FUEL");

        setSupportActionBar(myToolbar);
        myToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));


        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(FuelActivity.this, MainActivity.class);
                startActivity(myIntent);
            }
        });

        GetCurrentDate(dateButton);
        AdGenerator();

        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long id = AddDataToTheBase(FuelActivity.this, myDb, editQuantity.getText().toString(), editPrice.getText().toString(),
                                dateButton.getText().toString(), TABlE_NAME);
                        final String value = "Quan: " + editQuantity.getText().toString() + "\n" + "Price: " + editPrice.getText().toString() + "\n" +
                                "Date: " + dateButton.getText().toString() + "\n" + TABlE_NAME + "\n" + id;
                        Intent myIntent = new Intent(FuelActivity.this, ViewOne.class);
                        myIntent.putExtra("key", value); //Optional parameters
                        FuelActivity.this.startActivity(myIntent);
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
                        SetDateButton(FuelActivity.this);
                    }
                });

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth + "." + (++monthOfYear) + "." + year;
        dateButton.setText(date);
    }
}
