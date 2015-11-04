package com.example.simeo.cardesk;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import static android.os.Environment.getExternalStorageState;


public class FuelActivity extends ActionBarActivity {
    DatabaseHelper myDb;
    EditText editQuantity,editPrice,editDate;
    Button btnAddData;
    Button btnExport;
    Button btnHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel);
        myDb=new DatabaseHelper(this);

        editQuantity = (EditText)findViewById(R.id.editText_quantity);
        editPrice = (EditText)findViewById(R.id.editText_price);
        editDate = (EditText)findViewById(R.id.editText_date);
        btnAddData = (Button)findViewById(R.id.button_add);
        btnExport = (Button)findViewById(R.id.button_export);
        btnHistory = (Button)findViewById(R.id.button_history);

        AddData();
        SendData();
        History();

    }

    public void History(){
        btnHistory.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FuelActivity.this, ViewAll.class);
                        startActivity(intent);
                    }
                }
        );
    }

    public void AddData (){
        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if((editQuantity.getText().toString().equals(""))&&(editDate.getText().toString().equals(""))
                                &&(editPrice.getText().toString().equals(""))) {
                            Toast.makeText(FuelActivity.this, "Fill correct your information", Toast.LENGTH_LONG).show();
                        } else if(editPrice.getText().toString().contains(" ")){
                            boolean isInserted = myDb.insertData(editQuantity.getText().toString(), editPrice.getText().toString(),
                                    editDate.getText().toString());
                            if (isInserted)
                                Toast.makeText(FuelActivity.this, "Data inserted", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(FuelActivity.this, "Data NOT inserted", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(FuelActivity.this, "Fill correct your information: Check price " +
                                    "(it must contain space between a number and currency unit )", Toast.LENGTH_LONG).show();
                        }

                    }
                }
        );
    }

    public void SendData(){
        btnExport.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            generateJsonObject();
                            sendFile();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
        );
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void generateJsonObject() throws JSONException {
        JSONObject obj = null;
        JSONArray jsonArray = new JSONArray();
        Cursor res = myDb.getAllData();
        if (res.getCount()==0){
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }
        while (res.moveToNext()){
            for (int i = 0; i < res.getCount(); i++) {
                obj = new JSONObject();
                try {
                    obj.put("id", res.getString(0));
                    obj.put("name", res.getString(1));
                    obj.put("surname", res.getString(2));
                    obj.put("marks", res.getString(3));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                jsonArray.put(obj);

            }
        }
        JSONObject finalobject = new JSONObject();
        finalobject.put("student", jsonArray);
        //write to file
        getExternalStorageState();
        generateNoteOnSD("/storage/sdcard0/Notes/base.txt", finalobject.toString());


    }
    //generate file
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void generateNoteOnSD(String FilePath, String sBody){

        File file = new File(FilePath);
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(FilePath), "utf-8"))) {
            writer.write(sBody);
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "FileNotFoundException", Toast.LENGTH_SHORT).show();
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(this, "UnsupportedEncodingException", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "IOException", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendFile(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File("/storage/sdcard0/Notes/base.txt")));
        startActivity(intent);
    }

}
