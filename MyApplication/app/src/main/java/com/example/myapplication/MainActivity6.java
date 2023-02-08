package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity6 extends AppCompatActivity {
    private HashMap<String, String> map;
    private TextView textViewTXNPoints;
    private TextView textViewFamilyID;
    private TextView textViewFamilyPercent;
    private String cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
        map = (HashMap<String, String>)getIntent().getSerializableExtra("data");
        cid=getIntent().getStringExtra("cid");
        configureTextViews();
        configureSpinner();
        configureButton();
    }

    private void configureTextViews(){
        textViewTXNPoints=findViewById(R.id.textViewTXNPoints);
        textViewFamilyID=findViewById(R.id.textViewFamilyID);
        textViewFamilyPercent=findViewById(R.id.textViewFamilyPercent);
    }

    private void configureSpinner(){
        Spinner spinner=findViewById(R.id.spinner3);
        ArrayList<String> trefs=new ArrayList<>();
        for(String tref : map.keySet()){
            trefs.add(tref);
        }

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, trefs);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tref=parent.getSelectedItem().toString();
                String[] data = map.get(tref).split(",");

                for(int i=0;i<data.length;i++){
                    data[i]=data[i].trim();
                }

                textViewFamilyID.setText(data[0]);
                textViewFamilyPercent.setText(data[1]);
                textViewTXNPoints.setText(data[2].substring(0, data[2].length()-1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void doAddFamilyPoints(String npoints, String cid, String fid){
        try {
            int points = Integer.parseInt(npoints);

            if(points==0){
                Toast.makeText(MainActivity6.this, "No points to add", Toast.LENGTH_LONG).show();

                return;
            }
        }
        catch(NumberFormatException ex){
            Toast.makeText(MainActivity6.this, "No points to add", Toast.LENGTH_LONG).show();

            return;
        }

        RequestQueue queue = Volley.newRequestQueue(MainActivity6.this);
        String url="http://10.0.2.2:8080/loyaltyfirst/FamilyIncrease.jsp?fid=" + fid + "&cid=" + cid + "&npoints=" + npoints;

        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Toast.makeText(MainActivity6.this, npoints + " Points added to the members of Family ID " + fid, Toast.LENGTH_LONG).show();
            }
        }, null);

        queue.add(request);
    }

    private void configureButton(){
        Button button=findViewById(R.id.buttonAddFamilyPoints);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fid=textViewFamilyID.getText().toString();

                try {
                    int percent = Integer.parseInt(textViewFamilyPercent.getText().toString());
                    int points = Integer.parseInt(textViewTXNPoints.getText().toString());

                    int pointsToAdd = (int)Math.floor(points * percent/100.0);

                    String npoints = String.valueOf(pointsToAdd);

                    doAddFamilyPoints(npoints, cid, fid);
                }
                catch(NumberFormatException ex){
                    ex.printStackTrace();
                }
            }
        });
    }
}