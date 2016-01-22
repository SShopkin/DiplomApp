package com.example.simeo.cardesk;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import mehdi.sakout.fancybuttons.FancyButton;

public class EditActivity extends ActivityHelper {
    DatabaseHelper myDb;
    EditText edit,editPrice,editDate,change;
    FancyButton btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        myDb=new DatabaseHelper(this);

        edit = (EditText)findViewById(R.id.edit1);
        editPrice = (EditText)findViewById(R.id.price);
        editDate = (EditText)findViewById(R.id.date);
        change = (EditText)findViewById(R.id.edit2);
        btnSave = (FancyButton)findViewById(R.id.button_save);

        Intent intent = getIntent();
        String value = intent.getStringExtra("key");
        String line[]= value.split("\n");
        final String firstLine=line[0];
        final String price=line[1];
        final String date=line[2];
        final String thirdLine=line[3];
        final String id=line[4];
        final String table_name=line[5];

        ToolBar("Edit");
        AdGenerator();

        if(("fuel_table".equals(table_name))||("service_table".equals(table_name))){
            edit.setText(firstLine);
            editPrice.setText(price);
            editDate.setText(date);
        } else {
            edit.setText(date);
            editPrice.setText(firstLine);
            editDate.setText(price);
        }
        change.setText(thirdLine);
        //Toast.makeText(EditActivity.this, table_name, Toast.LENGTH_LONG).show();

        btnSave.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDb.editRecord(edit.getText().toString(),editPrice.getText().toString(),editDate.getText().toString(),change.getText().toString(),id,table_name);
                        History(EditActivity.this, table_name);
                    }
                });

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
                startActivity(new Intent(EditActivity.this, SettingsActivity.class));
                return true;
            case R.id.action_export:
                startActivity(new Intent(EditActivity.this, ExpImpActivity.class));
                return true;
            default:
                onBackPressed();
                return true;
        }
    }
}