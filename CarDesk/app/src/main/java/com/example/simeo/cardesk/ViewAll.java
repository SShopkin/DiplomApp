package com.example.simeo.cardesk;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewAll extends AppCompatActivity {
    DatabaseHelper myDb;
    @Override

        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_view_all);
            myDb=new DatabaseHelper(this);

            final ListView listview = (ListView) findViewById(R.id.listview);
            String[] values = new String[1000];

        Cursor res = myDb.getAllData();
        if (res.getCount() == 0) {
            Toast.makeText(ViewAll.this, "Error: No data", Toast.LENGTH_LONG).show();
            return;
        }

        int b=0;
        while (res.moveToNext()) {
            StringBuffer buffer = new StringBuffer();
           // buffer.append("Id: " + res.getString(0) + "\n");
            buffer.append("Quantity: " + res.getString(1) + "\n");
            buffer.append("Price: " + res.getString(2) + "\n");
            buffer.append("Date: " + res.getString(3));
            values[b]=buffer.toString();
            b++;
        }

        final ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < res.getCount(); ++i) {
                list.add(values[i]);
            }
            final StableArrayAdapter adapter = new StableArrayAdapter(this,
                    android.R.layout.simple_list_item_1, list);
            listview.setAdapter(adapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {
                    final String value = (String) parent.getItemAtPosition(position);
                    //Toast.makeText(ViewAll.this, item, Toast.LENGTH_LONG).show();
                    Intent myIntent = new Intent(ViewAll.this, ViewOne.class);
                    myIntent.putExtra("key", value); //Optional parameters
                    ViewAll.this.startActivity(myIntent);
                }

            });
        }

        private class StableArrayAdapter extends ArrayAdapter<String> {

            HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

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
    }

