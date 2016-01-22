package com.example.simeo.cardesk;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ViewAll extends ActivityHelper {
    DatabaseHelper myDb;
    String tableName;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);
        myDb=new DatabaseHelper(this);

        final ListView listview = (ListView) findViewById(R.id.listview);
        String[] values = new String[100000];
        final List<String> idArray = new ArrayList<>();
        Intent intent = getIntent();
        tableName  = intent.getStringExtra("key");

        AdGenerator();
        ToolBar("History");

        Cursor res = myDb.getAllData(tableName);
        if (res.getCount() == 0) {
            Toast.makeText(ViewAll.this, "Error: No data", Toast.LENGTH_LONG).show();
            return;
        }

        int b=0;
        while (res.moveToNext()) {
            StringBuffer buffer = new StringBuffer();
            idArray.add(res.getString(0));
            if("fuel_table".equals(tableName)){
                buffer.append("Quantity: " + res.getString(1) + "\n");
            } else if ("ins_table".equals(tableName)){
                buffer.append("Validity: " + res.getString(1) + "\n");
            } else if ("service_table".equals(tableName)){
                buffer.append("What: " + res.getString(1) + "\n");
            } else {
                buffer.append("Date: " + dateToShow(res.getString(4)) + "\n");
                buffer.append("Price: " + res.getString(3));
            }
            if(!("clean_table".equals(tableName))){
                buffer.append("Price: " + res.getString(4) + "\n");
                buffer.append("Date: " + dateToShow(res.getString(5)));
            }
            values[b]=buffer.toString();
            b++;
        }

        final ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < res.getCount(); ++i) {
            list.add(values[i]);
        }


        Collections.sort(idArray);
        Collections.reverse(idArray);
        Collections.reverse(list);
        final StableArrayAdapter adapter = new StableArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                String value="";
                if(("fuel_table".equals(tableName))||("service_table".equals(tableName))) {
                    value = tableName + "\n" + idArray.get(position);
                } else {
                    value = tableName + "\n" + idArray.get(position);
                }
                Intent myIntent = new Intent(ViewAll.this, ViewFS.class);
                myIntent.putExtra("key", value);
                ViewAll.this.startActivity(myIntent);
            }
        });
    }


    private class StableArrayAdapter extends ArrayAdapter<String> {
        HashMap<String, Integer> mIdMap = new HashMap<>();
        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }
        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }
        @Override
        public boolean hasStableIds() {
            return true;
        }
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
                startActivity(new Intent(ViewAll.this, SettingsActivity.class));
                return true;
            case R.id.action_export:
                startActivity(new Intent(ViewAll.this, ExpImpActivity.class));
                return true;
            default:
                Intent myIntent;
                if ("fuel_table".equals(tableName)) {
                    myIntent = new Intent(ViewAll.this, FuelActivity.class);
                } else if ("service_table".equals(tableName)) {
                    myIntent = new Intent(ViewAll.this, ServiceActivity.class);
                } else if ("ins_table".equals(tableName)) {
                    myIntent = new Intent(ViewAll.this, InsActivity.class);
                } else {
                    myIntent = new Intent(ViewAll.this, WashActivity.class);
                }
                ViewAll.this.startActivity(myIntent);
                return true;
        }
    }
}

