package com.example.simeo.cardesk;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.ganfra.materialspinner.MaterialSpinner;
import mehdi.sakout.fancybuttons.FancyButton;


public class SearchActivity extends ActivityHelper {
    DatabaseHelper myDb;
    TextView sumView,fuelView;
    FancyButton btnRefresh;
    MaterialSpinner dateSpinner,tableSpinner;
    String curDate,liquidMeasure,distanceMeasure,currencyMeasure;
    int curDay,curMonth,curYear;
    double sum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        myDb=new DatabaseHelper(this);
        liquidMeasure=myDb.getSettings().split(":")[0];
        distanceMeasure=myDb.getSettings().split(":")[1];
        currencyMeasure=myDb.getSettings().split(":")[2];

        ToolBar("Statistics");

        btnRefresh = (FancyButton)findViewById(R.id.button_refresh);
        sumView= (TextView)findViewById(R.id.sumTxt);
        fuelView= (TextView)findViewById(R.id.fuelTxt);
        fuelView.setVisibility(View.INVISIBLE);


        String[] TABLE = {"All", "Fuel", "Service", "Insurance", "Clean"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, TABLE);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tableSpinner = (MaterialSpinner)findViewById(R.id.tableSpinner);
        tableSpinner.setAdapter(adapter);
        String[] DATES = {"Today","Last week", "Last month", "Last year", "From beginning"};
        ArrayAdapter<String> dateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, DATES);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner = (MaterialSpinner)findViewById(R.id.timeSpinner);
        dateSpinner.setAdapter(dateAdapter);

        curDate=currentDate();


        AdGenerator();
        /*
        btnRefresh.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("All".equals(choseTable())) {
                            fuelView.setVisibility(View.INVISIBLE);
                            sum = 0;
                            sum += Double.parseDouble(myDb.sumQuery(chosePeriod(), curDate, "fuel_table"));
                            sum += Double.parseDouble(myDb.sumQuery(chosePeriod(), curDate, "service_table"));
                            sum += Double.parseDouble(myDb.sumQuery(chosePeriod(), curDate, "ins_table"));
                            sum += Double.parseDouble(myDb.sumQuery(chosePeriod(), curDate, "clean_table"));
                            sumView.setText(allSumCost(sum));
                        } else if ("fuel_table".equals(choseTable())) {
                            fuelView.setVisibility(View.VISIBLE);
                            fuelView.setText(fuelConsumption());
                            sumView.setText(curDate);
                            sumView.setText(myDb.preciseFuel());
                        } else {
                           fuelView.setVisibility(View.INVISIBLE);
                           sumView.setText(sumCost());
                        }
                   }
                }
        );
        */

    }

    public String currentDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();
        return dateForBase(dateFormat.format(date));
    }

    /*public String sumCost(){
        return dateSpinner.getSelectedItem().toString()+" you paid " +
                myDb.sumQuery(chosePeriod(),curDate,choseTable())+" "+currencyMeasure+".";
    }

    public String allSumCost(double total){
        return dateSpinner.getSelectedItem().toString()+" you paid " + total +" "+currencyMeasure+".";
    }

    public String fuelConsumption(){
        double consumption = myDb.FuelBetweenDate(chosePeriod(), curDate)/traveledDistance()*100;
        return "Your fuel consumption is " + (Math.floor(consumption * 100) / 100)+
                " "+ liquidMeasure+"s per 100 " + distanceMeasure+".";
    }

    public int traveledDistance(){
        int first = Integer.parseInt(myDb.GetFirstMileage(chosePeriod(),curDate));
        int last = Integer.parseInt(myDb.GetLastMileage(chosePeriod(),curDate));
        return last-first;
    }

    public String LastWeekDate(){
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
            return curYear+"-"+curMonth+"-"+(curDay);
        }
        return curYear+"-"+curMonth+"-"+(curDay-6);
    }

    public String LastMonthDate(){
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
        return curYear+"-"+curMonth+"-"+(curDay);
    }

    public String LastYearDate(){
        curDate=currentDate();
        curDay=Integer.parseInt(curDate.split("-")[2]);
        curMonth=Integer.parseInt(curDate.split("-")[1]);
        curYear=Integer.parseInt(curDate.split("-")[0]);
        return (curYear-1)+"-"+curMonth+"-"+curDay;
    }

    public String chosePeriod(){
        switch (dateSpinner.getSelectedItem().toString()) {
            case "Today":
                return currentDate();
            case "Last week":
                return LastWeekDate();
            case "Last month":
                return LastMonthDate();
            case "Last year":
                return LastYearDate();
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
        }
        return "";
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(SearchActivity.this, SettingsActivity.class));
                return true;
            default:
                Intent myIntent = new Intent(SearchActivity.this, MainActivity.class);
                SearchActivity.this.startActivity(myIntent);
                return true;
        }
    }

}
