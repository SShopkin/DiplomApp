package com.example.simeo.cardesk;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import mehdi.sakout.fancybuttons.FancyButton;

public class EditActivity extends ActivityHelper implements DatePickerDialog.OnDateSetListener{
    DatabaseHelper myDb;
    EditText edit,editPrice,editDate,editMileage,editNote;
    String price="",date="",mileage="",note="",service="",enterId="";
    FancyButton btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        myDb=new DatabaseHelper(this);

        edit = (EditText)findViewById(R.id.edit1);
        editPrice = (EditText)findViewById(R.id.price);
        editDate = (EditText)findViewById(R.id.date);
        editMileage = (EditText)findViewById(R.id.mileage);
        editNote = (EditText)findViewById(R.id.note);
        btnSave = (FancyButton)findViewById(R.id.button_save);

        Intent intent = getIntent();
        String value = intent.getStringExtra("key");
        String line[]= value.split("\n");
        final String tableName=line[1];
        final String id=line[0];

        Cursor res = myDb.getOneRow(tableName, id);
        while (res.moveToNext()) {
            if("clean_table".equals(tableName)) {
                enterId = res.getString(1);
                price = res.getString(3);
                date = dateToShow(res.getString(4));
                mileage = res.getString(5);
                note = res.getString(7);
            } else {
                service = res.getString(1);
                enterId = res.getString(3);
                price = res.getString(4);
                date = dateToShow(res.getString(5));
                mileage = res.getString(6);
                note = res.getString(8);
            }
        }

        toolBar("Edit");
        adGenerator();

        if(!("clean_table".equals(tableName))){
            edit.setText(service);
        }
        editPrice.setText(price);
        editDate.setText(date);
        editMileage.setText(mileage);
        editNote.setText(note);

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateButton(EditActivity.this);
            }

        });

        if("clean_table".equals(tableName)){
            edit.setVisibility(View.INVISIBLE);
        }

        btnSave.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDb.editRecord(id,edit.getText().toString(),enterId,tableName);
                        myDb.editEnter(enterId,editPrice.getText().toString(),dateForBase(editDate.getText().toString()),editMileage.getText().toString());
                        if((note==null)&&(!("".equals(editNote.getText().toString())))){
                            myDb.insertNote(editNote.getText().toString(),Long.parseLong(enterId));
                        } else if (!("".equals(note))){
                            myDb.editNote(enterId,editNote.getText().toString());
                        }
                        history(EditActivity.this, tableName);
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
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String day,month;
        if(((dayOfMonth)>=1)&&((dayOfMonth)<=9)){
            day = "0"+dayOfMonth;
        } else {
            day=dayOfMonth+"";
        }
        if(((monthOfYear)>=0)&&((monthOfYear)<=8)){
            month = "0"+(++monthOfYear);
        } else {
            month=(++monthOfYear)+"";
        }
        String dateEdit = day + "." + month + "." + year;
        editDate.setText(dateEdit);
    }
}