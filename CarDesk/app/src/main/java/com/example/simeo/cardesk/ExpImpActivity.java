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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
                            generateFile("/storage/sdcard0/Notes/base.txt", finalObject.toString());
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

    //***************************import***************************
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
                    try {
                        openFile(path,myDb);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void openFile(String path,DatabaseHelper myDb) throws IOException {
        File file = new File(path);
        JSONArray oneTable;
        String str;
        JSONObject allTables=null;
        byte[] data;

        FileInputStream fis = new FileInputStream(file);
        data=new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        str = new String(data, "UTF-8");

        try {
            allTables = new JSONObject(str);
            oneTable = allTables.getJSONArray("enter_table");
            importEnter(oneTable, myDb);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            oneTable = allTables.getJSONArray("note_table");
            importNote(oneTable, myDb);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            oneTable = allTables.getJSONArray("fuel_table");
            importTable(oneTable, myDb, "fuel_table");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            oneTable = allTables.getJSONArray("service_table");
            importTable(oneTable, myDb,"service_table");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            oneTable = allTables.getJSONArray("ins_table");
            importTable(oneTable, myDb,"ins_table");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            oneTable = allTables.getJSONArray("clean_table");
            importClean(oneTable,myDb);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void importEnter(JSONArray enterTable,DatabaseHelper myDb) throws JSONException {
        JSONObject row;
        for(int i=0;i<enterTable.length();i++){
            row = new JSONObject(enterTable.getString(i));
            myDb.importEnter(row.getString("id"), row.getString("price"), row.getString("date"), row.getString("mileage"));
        }
    }

    public void importClean(JSONArray cleanTable,DatabaseHelper myDb) throws JSONException {
        JSONObject row;
        for (int i = 0; i < cleanTable.length(); i++) {
            row = new JSONObject(cleanTable.getString(i));
            myDb.insertClean(Long.parseLong(row.getString("enterId")));
        }
    }

    public void importNote(JSONArray noteTable,DatabaseHelper myDb) throws JSONException {
        JSONObject row;
        for (int i = 0; i < noteTable.length(); i++) {
            row = new JSONObject(noteTable.getString(i));
            myDb.insertNote(row.getString("note"), Long.parseLong(row.getString("enterId")));
        }
    }

    public void importTable(JSONArray noteTable,DatabaseHelper myDb, String tableName) throws JSONException {
        JSONObject row;
        for (int i = 0; i < noteTable.length(); i++) {
            row = new JSONObject(noteTable.getString(i));
            myDb.insertData(row.getString("service"), Long.parseLong(row.getString("enterId")),tableName);
        }
    }

//*******************************export************************************
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void sendData(DatabaseHelper myDb,String tableName) throws JSONException {
        Cursor res;
        if("enter_table".equals(tableName)) {
            res = myDb.getAllEnter();
            finalObject.put("enter_table", textEnterTable(res));
            res = myDb.getAllNotes();
            finalObject.put("note_table", textNoteTable(res));
        } else if ("clean_table".equals(tableName)){
            res = myDb.getAllClean();
            if (res.getCount()!=0) {
                finalObject.put("clean_table", textCleanTable(res));
            }
        } else {
                res = myDb.getAllTables(tableName);
            if (res.getCount()!=0) {
                finalObject.put(tableName, textTable(res));
            }
        }
    }
    //generate file
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void generateFile(String FilePath, String sBody){
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

    public JSONArray textNoteTable(Cursor res){
        JSONObject row;
        JSONArray tableAsArray = new JSONArray();
        while (res.moveToNext()){
            row = new JSONObject();
            try {
                row.put("note", res.getString(1));
                row.put("enterId", res.getString(2));
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


