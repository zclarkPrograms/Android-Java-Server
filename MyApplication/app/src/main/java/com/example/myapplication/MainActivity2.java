package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;

public class MainActivity2 extends AppCompatActivity {
    private TextView NameTextView;
    private TextView PointsTextView;
    private ImageView ProfileImageView;
    private String cid;

    private int numTxnDetDone;
    private String[] transactionDetails;

    private int numRedDetDone;
    private String[] redemptionDetails;

    private int numFamilyDetailsDone;
    private String[] familyDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        configureButtons();
        configureTextAndImageViews();
        setupProfileInfo();
        initializeGlobals();
    }

    private void initializeGlobals(){
        transactionDetails=new String[1];
        numTxnDetDone=0;
        numRedDetDone=0;
        redemptionDetails=new String[1];
        familyDetails=new String[1];
        numFamilyDetailsDone=0;
    }

    private void setupProfileInfo(){
        cid = getIntent().getStringExtra("cid");
        RequestQueue queue = Volley.newRequestQueue(MainActivity2.this);
        String url="http://10.0.2.2:8080/loyaltyfirst/Info.jsp?cid="+cid;

        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                String[] result=s.trim().split(",");
                assert result.length==2;
                result[1]=result[1].substring(0, result[1].length()-1);

                NameTextView.setText(result[0]);
                PointsTextView.setText(result[1]);
                int res=getResources().getIdentifier("drawable/cid" +cid, null, "com.example.myapplication");
                ProfileImageView.setImageResource(res);

            }
        }, null);

        queue.add(request);
    }

    private void configureTextAndImageViews(){
        NameTextView = findViewById(R.id.textViewCustomerName);
        PointsTextView = findViewById(R.id.textViewCustomerPoints);
        ProfileImageView = findViewById(R.id.imageViewCustomer);
    }

    private synchronized void setTransactionDone(){
        numTxnDetDone++;

        if(numTxnDetDone==transactionDetails.length){
            notify();
        }
    }

    private synchronized void waitUntilTransactionsDone() throws InterruptedException{
        while(numTxnDetDone<transactionDetails.length){
            wait();
        }
    }

    private void collectTransactionData(String[] data){
        Runnable r = new Runnable() {
            public void run() {
                try {
                    waitUntilTransactionsDone();

                    HashMap<String, String> map = new HashMap<>();

                    for(int i=0;i<transactionDetails.length;i++){
                        if(transactionDetails[i]!=null){
                            map.put(data[i], transactionDetails[i]);
                        }
                    }

                    Intent intent=new Intent(MainActivity2.this, MainActivity4.class);
                    intent.putExtra("data", map);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread t = new Thread(r);
        t.start();
    }

    private void doAllTxn(){
        RequestQueue queue = Volley.newRequestQueue(MainActivity2.this);
        String url="http://10.0.2.2:8080/loyaltyfirst/Transactions.jsp?cid=" + cid;

        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                String[] rows = s.trim().split("#");
                Intent intent=new Intent(MainActivity2.this, MainActivity3.class);
                intent.putExtra("data", rows);
                startActivity(intent);
            }
        }, null);

        queue.add(request);
    }

    private void getTransactionDetails(String tref, int index){
        RequestQueue queue = Volley.newRequestQueue(MainActivity2.this);
        String url="http://10.0.2.2:8080/loyaltyfirst/TransactionDetails.jsp?tref=" + tref;

        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                transactionDetails[index]=s.trim();

                setTransactionDone();
            }
        }, err -> setTransactionDone());

        queue.add(request);
    }

    private void doTxnDet(){
        RequestQueue queue = Volley.newRequestQueue(MainActivity2.this);
        String url="http://10.0.2.2:8080/loyaltyfirst/Transactions.jsp?cid=" + cid;

        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(s.trim().isEmpty()){
                    Intent intent=new Intent(MainActivity2.this, MainActivity4.class);
                    intent.putExtra("data", new HashMap<>());
                    startActivity(intent);

                    return;
                }

                String[] rows = s.trim().split("#");

                String[] data = new String[rows.length];
                numTxnDetDone=0;
                transactionDetails = new String[rows.length];

                for(int i=0;i<data.length;i++){
                    data[i]=rows[i].split(",")[0];
                    getTransactionDetails(data[i], i);
                }

                collectTransactionData(data);
            }
        }, null);

        queue.add(request);
    }

    private synchronized void setRedemptionDone(){
        numRedDetDone++;

        if(numRedDetDone==redemptionDetails.length){
            notify();
        }
    }

    private synchronized void waitUntilRedemptionsDone() throws InterruptedException{
        while(numRedDetDone<redemptionDetails.length){
            wait();
        }
    }

    private void collectRedemptionData(String[] data){
        Runnable r = new Runnable() {
            public void run() {
                try {
                    waitUntilRedemptionsDone();

                    HashMap<String, String> map = new HashMap<>();

                    for(int i=0;i<redemptionDetails.length;i++){
                        if(redemptionDetails[i]!=null){
                            map.put(data[i], redemptionDetails[i]);
                        }
                    }

                    Intent intent=new Intent(MainActivity2.this, MainActivity5.class);
                    intent.putExtra("data", map);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread t = new Thread(r);
        t.start();
    }

    private void getRedemptionDetails(String prizeid, String cid, int index){
        RequestQueue queue = Volley.newRequestQueue(MainActivity2.this);
        String url="http://10.0.2.2:8080/loyaltyfirst/RedemptionDetails.jsp?prizeid=" + prizeid + "&cid="+cid;

        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                redemptionDetails[index]=s.trim();

                setRedemptionDone();
            }
        }, err -> setRedemptionDone());

        queue.add(request);
    }

    private void doRedDet(){
        RequestQueue queue = Volley.newRequestQueue(MainActivity2.this);
        String url="http://10.0.2.2:8080/loyaltyfirst/PrizeIds.jsp?cid=" + cid;

        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(s.trim().isEmpty()){
                    Intent intent=new Intent(MainActivity2.this, MainActivity5.class);
                    intent.putExtra("data", new HashMap<>());
                    startActivity(intent);

                    return;
                }

                String[] prizeids = s.trim().split("#");

                String[] data = new String[prizeids.length];
                numRedDetDone = 0;
                redemptionDetails = new String[prizeids.length];

                for(int i=0;i<data.length;i++){
                    data[i]=prizeids[i];
                    getRedemptionDetails(data[i], cid, i);
                }

                collectRedemptionData(data);
            }
        }, null);

        queue.add(request);
    }

    private void getFamilyData(String tref, String cid, int index){
        RequestQueue queue = Volley.newRequestQueue(MainActivity2.this);
        String url="http://10.0.2.2:8080/loyaltyfirst/SupportFamilyIncrease.jsp?cid=" + cid + "&tref=" + tref;

        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                familyDetails[index]=s.trim();

                setFamilyDone();
            }
        }, err->setFamilyDone());

        queue.add(request);
    }

    private synchronized void setFamilyDone(){
        numFamilyDetailsDone++;

        if(numFamilyDetailsDone==familyDetails.length){
            notify();
        }
    }

    private synchronized void waitUntilFamilyDetailsDone() throws InterruptedException{
        while(numFamilyDetailsDone<familyDetails.length){
            wait();
        }
    }

    private void collectFamilyData(String[] data){
        Runnable r = new Runnable() {
            public void run() {
                try {
                    waitUntilFamilyDetailsDone();

                    HashMap<String, String> map = new HashMap<>();

                    for(int i=0;i<familyDetails.length;i++){
                        if(familyDetails[i]!=null){
                            map.put(data[i], familyDetails[i]);
                        }
                    }

                    Intent intent=new Intent(MainActivity2.this, MainActivity6.class);
                    intent.putExtra("data", map);
                    intent.putExtra("cid", cid);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread t = new Thread(r);
        t.start();
    }

    private void doFamPer(){
        RequestQueue queue = Volley.newRequestQueue(MainActivity2.this);
        String url="http://10.0.2.2:8080/loyaltyfirst/Transactions.jsp?cid=" + cid;

        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(s.trim().isEmpty()){
                    Intent intent=new Intent(MainActivity2.this, MainActivity6.class);
                    intent.putExtra("data", new HashMap<>());
                    intent.putExtra("cid", cid);
                    startActivity(intent);

                    return;
                }

                String[] rows = s.trim().split("#");
                for(int i=0;i<rows.length;i++){
                    rows[i]=rows[i].split(",")[0];
                }

                numFamilyDetailsDone=0;
                familyDetails = new String[rows.length];

                for(int i=0;i<familyDetails.length;i++){
                    getFamilyData(rows[i], cid, i);
                }

                collectFamilyData(rows);
            }
        }, null);

        queue.add(request);
    }

    private void configureButtons() {
        Button AllTxnButton = findViewById(R.id.buttonAllTransactions);
        AllTxnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doAllTxn();
            }
        });

        Button TxnDetButton = findViewById(R.id.buttonTransactionDetails);
        TxnDetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doTxnDet();
            }
        });

        Button RedDetButton = findViewById(R.id.buttonRedemptionDetails);
        RedDetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doRedDet();
            }
        });

        Button FamPerButton = findViewById(R.id.buttonFamily);
        FamPerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doFamPer();
            }
        });

        Button ExitPerButton = findViewById(R.id.buttonExit);
        ExitPerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveTaskToBack(true);
                System.exit(0);
            }
        });
    }
}