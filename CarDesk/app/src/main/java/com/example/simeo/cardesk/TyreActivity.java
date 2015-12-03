package com.example.simeo.cardesk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import mehdi.sakout.fancybuttons.FancyButton;


public class TyreActivity extends ActivityHelper implements DatePickerDialog.OnDateSetListener{
    DatabaseHelper myDb;
    EditText editQuantity,editPrice;
    Button btnAddData;
    Button btnHistory;
    FancyButton dateButton;
    public static final String TABlE_NAME;

    static {
        TABlE_NAME = "tyre_table";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tyre);
        myDb=new DatabaseHelper(this);

        editQuantity = (EditText)findViewById(R.id.editText_quantity);
        editPrice = (EditText)findViewById(R.id.editText_price);
        btnAddData = (Button)findViewById(R.id.button_add);
        btnHistory = (Button)findViewById(R.id.button_history);
        dateButton = (FancyButton)findViewById(R.id.date_button);

        GetCurrentDate(dateButton);
        AdGenerator();

        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long id = AddDataToTheBase(TyreActivity.this, myDb, editQuantity.getText().toString(), editPrice.getText().toString(),
                                dateButton.getText().toString(), TABlE_NAME);
                        final String value = "Quan: " + editQuantity.getText().toString() + "\n" + "Price: " + editPrice.getText().toString() + "\n" +
                                "Date: " + dateButton.getText().toString() + "\n" + TABlE_NAME + "\n" + id;
                        Intent myIntent = new Intent(TyreActivity.this, ViewOne.class);
                        myIntent.putExtra("key", value); //Optional parameters
                        TyreActivity.this.startActivity(myIntent);
                    }
                });

        btnHistory.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        History(TyreActivity.this, TABlE_NAME);
                    }
                });

        dateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SetDateButton(TyreActivity.this,"#AED581");
                    }
                });



    }
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth + "." + (++monthOfYear) + "." + year;
        dateButton.setText(date);
    }
}
