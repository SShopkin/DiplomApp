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
    Button btnEdit;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_one);
        txtView= (TextView)findViewById(R.id.textView);
        btnDelete=(Button)findViewById(R.id.button_delete);
        btnEdit=(Button)findViewById(R.id.button_edit);
        myDb=new DatabaseHelper(this);

        AdGenerator();

        Intent intent = getIntent();
        String value = intent.getStringExtra("key");
        String line[]= value.split("\n");
        double intquantity=1;
        double intprice=0;
        final String firstLine[]=line[0].split(":");
        final String price[]=line[1].split(" ");
        final String date[]=line[2].split(":");
        final String table_name=line[3];
        final String id=line[4];
        String display;

        if ("fuel_table".equals(table_name)) {
            if (line[0].matches(".*\\d+.*")) {
                intquantity = Integer.parseInt(line[0].replaceAll("[^0-9]", ""));
                intprice = Integer.parseInt(line[1].replaceAll("[^0-9]", ""));
            }
            if (firstLine[1].replaceFirst("^ *", "").contains(" ")) {
                String[] quantitySpec = firstLine[1].split(" ");
                display = "You load" + firstLine[1] + " fuel on " + date[1] + ". This cost you " +
                        price[1] + " " + price[2] + " or " + Double.toString(intprice / intquantity) +
                        price[2] + " per " + quantitySpec[2].substring(0, quantitySpec[2].length() - 1) + " ";
            } else {
                display = "You load " + firstLine[1] + " fuel on " + date[1] + ". This cost you " + price[1] + " " + price[2] + " or "
                        + Double.toString(intprice / intquantity) + price[2] + " per litre.";

            }
        } else if ("tyre_table".equals(table_name)) {
            display = "You change your previous tyres with "+firstLine[1]+" on " + date[1]+
                    ". This cost you " + price[1] + " " + price[2] + ".";
        } else if ("ins_table".equals(table_name)){
            display = "You made your insurance on " + date[1]+". Your new one cost you "+ price[1] +
                    " " + price[2] +" and it is valid to "+ firstLine[1] + ".";
        } else if ("lamp_table".equals(table_name)) {
            String type=firstLine[1].split("-")[0];
            String place=firstLine[1].split("-")[1];
            display = "You change your bulb of type " + type +". Bulb is from " + place +". This cost you "+
                     price[1] + " " + price[2] + " and you do it on "+date[1]+".";
        } else if ("oil_table".equals(table_name)) {
            display = "You change your oil on " + date[1]+" at"+firstLine[1]+". This cost you "+ price[1] +
                    " " + price[2]+".";
        } else {
            display = "You do: "+firstLine[1]+" on " + date[1]+" and this cost you "+ price[1] +
                    " " + price[2]+".";
        }

        txtView.setText(display);

        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDb.delete(id, table_name);
                        Toast.makeText(ViewOne.this, "Successful deleted", Toast.LENGTH_LONG).show();
                        History(ViewOne.this, table_name);
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
