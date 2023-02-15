package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Login
 */
public class MainActivity extends AppCompatActivity {
    private EditText editTextUsername, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureTextFields();
        configureNextButton();
    }

    /**
     * Creates the username and password text fields
     */
    private void configureTextFields(){
        editTextUsername=findViewById(R.id.editTextUsername);
        editTextPassword=findViewById(R.id.editTextPassword);
    }

    /**
     * Creates the next button.
     * The next button checks the user credentials, via the {@link #login login} method, method upon being clicked,.
     */
    private void configureNextButton(){
        Button loginButton  = findViewById(R.id.B_Login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String username=editTextUsername.getText().toString();
                String password=editTextPassword.getText().toString();

                login(username, password);
            }
        });
    }

    /**
     * Attempts to log in user into LoyaltyFirst account by sending an HTTP request to the user's local server.
     * This server (through the 'login' servlet) sends an SQL query to the customer database to verify the user's credentials.
     * This application uses Android volley to parse this response from the server.
     * If credentials are valid, the application switches to the activity that displays the user's profile.
     * Otherwise, the application alerts the user that the credentials are invalid.
     * @param username the username of the account
     * @param password the password of the account
     */
    private void login(String username, String password){
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url="http://10.0.2.2:8080/loyaltyfirst/login?user="+username+"&pass="+password;

        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(s.trim().equals("No")){
                    Toast.makeText(MainActivity.this, "Error: invalid user or pass", Toast.LENGTH_LONG).show();
                }
                else{
                    String[] result=s.trim().split(":");
                    String cid=result[1];
                    Intent intent=new Intent(MainActivity.this, MainActivity2.class);
                    intent.putExtra("cid", cid);
                    startActivity(intent);
                }
            }
        }, null);

        queue.add(request);
    }
}