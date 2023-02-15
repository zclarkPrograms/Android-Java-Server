package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

/**
 * Customer Profile
 */
public class MainActivity2 extends AppCompatActivity {
    private TextView NameTextView;
    private TextView PointsTextView;
    private ImageView ProfileImageView;
    private String cid;

    private int numDone;
    private String[] details;

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
        numDone = 0;
        details = new String[1];
    }

    /**
     * Displays the profile information of the customer based on the customer's id (customer name, loyalty points, and profile image).
     * The customer name and points are obtained from an HTTP GET request to a local Apache Tomcat server.
     * The profile image is selected from the drawable resources, the image whose name corresponds with the customer's id (cid).
     */
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

    private synchronized void setActionDone(){
        numDone++;

        if(numDone == details.length){
            notify();
        }
    }

    private synchronized void waitUntilActionDone() throws InterruptedException{
        while(numDone < details.length){
            wait();
        }
    }

    /**
     * Obtains information about each transaction this customer made.
     */
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

    /**
     * Waits for data to be collected from the various HTTP requests that were sent,
     * then brings that data with it to the next activity (MainActivity4).
     * This avoids the issue of the main thread switching activities before every thread has received a response to the HTTP Request.
     * @param data data to bring to the next activity
     */
    private void collectTransactionData(String[] data){
        Runnable r = new Runnable() {
            public void run() {
                try {
                    waitUntilActionDone();

                    HashMap<String, String> map = new HashMap<>();

                    for(int i=0;i<details.length;i++){
                        if(details[i]!=null){
                            map.put(data[i], details[i]);
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

    /**
     * First obtains all transactions that this customer made,
     * then uses this information to make another request for specific details about these transactions.
     */
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
                numDone=0;
                details = new String[rows.length];

                for(int i=0;i<data.length;i++){
                    data[i]=rows[i].split(",")[0];
                    String url="http://10.0.2.2:8080/loyaltyfirst/TransactionDetails.jsp?tref=" + data[i];
                    getData(url, i);
                }

                collectTransactionData(data);
            }
        }, null);

        queue.add(request);
    }

    private void collectRedemptionData(String[] data){
        Runnable r = new Runnable() {
            public void run() {
                try {
                    waitUntilActionDone();

                    HashMap<String, String> map = new HashMap<>();

                    for(int i=0;i<details.length;i++){
                        if(details[i]!=null){
                            map.put(data[i], details[i]);
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

    /**
     * Obtains prize redemption details then switches to the next activity (MainActivity5)
     */
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
                numDone = 0;
                details = new String[prizeids.length];

                for(int i=0;i<data.length;i++){
                    data[i]=prizeids[i];
                    String url="http://10.0.2.2:8080/loyaltyfirst/RedemptionDetails.jsp?prizeid=" + data[i] + "&cid="+cid;
                    getData(url, i);
                }

                collectRedemptionData(data);
            }
        }, null);

        queue.add(request);
    }

    /**
     * Waits for all HTTP requests to finish before sending that data to the next activity and switches to the activity.
     * @param data the data to send to the next activity
     */
    private void collectFamilyData(String[] data){
        Runnable r = new Runnable() {
            public void run() {
                try {
                    waitUntilActionDone();

                    HashMap<String, String> map = new HashMap<>();

                    for(int i=0;i<details.length;i++){
                        if(details[i]!=null){
                            map.put(data[i], details[i]);
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

    /**
     * Retrieves the necessary data for adding points to family accounts, then
     * switches to the relevant activity (MainActivity6).
     */
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

                numDone=0;
                details = new String[rows.length];

                for(int i=0;i<details.length;i++){
                    String url="http://10.0.2.2:8080/loyaltyfirst/SupportFamilyIncrease.jsp?cid=" + cid + "&tref=" + rows[i];
                    getData(url, i);
                }

                collectFamilyData(rows);
            }
        }, null);

        queue.add(request);
    }

    /**
     * Sends an HTTP request to the Apache Server to obtain customer data and places the response data into an array.
     * @param url HTTP GET request
     * @param index index of the details array to fill with data
     */
    private void getData(String url, int index){
        RequestQueue queue = Volley.newRequestQueue(MainActivity2.this);

        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                details[index]=s.trim();

                setActionDone();
            }
        }, err -> setActionDone());

        queue.add(request);
    }

    /**
     * Runs the provided runnable.
     * This is useful for preventing the ActionListener for other buttons from being activated before another button switches the intent.
     * @param runnable the method to run
     */
    private synchronized void executeTask(Runnable runnable){
        numDone = 0;
        runnable.run();
    }

    /**
     * Configures all the buttons that this activity uses:
     * <b>ALLTxnButton</b> (displays basic information of each of customer's transactions)<br>
     * <b>TxnDetButton</b> (displays of information for a given transaction)<br>
     * <b>RedDetButton</b> (displays details of a selected prize redemption)<br>
     * <b>FamPerButton</b> (allows the user to add points to family accounts)<br>
     * <b>ExitPerButton</b> (exits the application)
     */
    private void configureButtons() {
        Button AllTxnButton = findViewById(R.id.buttonAllTransactions);
        AllTxnButton.setOnClickListener(view -> executeTask(() -> doAllTxn()));

        Button TxnDetButton = findViewById(R.id.buttonTransactionDetails);
        TxnDetButton.setOnClickListener(view -> executeTask(() -> doTxnDet()));

        Button RedDetButton = findViewById(R.id.buttonRedemptionDetails);
        RedDetButton.setOnClickListener(view -> executeTask(() -> doRedDet()));

        Button FamPerButton = findViewById(R.id.buttonFamily);
        FamPerButton.setOnClickListener(view -> executeTask(() -> doFamPer()));

        Button ExitPerButton = findViewById(R.id.buttonExit);
        ExitPerButton.setOnClickListener(view -> {
            moveTaskToBack(true);
            System.exit(0);
        });
    }
}