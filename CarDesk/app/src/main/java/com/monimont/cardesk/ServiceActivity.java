package com.monimont.cardesk;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import fr.ganfra.materialspinner.MaterialSpinner;
import mehdi.sakout.fancybuttons.FancyButton;


public class ServiceActivity extends ActivityHelper implements DatePickerDialog.OnDateSetListener{
    DatabaseHelper myDb;
    EditText editQuantity,editPrice,editNote,editMileage;
    FancyButton btnAddData;
    FancyButton btnHistory;
    FancyButton dateButton;
    MaterialSpinner spinner;
    public static final String TABlE_NAME;

    static {
        TABlE_NAME = "service_table";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        myDb=new DatabaseHelper(this);

        editQuantity = (EditText)findViewById(R.id.editText_quantity);
        editPrice = (EditText)findViewById(R.id.editText_price);
        editNote = (EditText)findViewById(R.id.editText_note);
        editMileage = (EditText)findViewById(R.id.editText_mileage);
        btnAddData = (FancyButton)findViewById(R.id.button_add);
        btnHistory = (FancyButton)findViewById(R.id.button_history);
        dateButton = (FancyButton)findViewById(R.id.date_button);

        String[] ITEMS = {getString(R.string.oil_change), getString(R.string.tyre_change), getString(R.string.blub_change), getString(R.string.abs_change), getString(R.string.brake_system), getString(R.string.other)};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = (MaterialSpinner)findViewById(R.id.spinner);
        spinner.setHint(R.string.service);
        spinner.setAdapter(adapter);

        toolBar(getString(R.string.service_toolbar));
        getCurrentDate(dateButton);
        editMileage.setText(myDb.currentMileage());
        adGenerator();

        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if((isMileageOk(ServiceActivity.this,myDb.currentMileage(),editMileage.getText().toString()))&&(isOptionSelected())) {
                            long id = addDataToTheBase(ServiceActivity.this, myDb, spinner.getSelectedItem().toString(), editPrice.getText().toString(),
                                    dateForBase(dateButton.getText().toString()), editMileage.getText().toString(), editNote.getText().toString(), TABlE_NAME);
                            if (id != -1) {
                                final String value = TABlE_NAME + "\n" + id;
                                Intent myIntent = new Intent(ServiceActivity.this, ViewOne.class);
                                myIntent.putExtra("key", value); //Optional parameters
                                ServiceActivity.this.startActivity(myIntent);
                            }
                        }
                    }
                });

        btnHistory.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        history(ServiceActivity.this, TABlE_NAME);
                    }
                });

        dateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setDateButton(ServiceActivity.this);
                    }
                });




    }
    
    public boolean isOptionSelected(){
        if (getString(R.string.service).equals(spinner.getSelectedItem().toString())){
            Toast.makeText(ServiceActivity.this, getString(R.string.choose_service), Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
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
                startActivity(new Intent(ServiceActivity.this, SettingsActivity.class));
                return true;
            case R.id.action_export:
                startActivity(new Intent(ServiceActivity.this, ExpImpActivity.class));
                return true;
            default:
                Intent myIntent = new Intent(ServiceActivity.this, MainActivity.class);
                ServiceActivity.this.startActivity(myIntent);
                return true;
        }
    }
}
