package com.example.simeo.cardesk;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import mehdi.sakout.fancybuttons.FancyButton;

public class ViewFS extends ActivityHelper {
    TextView txtView;
    FancyButton btnDelete;
    FancyButton btnEdit;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_fs);
        txtView= (TextView)findViewById(R.id.textView);
        btnDelete=(FancyButton)findViewById(R.id.button_delete);
        btnEdit=(FancyButton)findViewById(R.id.button_edit);
        myDb=new DatabaseHelper(this);

        ToolBar("Record");
        AdGenerator();

        Intent intent = getIntent();
        String value = intent.getStringExtra("key");
        String line[]= value.split("\n");
        double numQuantity;
        double numPrice;
        double numPricePerQuantity;
        final String firstLine=line[0].split(" ")[1];
        final String price=line[1].split(" ")[1];
        final String date=line[2].split(" ")[1];
        final String mileage=line[3];
        final String note=line[4];
        final String table_name=line[5];
        final String id=line[6];
        final String liquidMeasure=GetSetting(myDb).split(":")[0];
        final String distanceMeasure=GetSetting(myDb).split(":")[1];
        final String currencyMeasure=GetSetting(myDb).split(":")[2];
        String display;

        if ("fuel_table".equals(table_name)) {
            numQuantity = Double.parseDouble(firstLine);
            numPrice = Double.parseDouble(price);
            numPricePerQuantity = numPrice / numQuantity;
            numPricePerQuantity = Math.floor(numPricePerQuantity * 100) / 100;
            display =firstLine+" "+liquidMeasure+"s fuel on " + date+" and this cost you "+ price
                    +" "+currencyMeasure+" or " +Double.toString(numPricePerQuantity) + " " +
                    currencyMeasure+" per "+liquidMeasure+".";
            if(!("".equals(mileage))){
                display+= " You do this at "+ mileage+" "+distanceMeasure+". ";
            }
            if(!("".equals(note))){
                display+= note;
            }
        } else if("service_table".equals(table_name)){
            display ="What: "+firstLine+" on " + date+" and this cost you "+ price + " "+ currencyMeasure +".";
            if(!("".equals(mileage))){
                display+= " You do this at "+ mileage+" "+distanceMeasure+". ";
            }
            if(!("".equals(note))){
                display+= note;
            }
        } else if("ins_table".equals(table_name)){
            display ="You made insurance at "+price + " and it is valid to "+ date +
                    ". This cost you " + firstLine +" "+ currencyMeasure+". ";
            if(!("".equals(mileage))){
                display+= mileage;
            }
        } else {
            display ="What: ";
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
                        String value = firstLine + "\n" + price + "\n" + date + "\n" + mileage + "\n" + id  + "\n" + table_name;
                        Intent myIntent = new Intent(ViewFS.this, EditActivity.class);
                        myIntent.putExtra("key", value);
                        ViewFS.this.startActivity(myIntent);
                    }
                }
        );

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
                startActivity(new Intent(ViewFS.this, SettingsActivity.class));
                return true;
            default:
                onBackPressed();
                return true;
        }
    }

}
