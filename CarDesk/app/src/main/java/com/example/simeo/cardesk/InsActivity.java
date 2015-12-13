package com.example.simeo.cardesk;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import mehdi.sakout.fancybuttons.FancyButton;


public class InsActivity extends ActivityHelper implements DatePickerDialog.OnDateSetListener{
    DatabaseHelper myDb;
    EditText editValidity,editPrice;
    FancyButton btnAddData;
    FancyButton btnHistory;
    FancyButton dateButton;
    public static final String TABlE_NAME;

    static {
        TABlE_NAME = "ins_table";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ins);
        myDb=new DatabaseHelper(this);

        editValidity = (EditText)findViewById(R.id.editText_validity);
        editPrice = (EditText)findViewById(R.id.editText_price);
        btnAddData = (FancyButton)findViewById(R.id.button_add);
        btnHistory = (FancyButton)findViewById(R.id.button_history);
        dateButton = (FancyButton)findViewById(R.id.date_button);

        ToolBar("Insurance");
        GetCurrentDate(dateButton);
        AdGenerator();


        editValidity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetDateButton(InsActivity.this,"#D81B60");
            }

        });

        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long id = AddDataToTheBase(InsActivity.this, myDb, editValidity.getText().toString(), editPrice.getText().toString(),
                                dateButton.getText().toString(), TABlE_NAME);
                        final String value = "Quan: " + editValidity.getText().toString() + "\n" + "Price: " + editPrice.getText().toString() + "\n" +
                                "Date: " + dateButton.getText().toString() + "\n" + TABlE_NAME + "\n" + id;
                        Intent myIntent = new Intent(InsActivity.this, ViewFS.class);
                        myIntent.putExtra("key", value); //Optional parameters
                        InsActivity.this.startActivity(myIntent);
                    }
                });

        btnHistory.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        History(InsActivity.this, TABlE_NAME);
                    }
                });

        dateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SetDateButton(InsActivity.this,"#D81B60");
                    }
                });




    }
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth + "." + (++monthOfYear) + "." + year;
        int newDate= Integer.parseInt(new StringBuffer(date.replaceAll("\\D+","")).reverse().toString());
        int buttonDate=Integer.parseInt(new StringBuffer(dateButton.getText().toString().replaceAll("\\D+", "")).reverse().toString());
        if(newDate<1000000){
            newDate=newDate*100;
        }else if(newDate<10000000){
            newDate=newDate*10;
        }
        if(buttonDate<1000000){
            buttonDate=buttonDate*100;
        }else if(buttonDate<10000000){
            buttonDate=buttonDate*10;
        }
        if(newDate > buttonDate){
            editValidity.setText(date);
        } else {
            dateButton.setText(date);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return true;
    }
}
