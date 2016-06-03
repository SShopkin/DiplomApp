package com.monimont.cardesk;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.ganfra.materialspinner.MaterialSpinner;
import mehdi.sakout.fancybuttons.FancyButton;


public class StatisticActivity extends ActivityHelper {
    DatabaseHelper myDb;
    TextView sumView,fuelView;
    FancyButton btnRefresh;
    MaterialSpinner dateSpinner,tableSpinner;
    String curDate,liquidMeasure,distanceMeasure,currencyMeasure;
    int curDay,curMonth,curYear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        myDb=new DatabaseHelper(this);
        liquidMeasure=myDb.getSettings().split(":")[0];
        distanceMeasure=myDb.getSettings().split(":")[1];
        currencyMeasure=myDb.getSettings().split(":")[2];

        btnRefresh = (FancyButton)findViewById(R.id.button_refresh);
        sumView= (TextView)findViewById(R.id.sumTxt);
        fuelView= (TextView)findViewById(R.id.fuelTxt);
        fuelView.setVisibility(View.INVISIBLE);

        String[] TABLE = {getString(R.string.statistic_all), getString(R.string.statistic_fuel), getString(R.string.statistic_service), getString(R.string.statistic_travelled_distance), getString(R.string.statistic_insurance), getString(R.string.statistic_clean)};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, TABLE);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tableSpinner = (MaterialSpinner)findViewById(R.id.tableSpinner);
        tableSpinner.setHint(R.string.statistic_hint_cat);
        tableSpinner.setAdapter(adapter);
        String[] DATES = {getString(R.string.statistic_today),getString(R.string.statistic_week),getString(R.string.statistic_month), getString(R.string.statistic_year), getString(R.string.statistic_beginning)};
        ArrayAdapter<String> dateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, DATES);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner = (MaterialSpinner)findViewById(R.id.timeSpinner);
        dateSpinner.setHint(R.string.statistic_hint_time);
        dateSpinner.setAdapter(dateAdapter);

        setStatusBarColor(findViewById(R.id.statusBarBackground), getResources().getColor(R.color.accentOrange));
        curDate=currentDate();
        adGenerator();
        toolBar(getString(R.string.statistic_toolbar));

        btnRefresh.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isOtionSelected()) {
                            if ("All".equals(choseTable())) {
                                fuelView.setVisibility(View.INVISIBLE);
                                sumView.setText(String.format("%s%s", textAllSum(myDb.allSum(chosePeriod(), curDate)), averageSum(myDb.allSum(chosePeriod(), curDate))));
                            } else if ("fuel_table".equals(choseTable())) {
                                fuelView.setVisibility(View.VISIBLE);
                                fuelView.setText(fuelConsumption());
                                sumView.setText(String.format("%s%s", textSum(), averageSum(Double.parseDouble(myDb.totalSum(chosePeriod(), curDate, choseTable())))));
                            } else if("distance".equals(choseTable())){
                                fuelView.setVisibility(View.INVISIBLE);
                                sumView.setText(distance());
                            } else{
                                fuelView.setVisibility(View.INVISIBLE);
                                sumView.setText(String.format("%s%s", textSum(), averageSum(Double.parseDouble(myDb.totalSum(chosePeriod(), curDate, choseTable())))));
                            }
                        }
                    }
                }
        );
    }

    public boolean isOtionSelected(){
        if((("").equals(chosePeriod()))||("").equals(choseTable())) {
            Toast.makeText(StatisticActivity.this, getString(R.string.statistic_toast), Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public String currentDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();
        return dateForBase(dateFormat.format(date));
    }

    public String distance(){
        int cur = Integer.parseInt(myDb.currentMileage());
        if("".equals(myDb.mileage(chosePeriod(),curDate))){
            return getString(R.string.statistic_not_enough_records);
        } else {
            int prev = Integer.parseInt(myDb.mileage(chosePeriod(),curDate));
            String travel = (cur - prev) + "";
            return dateSpinner.getSelectedItem().toString() +" "+ getString(R.string.statistic_you_travelled) +" "+ travel + " " + distanceMeasure+".";
        }
    }

    public String textSum(){
        return dateSpinner.getSelectedItem().toString()+" "+getString(R.string.statistic_you_paid)+" " +
                myDb.totalSum(chosePeriod(),curDate,choseTable())+" "+currencyMeasure+". ";
    }

    public String textAllSum(double total){
        return dateSpinner.getSelectedItem().toString()+" "+getString(R.string.statistic_you_paid) +" "+ total +" "+currencyMeasure+". ";
    }

    public String averageSum(double sum){
        if(lastWeekDate().equals(chosePeriod())){
            sum = sum/7;
            sum = Math.floor(sum * 100) / 100;
            return getString(R.string.statistic_avr_day)+" "+sum+" "+currencyMeasure+".";
        } else if(lastMonthDate().equals(chosePeriod())){
            sum = sum/30;
            sum = Math.floor(sum * 100) / 100;
            return getString(R.string.statistic_avr_day)+" "+sum+" "+currencyMeasure+".";
        } else if(lastYearDate().equals(chosePeriod())){
            sum = sum/12;
            sum = Math.floor(sum * 100) / 100;
            return getString(R.string.statistic_avr_month)+" "+sum+" "+currencyMeasure+".";
        }
        return "";
    }

    public String fuelConsumption(){
        if(myDb.fullUpCount(chosePeriod(), curDate)>1){
            double consumption = myDb.preciseFuel(myDb.fullUpCount(chosePeriod(), curDate)) / fuelDistance() * 100;
            consumption = Math.floor(consumption * 100) / 100;
            return getString(R.string.statistic_fuel_cons)+" "+consumption+" "+liquidMeasure+" "+getString(R.string.statistic_per_cat)+" "+distanceMeasure+".";
        }
        return "";
    }

    public int fuelDistance(){
        int first = myDb.getFirstFullUpMileage(chosePeriod(),curDate);
        int last = myDb.getLastFullUpMileage();
        return last-first;
    }

    public String lastWeekDate(){
        curDate=currentDate();
        curDay=Integer.parseInt(curDate.split("-")[2]);
        curMonth=Integer.parseInt(curDate.split("-")[1]);
        curYear=Integer.parseInt(curDate.split("-")[0]);
        if(curDay<=6){
            curDay=(curDay+25);
            if (curMonth==1){
                curMonth=12;
                curYear--;
            } else {
                curMonth--;
            }
            return curYear+"-"+dateCheck(curDay,curMonth);
        }
        return curYear+"-"+dateCheck(curDay-6,curMonth);
    }

    public String lastMonthDate(){
        curDate=currentDate();
        curDay=Integer.parseInt(curDate.split("-")[2]);
        curMonth=Integer.parseInt(curDate.split("-")[1]);
        curYear=Integer.parseInt(curDate.split("-")[0]);
        if (curMonth==1){
            curMonth=12;
            curYear--;
        } else {
            curMonth--;
        }
        return curYear+"-"+dateCheck(curDay,curMonth);
    }

    public String lastYearDate(){
        curDate=currentDate();
        curDay=Integer.parseInt(curDate.split("-")[2]);
        curMonth=Integer.parseInt(curDate.split("-")[1]);
        curYear=Integer.parseInt(curDate.split("-")[0]);
        return (curYear-1)+"-"+dateCheck(curDay,curMonth);
    }

    public String dateCheck(Integer dayOfMonth,Integer monthOfYear){
        String day,month;
        if(((dayOfMonth)>=1)&&((dayOfMonth)<=9)){
            day = "0"+dayOfMonth;
        } else {
            day=dayOfMonth+"";
        }
        if(((monthOfYear)>=0)&&((monthOfYear)<=8)){
            month = "0"+(monthOfYear);
        } else {
            month=(monthOfYear)+"";
        }
        return month+"-"+day;
    }

    public String chosePeriod(){
        switch (dateSpinner.getSelectedItem().toString()) {
            case "Today":
                return currentDate();
            case "Last week":
                return lastWeekDate();
            case "Last month":
                return lastMonthDate();
            case "Last year":
                return lastYearDate();
            case "From beginning":
                return "2000-01-01";
        }
        return "";
    }

    public String choseTable(){
        switch (tableSpinner.getSelectedItem().toString()) {
            case "All":
                return "All";
            case "Fuel":
                return "fuel_table";
            case "Service":
                return "service_table";
            case "Insurance":
                return "ins_table";
            case "Clean":
                return "clean_table";
            case "Travelled distance":
                return "distance";
        }
        return "";
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
                startActivity(new Intent(StatisticActivity.this, SettingsActivity.class));
                return true;
            case R.id.action_export:
                startActivity(new Intent(StatisticActivity.this, ExpImpActivity.class));
                return true;
            default:
                Intent myIntent = new Intent(StatisticActivity.this, MainActivity.class);
                StatisticActivity.this.startActivity(myIntent);
                return true;
        }
    }

}
