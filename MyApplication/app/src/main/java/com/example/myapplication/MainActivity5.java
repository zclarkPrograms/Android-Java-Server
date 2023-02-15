package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Display information of selected prize
 */
public class MainActivity5 extends AppCompatActivity {
    private HashMap<String, String> rows;
    TableLayout table;
    private String[][] data_table = new String[25][2];
    private TextView description;
    private TextView pointsNeeded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        rows = (HashMap<String, String>)getIntent().getSerializableExtra("data");
        table = findViewById(R.id.table_main2);
        configureText();
        handleSpinner();
    }

    /**
     * Creates table
     */
    private void configureText() {
        description = findViewById(R.id.textView5);
        pointsNeeded = findViewById(R.id.textView8);
        final String[] labels = new String[]{"Redemption Date", "Exchange Center"};
        TableRow tbrow0 = new TableRow(this);
        for (String l : labels) {
            TextView tv = new TextView(this);
            tv.setText(l);
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(20);
            tbrow0.addView(tv);
        }
        table.addView(tbrow0);
    }

    /**
     * Creates spinner for selecting prize
     */
    private void handleSpinner() {
        Spinner spinner = findViewById(R.id.spinner2);
        ArrayList<String> prizeIDs = new ArrayList<>();
        for(String prizeID : rows.keySet()){
            prizeIDs.add(prizeID);
        }

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, prizeIDs);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                table.removeAllViews();
                configureText();
                String prizeID=parent.getSelectedItem().toString();
                String[] data = rows.get(prizeID).split("#");
                if (data.length >= 1) {
                    for(int i=0;i<data.length;i++){
                        data[i]=data[i].trim();
                        String[] inter = data[i].split(",");
                        data_table[i][0] = inter[inter.length - 2];
                        data_table[i][1] = inter[inter.length - 1];
                    }

                    String[] header = data[0].split(",");
                    description.setText(header[0].trim());
                    pointsNeeded.setText(header[1].trim() + " Points");
                    loadTable();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Loads table with prize information
     */
    public void loadTable() {
        for (String[] strings : data_table) {
            TableRow tbrow = new TableRow(this);
            tbrow.setPadding(0, 10, 0, 0);

            TextView tv = new TextView(this);
            tv.setText(strings[0]);
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(18);
            tbrow.addView(tv);
            TextView tv1 = new TextView(this);
            tv1.setText(strings[1]);
            tv1.setTextColor(Color.BLACK);
            tv1.setTextSize(18);
            tbrow.addView(tv1);

            table.addView(tbrow);
        }
    }
}