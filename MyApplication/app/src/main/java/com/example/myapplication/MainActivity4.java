package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.ArrayList;

/**
 * Displays information of specific transaction
 */
public class MainActivity4 extends AppCompatActivity {
    private HashMap<String, String> map;
    private TextView textViewDate;
    private TextView textViewPoints;
    TableLayout table;
    private String[][] data_table = new String[25][3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        map = (HashMap<String, String>)getIntent().getSerializableExtra("data");
        table = findViewById(R.id.table_main);
        configureTextViews();
        configureSpinner();
    }

    /**
     * Creates spinner that allows user view information on a specific transaction
     */
    private void configureSpinner(){
        Spinner spinner=findViewById(R.id.spinner);
        ArrayList<String> trefs = new ArrayList<>();
        for(String tref : map.keySet()){
            trefs.add(tref);
            System.out.println(tref);
        }

        ArrayAdapter<String> adapter=new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, trefs);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                table.removeAllViews();
                configureTextViews();
                String tref=parent.getSelectedItem().toString();
                String[] data = map.get(tref).split("#");

                if (data.length >= 1) {
                    for(int i=0;i<data.length;i++){
                        data[i]=data[i].trim();
                        String[] inter = data[i].split(",");

                        data_table[i][0] = inter[inter.length - 3];
                        data_table[i][1] = inter[inter.length - 2];
                        data_table[i][2] = inter[inter.length - 1];
                    }

                    String[] header = data[0].split(",");
                    textViewDate.setText(header[0].trim().split(" ")[0]);
                    textViewPoints.setText(header[1].trim());
                    displayTable();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Create table
     */
    private void configureTextViews(){
        textViewPoints=findViewById(R.id.textView9);
        textViewDate=findViewById(R.id.textView6);
        final String[] labels = new String[]{"Prod. Name", "Quantity", "Points"};
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
     * Fill table with transaction information
     */
    public void displayTable() {
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
            TextView tv2 = new TextView(this);
            tv2.setText(strings[2]);
            tv2.setTextColor(Color.BLACK);
            tv2.setTextSize(18);
            tbrow.addView(tv2);

            table.addView(tbrow);
        }
    }
}
