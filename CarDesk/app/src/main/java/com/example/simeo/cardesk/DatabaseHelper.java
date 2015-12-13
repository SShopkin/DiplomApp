package com.example.simeo.cardesk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="Database.db" ;
    public static final String COL_1="ID";
    public static final String COL_2="QUANTITY";
    public static final String COL_3="PRICE";
    public static final String COL_4="DATE";
    public static final String COL_5="MILEAGE";
    public static final String TABlE_NAME[] = {"service_table","fuel_table","ins_table","clean_table","statistic_table","settings_table"};


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABlE_NAME[0] + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, SERVICE TEXT, PRICE TEXT, DATE TEXT, MILEAGE TEXT, NOTE TEXT)");
        db.execSQL("create table " + TABlE_NAME[1] + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, SERVICE TEXT, PRICE TEXT, DATE TEXT, MILEAGE TEXT, NOTE TEXT)");
        db.execSQL("create table " + TABlE_NAME[2] + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, PRICE TEXT, DATE TEXT, VALIDMIL TEXT, NOTE TEXT)");
        db.execSQL("create table " + TABlE_NAME[3] + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, PRICE TEXT, DATE TEXT, VALIDMIL TEXT, NOTE TEXT)");
        db.execSQL("create table " + TABlE_NAME[4] + " (CURRENTMILEAGE TEXT, LASTFULLUPDATE TEXT, PREVIOUSFULLUPDATE TEXT)");
        db.execSQL("create table " + TABlE_NAME[5] + " (UNITFORLIQUID TEXT, UNITFORDISTANCE TEXT, CURRENCY TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for(int i=0;i<6;i++) {
        db.execSQL("DROP TABLE IF EXISTS" + TABlE_NAME[i]);
        }
        onCreate(db);
    }

    public void delete(String id,String TABlE_NAME)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            db.delete(TABlE_NAME, "ID = ?", new String[]{id});
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            db.close();
        }
    }

    public void updateSettings(String newLiquid,String newDistance,String newCurrency){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("UNITFORLIQUID",newLiquid);
        cv.put("UNITFORDISTANCE",newDistance);
        cv.put("CURRENCY",newCurrency);
        db.insert("settings_table", null, cv);
    }

    public long insertDataFS(String firstRow, String price,String date,String mileage,String note,String TABlE_NAME) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("SERVICE",firstRow);
        contentValues.put(COL_3,price);
        contentValues.put(COL_4,date);
        contentValues.put("MILEAGE",mileage);
        contentValues.put("NOTE",note);
        long result = db.insert(TABlE_NAME,null,contentValues);
        return result;
    }

    public long insertData(String quantity, String price,String date,String TABlE_NAME) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,quantity);
        contentValues.put(COL_3,price);
        contentValues.put(COL_4,date);
        long result = db.insert(TABlE_NAME,null,contentValues);
        return result;
    }

    public Cursor getAllData(String TABlE_NAME) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+TABlE_NAME,null);
    }

}
