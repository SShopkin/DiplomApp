package com.monimont.cardesk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import mehdi.sakout.fancybuttons.FancyButton;


public class ActivityHelper extends AppCompatActivity {
    private static final int FILE_SELECT_CODE = 0;

    public void toolBar(String title){
        Toolbar myToolbar = (Toolbar)findViewById(R.id.toolbar);
        myToolbar.setTitle(title);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

     public void adGenerator(){
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void setDateButton(Context context){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                (DatePickerDialog.OnDateSetListener) context,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setAccentColor(Color.parseColor("#FFCC80"));
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    public void getCurrentDate(FancyButton dateButton){
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();
        dateButton.setText(dateFormat.format(date));
        dateButton.setTextSize(25);
    }

    public String dateForBase(String date){
        String year=date.split("\\.")[2];
        String month=date.split("\\.")[1];
        String day=date.split("\\.")[0];
        return year+"-"+month+"-"+day;
    }

    public String dateToShow(String date){
        String year=date.split("-")[0];
        String month=date.split("-")[1];
        String day=date.split("-")[2];
        return day+"."+month+"."+year;
    }

    public void history(Context context,String table_name){
        Intent myIntent = new Intent(context, ViewAll.class);
        myIntent.putExtra("key", table_name);
        context.startActivity(myIntent);
    }

    public boolean isMileageOk(Context activity,String current,String newOne){
        if("".equals(newOne)){
            Toast.makeText(activity, "Enter mileage", Toast.LENGTH_LONG).show();
            return false;
        }
        if(Integer.parseInt(current)>(Integer.parseInt(newOne))){
            Toast.makeText(activity, "Your mileage is lower than mileage in last record", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public long addDataToTheBase (Context activity,DatabaseHelper myDb,String service,String price,String date,String mileage,String note,String tableName){
        long isInserted = -1,enterId;
        if(!("".equals(price)||("").equals(service))) {
            enterId = myDb.insertEnter(price, date, mileage);
            isInserted = myDb.insertData(service, enterId, tableName);
            if(!("".equals(note))){
                myDb.insertNote(note,enterId);
            }
            if ((isInserted==-1)||(enterId==-1))
                Toast.makeText(activity, "Data NOT inserted", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(activity, "Data inserted", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(activity, "Enter price or/and quantity", Toast.LENGTH_LONG).show();
        }
        return isInserted;
    }

    public long addCleaning(Context activity,DatabaseHelper myDb,String price,String date,String mileage,String note){
        long isInserted = -1,enterId;
        if(!("".equals(price))) {
            enterId = myDb.insertEnter(price, date, mileage);
            isInserted = myDb.insertClean(enterId);
            if(!("".equals(note))){
                myDb.insertNote(note,enterId);
            }
            if (isInserted==-1)
                Toast.makeText(activity, "Data NOT inserted", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(activity, "Data inserted", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(activity, "Enter price or/and quantity", Toast.LENGTH_LONG).show();
        }
        return isInserted;
    }

    public void setStatusBarColor(View statusBar,int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //status bar height
            int actionBarHeight = getActionBarHeight();
            int statusBarHeight = getStatusBarHeight();
            //action bar height
            statusBar.getLayoutParams().height = actionBarHeight + statusBarHeight;
            statusBar.setBackgroundColor(color);
        }
    }

    public int getActionBarHeight() {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
