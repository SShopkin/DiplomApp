package com.example.simeo.cardesk;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import mehdi.sakout.fancybuttons.FancyButton;


public class WashActivity extends ActivityHelper implements DatePickerDialog.OnDateSetListener{
    DatabaseHelper myDb;
    EditText editMileage,editPrice,editNote;
    FancyButton btnAddData,btnHistory,dateButton;
    public static final String TABlE_NAME;

    static {
        TABlE_NAME = "clean_table";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wash);
        myDb=new DatabaseHelper(this);


        dateButton = (FancyButton)findViewById(R.id.date_button);
        editMileage = (EditText)findViewById(R.id.editText_mileage);
        editPrice = (EditText) findViewById(R.id.editText_price);
        editNote = (EditText) findViewById(R.id.editText_note);
        btnAddData = (FancyButton)findViewById(R.id.button_add);
        btnHistory = (FancyButton)findViewById(R.id.button_history);

        ToolBar("Car Wash");
        editMileage.setText(myDb.currentMileage());
        GetCurrentDate(dateButton);
        AdGenerator();

        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isMileageOk(WashActivity.this,myDb.currentMileage(),editMileage.getText().toString())) {
                            long id = addCleaning(WashActivity.this, myDb, editPrice.getText().toString(), dateForBase(dateButton.getText().toString()),
                                    editMileage.getText().toString(), editNote.getText().toString());
                            final String value = TABlE_NAME + "\n" + id;
                            Intent myIntent = new Intent(WashActivity.this, ViewFS.class);
                            myIntent.putExtra("key", value);
                            WashActivity.this.startActivity(myIntent);
                        }
                    }
                });

        btnHistory.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        History(WashActivity.this, TABlE_NAME);
                    }
                });

        dateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SetDateButton(WashActivity.this,"#9C27B0");
                    }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth+"."+(++monthOfYear)+"."+year;
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
                startActivity(new Intent(WashActivity.this, SettingsActivity.class));
                return true;
            default:
                Intent myIntent = new Intent(WashActivity.this, MainActivity.class);
                WashActivity.this.startActivity(myIntent);
                return true;
        }
    }
}
