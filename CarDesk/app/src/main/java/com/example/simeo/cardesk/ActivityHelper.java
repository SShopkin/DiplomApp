package com.example.simeo.cardesk;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
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
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URISyntaxException;
import static android.os.Environment.getExternalStorageState;


public class ActivityHelper extends AppCompatActivity {
    private static final int FILE_SELECT_CODE = 0;


    public void History(Context a){
        Intent intent = new Intent(a, ViewAll.class);
        startActivity(intent);
    }

    public void AddData (Context activity,DatabaseHelper myDb,String firstRow,String secondRow,String thirdRow){
        if(("".equals(firstRow))||("".equals(secondRow))||("".equals(thirdRow))) {
            Toast.makeText(activity, "Fill correct your information", Toast.LENGTH_LONG).show();
        } else if(secondRow.contains(" ")){
            boolean isInserted = myDb.insertData(firstRow, secondRow, thirdRow);
            if (isInserted)
                Toast.makeText(activity, "Data inserted", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(activity, "Data NOT inserted", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(activity, "Fill correct your information: Check price " +
                    "(it must contain space between a number and currency unit )", Toast.LENGTH_LONG).show();
        }
        //VIEW ONE ACTIVITY
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void sendData(DatabaseHelper myDb) throws JSONException {
        JSONObject obj;
        JSONArray jsonArray = new JSONArray();
        Cursor res = myDb.getAllData();
        if (res.getCount()==0){
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }
        while (res.moveToNext()){
            obj = new JSONObject();
            try {
                obj.put("id", res.getString(0));
                obj.put("row1", res.getString(1));
                obj.put("row2", res.getString(2));
                obj.put("row3", res.getString(3));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(obj);
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

    public void sendFile(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File("/storage/sdcard0/Notes/base.txt")));
        startActivity(intent);
    }

    public void Import() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
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

    public void openFile(String path,DatabaseHelper myDb,Context context) {
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
            Toast.makeText(context, "3.",Toast.LENGTH_SHORT).show();
        }

        int number = 34;
        char c = (char)number;
        String a = String.valueOf(c);
        String[] textByLine = text.toString().split(a) ;
        for(int i=5;i<textByLine.length;i+=0){

            myDb.insertData(textByLine[i+4],textByLine[i+8],textByLine[i+12]);
            i +=16;
        }
        History(context);
    }


}