package com.example.simeo.cardesk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ViewFS extends ActivityHelper {
    TextView txtView;
    Button btnDelete;
    Button btnEdit;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_fs);
        txtView= (TextView)findViewById(R.id.textView);
        btnDelete=(Button)findViewById(R.id.button_delete);
        btnEdit=(Button)findViewById(R.id.button_edit);
        myDb=new DatabaseHelper(this);

        AdGenerator();

        Intent intent = getIntent();
        String value = intent.getStringExtra("key");
        String line[]= value.split("\n");
        double intquantity;
        double intprice;
        double pricePerQuan;
        final String firstLine=line[0].split(" ")[1];
        final String price=line[1].split(" ")[1];
        final String date=line[2].split(" ")[1];
        final String mileage=line[3];
        final String note=line[4];
        final String table_name=line[5];
        final String id=line[6];
        String display;

        if ("fuel_table".equals(table_name)) {
            intquantity = Integer.parseInt(firstLine);
            intprice = Integer.parseInt(price);
            pricePerQuan = intprice / intquantity;
            pricePerQuan = Math.floor(pricePerQuan * 100) / 100;
            display =firstLine+" fuel on " + date+" and this cost you "+ price +" or " +Double.toString(pricePerQuan) +" per ";
            if(!("".equals(mileage))){
                display+= " You do this at "+ mileage+". ";
            }
            if(!("".equals(note))){
                display+= note;
            }
        } else {
            display ="What: "+firstLine+" on " + date+" and this cost you "+ price +".";
            if(!("".equals(mileage))){
                display+= " You do this at "+ mileage+". ";
            }
            if(!("".equals(note))){
                display+= note;
            }
        }

        txtView.setText(display);

        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDb.delete(id, table_name);
                        Toast.makeText(ViewFS.this, "Successful deleted", Toast.LENGTH_LONG).show();
                        History(ViewFS.this, table_name);
                    }
                }
        );

        btnEdit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }
        );

    }

}
