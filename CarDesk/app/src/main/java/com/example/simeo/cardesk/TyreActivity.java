package com.example.simeo.cardesk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class TyreActivity extends ActivityHelper {
    DatabaseHelper myDb;
    EditText editQuantity,editPrice,editDate;
    Button btnAddData;
    Button btnHistory;
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
        editDate = (EditText)findViewById(R.id.editText_date);
        btnAddData = (Button)findViewById(R.id.button_add);
        btnHistory = (Button)findViewById(R.id.button_history);


        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddData(TyreActivity.this, myDb, editQuantity.getText().toString(), editPrice.getText().toString(),
                                editDate.getText().toString(), TABlE_NAME);
                        final String value = "Quan: " + editQuantity.getText().toString() + "\n" + "Price: " + editPrice.getText().toString() + "\n" +
                                "Date: " + editDate.getText().toString() + "\n" + TABlE_NAME;
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




    }





}
