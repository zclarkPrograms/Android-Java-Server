package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Lists all transactions
 */
public class MainActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        displayTable();
    }

    /**
     * Creates a table of the information for each transaction (Transaction ID, Date, Reward Points, Total Cost).
     */
    public void displayTable() {
        TableLayout table = (TableLayout) findViewById(R.id.table_main2);
        final String[] labels = new String[]{"TXN Ref", "Date", "Points", "Total"};
        TableRow tbrow0 = new TableRow(this);
        for (String l : labels) {
            TextView tv = new TextView(this);
            tv.setText(l);
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(20);
            tbrow0.addView(tv);
        }
        table.addView(tbrow0);
        Intent intent = getIntent();
        String[] rows = intent.getStringArrayExtra("data");

        for (String row : rows) {
            String[] fields = row.split(",");
            TableRow tbrow = new TableRow(this);
            tbrow.setPadding(0, 10, 0, 0);
            for (String f : fields) {
                TextView tv = new TextView(this);
                tv.setText(f);
                tv.setTextColor(Color.BLACK);
                tv.setTextSize(18);
                tbrow.addView(tv);
            }
            table.addView(tbrow);
        }
    }
}