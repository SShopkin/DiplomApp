package com.example.simeo.cardesk;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

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

        String[] ITEMS = {"Oil change", "Tyre change", "Bulb change", "Absorber change", "Brake system", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = (MaterialSpinner)findViewById(R.id.spinner);
        spinner.setAdapter(adapter);

        ToolBar("Service");
        GetCurrentDate(dateButton);
        AdGenerator();

        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long id = AddDataToTheBaseFS(ServiceActivity.this, myDb, spinner.getSelectedItem().toString(), editPrice.getText().toString(),
                                dateButton.getText().toString(), editMileage.getText().toString(), editNote.getText().toString(), TABlE_NAME);
                        if (id!=-1) {
                            final String value = spinner.getSelectedItem().toString() + "\n" +" " +editPrice.getText().toString() + "\n" +
                                    " "+dateButton.getText().toString() + "\n" + editMileage.getText().toString() + "\n" +
                                    editNote.getText().toString() + "\n" + TABlE_NAME + "\n" + id;
                            Intent myIntent = new Intent(ServiceActivity.this, ViewFS.class);
                            myIntent.putExtra("key", value); //Optional parameters
                            ServiceActivity.this.startActivity(myIntent);
                        }
                    }
                });

        btnHistory.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        History(ServiceActivity.this, TABlE_NAME);
                    }
                });

        dateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SetDateButton(ServiceActivity.this,"#FFCC80");
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
