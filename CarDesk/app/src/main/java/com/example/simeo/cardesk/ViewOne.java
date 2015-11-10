package com.example.simeo.cardesk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ViewOne extends ActivityHelper {
    TextView txtView;
    Button btnDelete;
    DatabaseHelper myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_one);
        txtView= (TextView)findViewById(R.id.textView);
        btnDelete=(Button)findViewById(R.id.button_delete);
        myDb=new DatabaseHelper(this);


        Intent intent = getIntent();
        String value = intent.getStringExtra("key");
        String line[]= value.split("\n");
        double intquantity= Integer.parseInt(line[0].replaceAll("[^0-9]", ""));
        double intprice=Integer.parseInt(line[1].replaceAll("[^0-9]", ""));
        final String quantity[]=line[0].split(":");
        final String price[]=line[1].split(" ");
        final String date[]=line[2].split(":");
        final String table_name=line[3];
        String display;

        if(quantity[1].replaceFirst("^ *", "").contains(" ")) {
            String[] quantitySpec=quantity[1].split(" ");
            display = "You load" + quantity[1] + " fuel on " + date[1] + ". This cost you " +
                    price[1] + " " + price[2] + " or "+ Double.toString(intprice / intquantity) +
                    price[2] + " per " + quantitySpec[2].substring(0, quantitySpec[2].length()-1)+" ";
        } else {
            display = "You load " + quantity[1] + " fuel on " + date[1] + ". This cost you " + price[1] + " " + price[2] + " or "
                    + Double.toString(intprice / intquantity) + price[2] + " per litre.";

        }

        txtView.setText(display);

        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDb.delete(date[1].replaceFirst("^ *", ""),price[1] +" "+price[2],quantity[1].replaceFirst("^ *", ""),table_name);
                        Toast.makeText(ViewOne.this, "Successfull deleted", Toast.LENGTH_LONG).show();
                        History(ViewOne.this,table_name);
                    }
                }
        );

    }

}
