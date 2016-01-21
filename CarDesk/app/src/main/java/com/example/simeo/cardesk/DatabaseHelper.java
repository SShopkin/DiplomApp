package com.example.simeo.cardesk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="Database.db" ;
    public static final String TABlE_NAME[] = {"enter_table","service_table","fuel_table","ins_table","clean_table","note_table","settings_table"};


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABlE_NAME[0] + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, PRICE REAL, DATE TEXT, MILEAGE INTEGER)");
        db.execSQL("create table " + TABlE_NAME[1] + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, SERVICE TEXT, ENTER INTEGER, FOREIGN KEY (ENTER) REFERENCES enter_table(ID))");
        db.execSQL("create table " + TABlE_NAME[2] + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, SERVICE REAL, ENTER INTEGER, FOREIGN KEY (ENTER) REFERENCES enter_table(ID))");
        db.execSQL("create table " + TABlE_NAME[3] + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, SERVICE TEXT, ENTER INTEGER, FOREIGN KEY (ENTER) REFERENCES enter_table(ID))");
        db.execSQL("create table " + TABlE_NAME[4] + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, ENTER INTEGER, FOREIGN KEY (ENTER) REFERENCES enter_table(ID))");
        db.execSQL("create table " + TABlE_NAME[5] + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NOTE TEXT, ENTER INTEGER, FOREIGN KEY (ENTER) REFERENCES enter_table(ID))");
        db.execSQL("create table " + TABlE_NAME[6] + " (UNITFORLIQUID TEXT, UNITFORDISTANCE TEXT, CURRENCY TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for(int i=0;i<7;i++) {
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
        cv.put("UNITFORLIQUID", newLiquid);
        cv.put("UNITFORDISTANCE", newDistance);
        cv.put("CURRENCY", newCurrency);
        db.insert("settings_table", null, cv);
    }

    public String getSettings(){
        String liquid = null,distance=null,currency=null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from settings_table", null);
        while (res.moveToNext()) {
            liquid = res.getString(0);
            distance = res.getString(1);
            currency = res.getString(2);
        }
        return liquid+":"+distance+":"+currency;
    }

    public long insertData(String service,long enterId,String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("SERVICE",service);
        contentValues.put("ENTER", enterId);
        return db.insert(tableName,null,contentValues);
    }

    public long insertNote(String note,long enterId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("NOTE", note);
        cv.put("ENTER", enterId);
        return db.insert("note_table", null, cv);
    }

    public long insertClean(long enterId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ENTER", enterId);
        return db.insert("clean_table", null, contentValues);
    }

    public long insertEnter(String price,String date,String mileage){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("PRICE",price);
        cv.put("DATE",date);
        cv.put("MILEAGE", mileage);
        return db.insert("enter_table", null, cv);
    }

    public Cursor getAllData(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from " + tableName + " LEFT JOIN enter_table ON " + tableName + ".ENTER = enter_table.ID", null);
    }

    public Cursor getOneRow(String tableName,String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from " + tableName + " LEFT JOIN enter_table ON " + tableName + ".ENTER = enter_table.ID LEFT JOIN note_table ON " + tableName + ".ENTER = note_table.ENTER where " + tableName + ".ID=" + id, null);
    }

    public String currentMileage(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select MILEAGE FROM enter_table ORDER BY ID DESC limit 1", null);
        while (res.moveToNext()) {
            return res.getString(0);
        }
        return "";
    }

/*
    public String sumQuery(String searchDate, String currentDate, String tableName){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Sum(PRICE) FROM " + tableName + " WHERE date(DATE) BETWEEN date('" + searchDate + "') AND date('" + currentDate + "')", null);
        if(cursor.moveToFirst()){
            return Double.toString(cursor.getDouble(0));
        } return "0";

    }

    public double FuelBetweenDate(String searchDate, String currentDate){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor fuel = db.rawQuery("SELECT Sum(SERVICE) FROM fuel_table WHERE date(DATE) BETWEEN date('" + searchDate + "') AND date('" + currentDate + "')", null);
        Cursor firstFuel = db.rawQuery("SELECT SERVICE FROM fuel_table WHERE date(DATE) BETWEEN date('" + searchDate + "') AND date('" + currentDate + "') ORDER BY ID ASC limit 1", null);
        if(firstFuel.moveToFirst()&&fuel.moveToFirst()){
            return (Double.parseDouble(fuel.getString(0))) - (Double.parseDouble(firstFuel.getString(0)));
        }  return 0;
    }

    public String GetLastMileage(String searchDate, String currentDate){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor lastMileage = db.rawQuery("SELECT MILEAGE FROM fuel_table WHERE date(DATE) BETWEEN date('" + searchDate + "') AND date('" + currentDate + "') ORDER BY ID DESC limit 1", null);
        if(lastMileage.moveToFirst())        {
            return lastMileage.getString(0);
        } return "101";
    }

    public String GetFirstMileage(String searchDate, String currentDate){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor lastMileage = db.rawQuery("SELECT MILEAGE FROM fuel_table WHERE date(DATE) BETWEEN date('" + searchDate + "') AND date('" + currentDate + "') ORDER BY ID ASC limit 1", null);
        if(lastMileage.moveToFirst())        {
            return lastMileage.getString(0);
        } return "100";
    }

    public String getLastFullUpMileage(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor lastFullUp = db.rawQuery("SELECT MILEAGE FROM fuel_table WHERE NOTE!='' ORDER BY ID DESC limit 1", null);
        if(lastFullUp.moveToFirst())        {
            return lastFullUp.getString(0);
        } return "";
    }


    public String preciseFuel(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor fuel = db.rawQuery("SELECT Sum(SERVICE) FROM (SELECT ft.*, (SELECT count(*) FROM fuel_table ft2 WHERE ft2.note='Your tank was full up.' and ft2.id>=ft.id) AS numNotesAhead FROM fuel_table ft) AS ft WHERE numNotesAhead=1",null);
        if(fuel.moveToFirst())        {
            return fuel.getString(0);
        } return "";
    }

    public String getPreviousFullUpMileage(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor previosFullUp = db.rawQuery("SELECT MILEAGE FROM fuel_table WHERE NOTE!='' ORDER BY ID DESC limit 1,1", null);
        if(previosFullUp.moveToFirst())        {
            return previosFullUp.getString(0);
        } return "";
    }*/


    /*
    ********import******
    public long insertData(String price, String date,String validMil,String note,String TABlE_NAME) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("PRICE",price);
        contentValues.put("DATE",date);
        contentValues.put("VALIDMIL",validMil);
        contentValues.put("NOTE",note);
        return db.insert(TABlE_NAME, null, contentValues);
    }
    */



    public void editRecord(String firstRow,String secoundRow,String thirdRow,String fourthRow,String id,String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String where="ID = ?";
        if(("fuel_table".equals(tableName))||("service_table".equals(tableName))){
            cv.put("SERVICE",firstRow);
            cv.put("MILEAGE",fourthRow);
            cv.put("PRICE",secoundRow);
            cv.put("DATE",thirdRow);
        }else{
            cv.put("PRICE",secoundRow);
            cv.put("DATE",thirdRow);
            cv.put("VALIDMIL",firstRow);
            cv.put("NOTE", fourthRow);
        }
        db.update(tableName,cv, where, new String[]{id});
    }
}
