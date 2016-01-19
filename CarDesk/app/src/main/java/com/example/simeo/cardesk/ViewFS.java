package com.example.simeo.cardesk;

import android.content.Intent;
import android.database.Cursor;
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
        final String table_name=line[0];
        final String id=line[1];
        double numQuantity;
        double numPrice;
        double numPricePerQuantity;
        String service="1",price="1",date="",mileage="",note="",noteID="";
        final String liquidMeasure=myDb.getSettings().split(":")[0];
        final String distanceMeasure=myDb.getSettings().split(":")[1];
        final String currencyMeasure=myDb.getSettings().split(":")[2];
        String display="";

        Cursor res = myDb.getOneRow(table_name, id);
        while (res.moveToNext()) {
            service=res.getString(1);
            noteID=res.getString(3);
            price=res.getString(4);
            date=res.getString(5);
            mileage=res.getString(6);
        }
        note = myDb.getNote(noteID);
        if ("fuel_table".equals(table_name)) {
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
            if(!("".equals(note))){
                display+= note;
            }
        } /*else if("service_table".equals(table_name)){
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
        }*/

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
                        //String value = service + "\n" + price + "\n" + date + "\n" + mileage + "\n" + id  + "\n" + table_name;
                        Intent myIntent = new Intent(ViewFS.this, EditActivity.class);
                        //myIntent.putExtra("key", value);
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
