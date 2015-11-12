package com.example.simeo.cardesk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


    public class LampActivity extends ActivityHelper {
    DatabaseHelper myDb;
    EditText editQuantity,editPrice,editDate;
    Button btnAddData;
    Button btnHistory;
    public static final String TABlE_NAME;

    static {
        TABlE_NAME = "lamp_table";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lamp);
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
                        AddData(LampActivity.this, myDb, editQuantity.getText().toString(), editPrice.getText().toString(),
                                editDate.getText().toString(),TABlE_NAME);
                        final String value = "Quan: "+editQuantity.getText().toString()+"\n"+"Price: "+editPrice.getText().toString()+"\n"+
                                "Date: "+editDate.getText().toString()+"\n"+TABlE_NAME;
                        Intent myIntent = new Intent(LampActivity.this, ViewOne.class);
                        myIntent.putExtra("key", value); //Optional parameters
                        LampActivity.this.startActivity(myIntent);
                    }
                });

        btnHistory.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        History(LampActivity.this,"lamp_table");
                    }
                });


    }


}
