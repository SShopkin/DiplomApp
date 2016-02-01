package com.example.simeo.cardesk;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import mehdi.sakout.fancybuttons.FancyButton;


public class InsActivity extends ActivityHelper implements DatePickerDialog.OnDateSetListener{
    DatabaseHelper myDb;
    EditText editValidity,editPrice,editNote,editMileage;
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
        editNote = (EditText)findViewById(R.id.editText_note);
        editMileage = (EditText)findViewById(R.id.editText_mileage);
        btnAddData = (FancyButton)findViewById(R.id.button_add);
        btnHistory = (FancyButton)findViewById(R.id.button_history);
        dateButton = (FancyButton)findViewById(R.id.date_button);

        toolBar("Insurance");
        getCurrentDate(dateButton);
        adGenerator();
        editMileage.setText(myDb.currentMileage());

        editValidity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateButton(InsActivity.this);
            }

        });

        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isMileageOk(InsActivity.this,myDb.currentMileage(),editMileage.getText().toString())) {
                            long id = addDataToTheBase(InsActivity.this, myDb, editValidity.getText().toString(), editPrice.getText().toString(),
                                    dateForBase(dateButton.getText().toString()), editMileage.getText().toString()
                                    , editNote.getText().toString(), TABlE_NAME);
                            if (id != -1) {
                                final String value = TABlE_NAME + "\n" + id;
                                Intent myIntent = new Intent(InsActivity.this, ViewOne.class);
                                myIntent.putExtra("key", value);
                                InsActivity.this.startActivity(myIntent);
                            }
                        }
                    }
                });

        btnHistory.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        history(InsActivity.this, TABlE_NAME);
                    }
                });

        dateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setDateButton(InsActivity.this);
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
        int buttonYear=Integer.parseInt(dateButton.getText().toString().split("\\.")[2]);
        int buttonMonth=Integer.parseInt(dateButton.getText().toString().split("\\.")[1]);
        int buttonDay=Integer.parseInt(dateButton.getText().toString().split("\\.")[0]);
       if(year > buttonYear){
            editValidity.setText(date);
        } else if((monthOfYear > buttonMonth)&&(year==buttonYear)){
            editValidity.setText(date);
        } else if((dayOfMonth > buttonDay)&&(monthOfYear==buttonMonth)&&(year==buttonYear)){
            editValidity.setText(date);
        } else {
            dateButton.setText(date);
        }
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
                startActivity(new Intent(InsActivity.this, SettingsActivity.class));
                return true;
            case R.id.action_export:
                startActivity(new Intent(InsActivity.this, ExpImpActivity.class));
                return true;
            default:
                Intent myIntent = new Intent(InsActivity.this, MainActivity.class);
                InsActivity.this.startActivity(myIntent);
                return true;
        }
    }
}
