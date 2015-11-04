package com.example.simeo.cardesk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="Fuel.db" ;
    public static final String TABlE_NAME="fuel_table";
    public static final String COL_1="ID";
    public static final String COL_2="QUANTITY";
    public static final String COL_3="PRICE";
    public static final String COL_4="DATE";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABlE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, QUANTITY TEXT, PRICE TEXT, DATE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABlE_NAME);
        onCreate(db);
    }

    public void delete(String date,String price,String quantity)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            db.delete(TABlE_NAME, "DATE = ? and PRICE=? and QUANTITY=?", new String[]{date,price,quantity});
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

    public boolean insertData(String quantity, String price,String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,quantity);
        contentValues.put(COL_3,price);
        contentValues.put(COL_4,date);
        long result = db.insert(TABlE_NAME,null,contentValues);
        if (result ==-1){
            return false;
        } else return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+TABlE_NAME,null);
        return res;
    }
}
