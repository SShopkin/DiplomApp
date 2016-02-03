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
        db.execSQL("create table " + TABlE_NAME[1] + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, SERVICE TEXT, ENTERID INTEGER, FOREIGN KEY (ENTERID) REFERENCES enter_table(ID))");
        db.execSQL("create table " + TABlE_NAME[2] + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, SERVICE REAL, ENTERID INTEGER, FOREIGN KEY (ENTERID) REFERENCES enter_table(ID))");
        db.execSQL("create table " + TABlE_NAME[3] + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, SERVICE TEXT, ENTERID INTEGER, FOREIGN KEY (ENTERID) REFERENCES enter_table(ID))");
        db.execSQL("create table " + TABlE_NAME[4] + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, ENTERID INTEGER, FOREIGN KEY (ENTERID) REFERENCES enter_table(ID))");
        db.execSQL("create table " + TABlE_NAME[5] + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NOTE TEXT, ENTERID INTEGER, FOREIGN KEY (ENTERID) REFERENCES enter_table(ID))");
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
        contentValues.put("ENTERID", enterId);
        return db.insert(tableName,null,contentValues);
    }

    public long insertNote(String note,long enterId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("NOTE", note);
        cv.put("ENTERID", enterId);
        return db.insert("note_table", null, cv);
    }

    public long insertClean(long enterId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ENTERID", enterId);
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
        return db.rawQuery("select * from " + tableName + " LEFT JOIN enter_table ON " + tableName + ".ENTERID = enter_table.ID", null);
    }

    public Cursor getOneRow(String tableName,String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from " + tableName + " LEFT JOIN enter_table ON " + tableName + ".ENTERID = enter_table.ID LEFT JOIN note_table ON " + tableName + ".ENTERID = note_table.ENTERID where " + tableName + ".ID=" + id, null);
    }

    public String currentMileage(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select MILEAGE FROM enter_table ORDER BY ID DESC limit 1", null);
        while (res.moveToNext()) {
            return res.getString(0);
        }
        return "";
    }

    public double allSum(String searchDate, String currentDate){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Sum(PRICE) FROM enter_table WHERE date(DATE) BETWEEN date('" + searchDate + "') AND date('" + currentDate + "')", null);
        if(cursor.moveToFirst()){
            return cursor.getDouble(0);
        } return 0;
    }

    public String totalSum(String searchDate, String currentDate, String tableName){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Sum(PRICE) FROM " + tableName + " LEFT JOIN enter_table ON " + tableName + ".ENTERID = enter_table.ID LEFT JOIN note_table ON " + tableName + ".ENTERID = note_table.ENTERID WHERE date(DATE) BETWEEN date('" + searchDate + "') AND date('" + currentDate + "')", null);
        if(cursor.moveToFirst()){
            return Double.toString(cursor.getDouble(0));
        } return "0";

    }

    public int fullUpCount(String searchDate, String currentDate){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT count(*) FROM fuel_table LEFT JOIN enter_table ON fuel_table.ENTERID = enter_table.ID LEFT JOIN note_table ON fuel_table.ENTERID = note_table.ENTERID WHERE date(enter_table.DATE) BETWEEN date('" + searchDate + "') AND date('" + currentDate + "') AND note_table.NOTE='Your tank was full up.'", null);
        if(cursor.moveToFirst()){
            return cursor.getInt(0);
        } return 0;
    }


    public int getLastFullUpMileage(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor lastFullUp = db.rawQuery("SELECT enter_table.mileage FROM fuel_table LEFT JOIN enter_table ON fuel_table.ENTERID = enter_table.ID LEFT JOIN note_table ON fuel_table.ENTERID = note_table.ENTERID WHERE note_table.note !='' ORDER BY date(DATE) DESC, fuel_table.ID DESC limit 1", null);
        if(lastFullUp.moveToFirst())        {
            return lastFullUp.getInt(0);
        }
        return 0;
    }

    public int getFirstFullUpMileage(String searchDate, String currentDate){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor previousFullUp = db.rawQuery("SELECT enter_table.mileage FROM fuel_table LEFT JOIN enter_table ON fuel_table.ENTERID = enter_table.ID LEFT JOIN note_table ON fuel_table.ENTERID = note_table.ENTERID WHERE date(DATE) BETWEEN date('" + searchDate + "') AND date('" + currentDate + "') AND note_table.note !='' ORDER BY date(DATE) ASC limit 1", null);
        if(previousFullUp.moveToFirst())        {
            return previousFullUp.getInt(0);
        } return 0;
    }

    public Double preciseFuel(int fullUpCount){
        Double sum=0.0;
        SQLiteDatabase db = this.getReadableDatabase();
        for(int i=1;i<fullUpCount;i++) {
            Cursor fuel = db.rawQuery("SELECT SUM(fq.service) AS total FROM (SELECT fq2.*,(SELECT COUNT(*) FROM (SELECT f.id, f.service, f.enterid, n.note FROM fuel_table f LEFT JOIN note_table n ON f.enterid = n.enterid) fq1 WHERE fq1.note = 'Your tank was full up.' AND fq1.id >= fq2.id) AS numNotesAhead FROM (SELECT f.id, f.service, f.enterid, n.note FROM fuel_table f LEFT JOIN note_table n ON f.enterid = n.enterid) fq2) fq WHERE numNotesAhead = "+i, null);
            if (fuel.moveToFirst()) {
                sum += fuel.getDouble(0);
            }
        }
        return sum;
    }

//************************ editing ********************************
    public void editRecord(String id, String service,String enterID,String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String where="ID = ?";
        if(!("clean_table".equals(tableName))){
            cv.put("SERVICE",service);
            cv.put("ENTERID",enterID);
        }else{
            cv.put("ENTERID",enterID);
        }
        db.update(tableName,cv, where, new String[]{id});
    }

    public void editEnter(String id, String price,String date,String mileage){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String where="ID = ?";
        cv.put("PRICE",price);
        cv.put("DATE",date);
        cv.put("MILEAGE",mileage);
        db.update("enter_table",cv, where, new String[]{id});
    }

    public void editNote(String enterId, String note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String where="ENTERID = ?";
        cv.put("NOTE",note);
        db.update("note_table",cv, where, new String[]{enterId});
    }
// *********************** export ***********************
    public Cursor getAllEnter(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from enter_table", null);
    }

    public Cursor getAllClean(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from clean_table", null);
    }

    public Cursor getAllNotes(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from note_table", null);
    }

    public Cursor getAllTables(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+tableName, null);
    }
// ****************************** import ***********************
    public void importEnter(String id,String price,String date,String mileage){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ID",id);
        cv.put("PRICE",price);
        cv.put("DATE",date);
        cv.put("MILEAGE", mileage);
        db.insert("enter_table", null, cv);
    }
}
