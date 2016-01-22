package com.example.simeo.cardesk;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.lguipeng.library.animcheckbox.AnimCheckBox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URISyntaxException;

import mehdi.sakout.fancybuttons.FancyButton;

import static android.os.Environment.getExternalStorageState;

public class ExpImpActivity extends ActivityHelper {
    AnimCheckBox fuelCheck,serviceCheck,insuranceCheck,cleanCheck;
    FancyButton btnExport,btnImport;
    DatabaseHelper myDb;
    boolean checkedFuel=false,checkedService=false,checkedInsurance=false,checkedClean=false;
    JSONObject finalObject = new JSONObject();
    String path = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exp_imp);
        myDb=new DatabaseHelper(this);

        fuelCheck = (AnimCheckBox)findViewById(R.id.fuel);
        serviceCheck = (AnimCheckBox)findViewById(R.id.service);
        insuranceCheck = (AnimCheckBox)findViewById(R.id.ins);
        cleanCheck = (AnimCheckBox)findViewById(R.id.clean);
        btnExport = (FancyButton)findViewById(R.id.exportBtn);
        btnImport = (FancyButton)findViewById(R.id.importBtn);

        fuelCheck.setChecked(checkedFuel);
        serviceCheck.setChecked(checkedService);
        insuranceCheck.setChecked(checkedInsurance);
        cleanCheck.setChecked(checkedClean);

        fuelCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedFuel = !checkedFuel;
                fuelCheck.setChecked(checkedFuel);
            }
        });

        serviceCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedService = !checkedService;
                serviceCheck.setChecked(checkedService);
            }
        });

        insuranceCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedInsurance = !checkedInsurance;
                insuranceCheck.setChecked(checkedInsurance);
            }
        });

        cleanCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedClean = !checkedClean;
                cleanCheck.setChecked(checkedClean);
            }
        });

        ToolBar("Export and import");
        AdGenerator();

        btnExport.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if(checkedClean){
                            sendData(myDb, "clean_table");
                        }
                        if(checkedInsurance){
                            sendData(myDb, "ins_table");
                        }
                        if(checkedService){
                            sendData(myDb, "service_table");
                        }
                        if(checkedFuel){
                            sendData(myDb, "fuel_table");
                        }
                        if((checkedFuel)||(checkedService)||(checkedInsurance)||(checkedClean)){
                            sendData(myDb, "enter_table");
                            getExternalStorageState();
                            generateNoteOnSD("/storage/sdcard0/Notes/base.txt", finalObject.toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        btnImport.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    importBase();
                }
            });

    }

    private static final int FILE_SELECT_CODE = 0;
    public void importBase() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.",Toast.LENGTH_SHORT).show();
        }
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                Toast.makeText(context, "Error",Toast.LENGTH_SHORT).show();
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public void openFile(String path,DatabaseHelper myDb,String tableName) {
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
            //Toast.makeText(context, "3.",Toast.LENGTH_SHORT).show();
        }
        int number = 34;
        char c = (char)number;
        String a = String.valueOf(c);
        String[] textByLine = text.toString().split(a) ;
        for(int i=5;i<textByLine.length;i+=0){
            //function is commented
            //myDb.insertData(textByLine[i+4],textByLine[i+8],textByLine[i+12],textByLine[i+16],tableName);
            i +=20;
        }
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    try {
                        path = getPath(this, uri);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    //openFile(path,myDb,tableName);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void sendData(DatabaseHelper myDb,String tableName) throws JSONException {
        Cursor res;
        if("enter_table".equals(tableName)) {
            res = myDb.getAllEnter();
                finalObject.put("enter_table", textEnterTable(res));
        } else if ("clean_table".equals(tableName)){
            res = myDb.getAllClean();
            if (res.getCount()!=0) {
                finalObject.put("clean_table", textCleanTable(res));
            }
        } else {
                res = myDb.getAllTable(tableName);
            if (res.getCount()!=0) {
                finalObject.put(tableName, textTable(res));
            }
        }

    }
    //generate file
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void generateNoteOnSD(String FilePath, String sBody){
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
        sendFile();
    }
    //send file
    public void sendFile(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File("/storage/sdcard0/Notes/base.txt")));
        startActivity(intent);
    }

    public JSONArray textEnterTable(Cursor res){
        JSONObject row;
        JSONArray tableAsArray = new JSONArray();
        while (res.moveToNext()){
            row = new JSONObject();
            try {
                row.put("id", res.getString(0));
                row.put("price", res.getString(1));
                row.put("date", res.getString(2));
                row.put("mileage", res.getString(3));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            tableAsArray.put(row);
        }
        return tableAsArray;
    }

    public JSONArray textCleanTable(Cursor res){
        JSONObject row;
        JSONArray tableAsArray = new JSONArray();
        while (res.moveToNext()){
            row = new JSONObject();
            try {
                row.put("id", res.getString(0));
                row.put("enterId", res.getString(1));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            tableAsArray.put(row);
        }
        return tableAsArray;
    }

    public JSONArray textTable(Cursor res){
        JSONObject row;
        JSONArray tableAsArray = new JSONArray();
        while (res.moveToNext()){
            row = new JSONObject();
            try {
                row.put("id", res.getString(0));
                row.put("service", res.getString(1));
                row.put("enterId", res.getString(2));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            tableAsArray.put(row);
        }
        return tableAsArray;
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
                startActivity(new Intent(ExpImpActivity.this, SettingsActivity.class));
                return true;
            case R.id.action_export:
                startActivity(new Intent(ExpImpActivity.this, ExpImpActivity.class));
                return true;
            default:
                Intent myIntent = new Intent(ExpImpActivity.this, MainActivity.class);
                ExpImpActivity.this.startActivity(myIntent);
                return true;
        }
    }
}


