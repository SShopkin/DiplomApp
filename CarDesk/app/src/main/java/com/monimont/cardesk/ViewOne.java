package com.monimont.cardesk;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import mehdi.sakout.fancybuttons.FancyButton;

public class ViewOne extends ActivityHelper {
    TextView txtView;
    FancyButton btnDelete;
    FancyButton btnEdit;
    DatabaseHelper myDb;
    String enterId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_one);
        txtView= (TextView)findViewById(R.id.textView);
        btnDelete=(FancyButton)findViewById(R.id.button_delete);
        btnEdit=(FancyButton)findViewById(R.id.button_edit);
        myDb=new DatabaseHelper(this);

        toolBar("Record");
        adGenerator();

        Intent intent = getIntent();
        String value = intent.getStringExtra("key");
        String line[]= value.split("\n");
        final String tableName=line[0];
        final String id=line[1];
        double numQuantity;
        double numPrice;
        double numPricePerQuantity;
        String service="1",price="1",date="",mileage="",note="";
        final String liquidMeasure=myDb.getSettings().split(":")[0];
        final String distanceMeasure=myDb.getSettings().split(":")[1];
        final String currencyMeasure=myDb.getSettings().split(":")[2];
        String display;

        Cursor res = myDb.getOneRow(tableName, id);
        while (res.moveToNext()) {
            if("clean_table".equals(tableName)) {
                enterId = res.getString(2);
                price = res.getString(3);
                date = dateToShow(res.getString(4));
                mileage = res.getString(5);
                note = res.getString(7);
            } else {
                service = res.getString(1);
                enterId = res.getString(2);
                price = res.getString(4);
                date = dateToShow(res.getString(5));
                mileage = res.getString(6);
                note = res.getString(8);
            }
        }

        if ("fuel_table".equals(tableName)) {
            numQuantity = Double.parseDouble(service);
            numPrice = Double.parseDouble(price);
            numPricePerQuantity = numPrice / numQuantity;
            numPricePerQuantity = Math.floor(numPricePerQuantity * 100) / 100;
            display =service+" "+liquidMeasure+"s fuel on " + date+" and this cost you "+ price
                    +" "+currencyMeasure+" or " +Double.toString(numPricePerQuantity) + " " +
                    currencyMeasure+" per "+liquidMeasure+".";
            if(!("".equals(mileage))){
                display+= " You do this at "+ mileage+" "+distanceMeasure+". ";
            }
            if(null!=note){
                display+= note;
            }
        } else if("service_table".equals(tableName)){
            display ="What: "+service+" on " + date+" and this cost you "+ price + " "+ currencyMeasure +".";
            if(!("".equals(mileage))){
                display+= " You do this at "+ mileage+" "+distanceMeasure+". ";
            }
            if(null!=note){
                display+= note;
            }
        } else if("ins_table".equals(tableName)){
            display ="You made insurance at "+date + " and it is valid to "+ service +
                    ". This cost you " + price +" "+ currencyMeasure+". ";
            if(!("".equals(mileage))){
                display+= " You do this at "+ mileage+" "+distanceMeasure+". ";
            }
            if(null!=note){
                display+= note;
            }
        } else {
            display ="You clean your car at "+date+" and this cost you "+ price + " "+ currencyMeasure +".";
            if(!("".equals(mileage))){
                display+= " You do this at "+ mileage+" "+distanceMeasure+". ";
            }
            if(null!=note){
                display+= note;
            }
        }

        txtView.setText(display);

        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDb.delete(id, enterId, tableName);
                        Toast.makeText(ViewOne.this, "Successful deleted", Toast.LENGTH_LONG).show();
                        history(ViewOne.this, tableName);
                    }
                }
        );

        btnEdit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String value =id  + "\n" + tableName;
                        Intent myIntent = new Intent(ViewOne.this, EditActivity.class);
                        myIntent.putExtra("key", value);
                        ViewOne.this.startActivity(myIntent);
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
                startActivity(new Intent(ViewOne.this, SettingsActivity.class));
                return true;
            case R.id.action_export:
                startActivity(new Intent(ViewOne.this, ExpImpActivity.class));
                return true;
            default:
                onBackPressed();
                return true;
        }
    }

}