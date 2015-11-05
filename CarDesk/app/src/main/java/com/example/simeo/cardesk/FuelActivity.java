package com.example.simeo.cardesk;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URISyntaxException;

import static android.os.Environment.getExternalStorageState;


public class FuelActivity extends ActionBarActivity {
    DatabaseHelper myDb;
    EditText editQuantity,editPrice,editDate;
    Button btnAddData;
    Button btnExport;
    Button btnImport;
    Button btnHistory;
    String path = null;

    private static final int CHOOSE_FILE_REQUESTCODE = 1;
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
        btnImport = (Button)findViewById(R.id.button_import);
        btnHistory = (Button)findViewById(R.id.button_history);

        AddData();
        SendData();
        btnHistory.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        History();
                    }
                });

        btnImport.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Import();
                    }
                });





    }

    public void Import(){
        showFileChooser();
    }
    private static final int FILE_SELECT_CODE = 0;

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                   // Log.d(TAG "File Uri: " + uri.toString());
                    // Get the path

                    try {
                        path = getPath(this, uri);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    openFile(path);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    public void openFile(String path) {
        File file = new File(path);
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }

        int number = 34;
        char c = (char)number;
        String a = String.valueOf(c);
        String[] textByLine = text.toString().split(a) ;
        for(int i=5;i<textByLine.length;i+=0){

            myDb.insertData(textByLine[i+4],textByLine[i+12],textByLine[i]);
            i+=16;
        }

        History();


    }



    public void History(){
        Intent intent = new Intent(FuelActivity.this, ViewAll.class);
        startActivity(intent);
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
            //for (int i = 0; i < res.getCount(); i++) {
                obj = new JSONObject();
                try {
                    obj.put("id", res.getString(0));
                    obj.put("activ", res.getString(1));
                    obj.put("price", res.getString(2));
                    obj.put("date", res.getString(3));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                jsonArray.put(obj);

            //}
        }
        JSONObject finalobject = new JSONObject();
        finalobject.put("car", jsonArray);
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
