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
    public static final String TABlE_NAME[] = {"fuel_table","lamp_table","ins_table","oil_table","other_table","tyre_table"};


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for(int i=0;i<6;i++) {
            db.execSQL("create table " + TABlE_NAME[i] + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, QUANTITY TEXT, PRICE TEXT, DATE TEXT)");
        }
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

    /*public void update(String newDate,String newPrice,String newQuantity,String date,String price,String quantity,String TABlE_NAME){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_2,newQuantity);
        cv.put(COL_3,newPrice);
        cv.put(COL_4,newDate);
        db.update(TABlE_NAME, cv,"DATE = ? and PRICE=? and QUANTITY=?", new String[]{date, price, quantity});
    }*/

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
