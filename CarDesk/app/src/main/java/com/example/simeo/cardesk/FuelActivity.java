package com.example.simeo.cardesk;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.json.JSONException;
import java.net.URISyntaxException;


public class FuelActivity extends ActivityHelper {
    DatabaseHelper myDb;
    EditText editQuantity,editPrice,editDate;
    Button btnAddData;
    Button btnExport;
    Button btnImport;
    Button btnHistory;
    String path = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel);
        myDb=new DatabaseHelper(this);

        editQuantity = (EditText)findViewById(R.id.editText_quantity);
        editPrice = (EditText)findViewById(R.id.editText_price);
        editDate = (EditText)findViewById(R.id.editText_date);
        btnAddData = (Button)findViewById(R.id.button_add);
        btnExport = (Button)findViewById(R.id.button_export);
        btnImport = (Button)findViewById(R.id.button_import);
        btnHistory = (Button)findViewById(R.id.button_history);

        btnExport.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            sendData(myDb);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddData(FuelActivity.this,myDb,editQuantity.getText().toString(),editPrice.getText().toString(),
                                editDate.getText().toString());
                    }
                });

        btnHistory.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        History(FuelActivity.this);
                    }
                });

        btnImport.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Import();
                    }
                });


    }

    private static final int FILE_SELECT_CODE = 0;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(FuelActivity.this, "2.",Toast.LENGTH_SHORT).show();
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    try {
                        path = getPath(this, uri);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    openFile(path,myDb,FuelActivity.this);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



}
