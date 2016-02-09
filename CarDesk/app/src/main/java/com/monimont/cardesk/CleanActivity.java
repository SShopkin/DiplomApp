package com.monimont.cardesk;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import mehdi.sakout.fancybuttons.FancyButton;


public class CleanActivity extends ActivityHelper implements DatePickerDialog.OnDateSetListener{
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
        setContentView(R.layout.activity_clean);
        myDb=new DatabaseHelper(this);


        dateButton = (FancyButton)findViewById(R.id.date_button);
        editMileage = (EditText)findViewById(R.id.editText_mileage);
        editPrice = (EditText) findViewById(R.id.editText_price);
        editNote = (EditText) findViewById(R.id.editText_note);
        btnAddData = (FancyButton)findViewById(R.id.button_add);
        btnHistory = (FancyButton)findViewById(R.id.button_history);

        toolBar("Car Wash");
        editMileage.setText(myDb.currentMileage());
        getCurrentDate(dateButton);
        adGenerator();

        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isMileageOk(CleanActivity.this,myDb.currentMileage(),editMileage.getText().toString())) {
                            long id = addCleaning(CleanActivity.this, myDb, editPrice.getText().toString(), dateForBase(dateButton.getText().toString()),
                                    editMileage.getText().toString(), editNote.getText().toString());
                            final String value = TABlE_NAME + "\n" + id;
                            Intent myIntent = new Intent(CleanActivity.this, ViewOne.class);
                            myIntent.putExtra("key", value);
                            CleanActivity.this.startActivity(myIntent);
                        }
                    }
                });

        btnHistory.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        history(CleanActivity.this, TABlE_NAME);
                    }
                });

        dateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setDateButton(CleanActivity.this);
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
                startActivity(new Intent(CleanActivity.this, SettingsActivity.class));
                return true;
            case R.id.action_export:
                startActivity(new Intent(CleanActivity.this, ExpImpActivity.class));
                return true;
            default:
                Intent myIntent = new Intent(CleanActivity.this, MainActivity.class);
                CleanActivity.this.startActivity(myIntent);
                return true;
        }
    }
}
