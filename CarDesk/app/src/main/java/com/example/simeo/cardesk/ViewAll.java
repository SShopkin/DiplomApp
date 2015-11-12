package com.example.simeo.cardesk;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ViewAll extends ActivityHelper {
    DatabaseHelper myDb;
    Button btnExport;
    Button btnImport;
    String path = null;
    String table_name;

    @Override

        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_view_all);
            myDb=new DatabaseHelper(this);
            btnExport = (Button)findViewById(R.id.button_export);
            btnImport = (Button)findViewById(R.id.button_import);

            final ListView listview = (ListView) findViewById(R.id.listview);
            String[] values = new String[1000];

            Intent intent = getIntent();
            table_name  = intent.getStringExtra("key");

            btnExport.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                sendData(myDb, table_name);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

            btnImport.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Import();
                        }
                    });

            Cursor res = myDb.getAllData(table_name);
                if (res.getCount() == 0) {
                    Toast.makeText(ViewAll.this, "Error: No data", Toast.LENGTH_LONG).show();
                    return;
                }

            int b=0;
            while (res.moveToNext()) {
                StringBuffer buffer = new StringBuffer();
               // buffer.append("Id: " + res.getString(0) + "\n");
                if("fuel_table".equals(table_name)){
                    buffer.append("Quantity: " + res.getString(1) + "\n");
                } else if ("ins_table".equals(table_name)){
                    buffer.append("Validity: " + res.getString(1) + "\n");
                } else if ("oil_table".equals(table_name)){
                    buffer.append("Changing km: " + res.getString(1) + "\n");
                } else if ("tyre_table".equals(table_name)) {
                    buffer.append("Season: " + res.getString(1) + "\n");
                } else if ("other_table".equals(table_name)){
                    buffer.append("What: " + res.getString(1) + "\n");
                } else {
                    buffer.append("Type: " + res.getString(1) + "\n");
                }
                buffer.append("Price: " + res.getString(2) + "\n");
                buffer.append("Date: " + res.getString(3));
                values[b]=buffer.toString();
                b++;
            }

            final ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < res.getCount(); ++i) {
                list.add(values[i]);
            }

            Collections.reverse(list);
            final StableArrayAdapter adapter = new StableArrayAdapter(this,android.R.layout.simple_list_item_1, list);
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {
                    String value = (String) parent.getItemAtPosition(position);
                    value = value + "\n" + table_name;
                    Intent myIntent = new Intent(ViewAll.this, ViewOne.class);
                    myIntent.putExtra("key", value); //Optional parameters
                    ViewAll.this.startActivity(myIntent);
                }
            });
        }

        private static final int FILE_SELECT_CODE = 0;
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            //Toast.makeText(FuelActivity.this, "2.",Toast.LENGTH_SHORT).show();
            switch (requestCode) {
                case FILE_SELECT_CODE:
                    if (resultCode == RESULT_OK) {
                        Uri uri = data.getData();
                        try {
                            path = getPath(this, uri);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        openFile(path,myDb,table_name);

                    }
                    break;
            }
            super.onActivityResult(requestCode, resultCode, data);
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

