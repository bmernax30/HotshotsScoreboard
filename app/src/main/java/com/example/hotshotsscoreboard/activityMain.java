package com.example.hotshotsscoreboard;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class activityMain extends AppCompatActivity {

    String api_message = "http://hotshotsvballscoreboard.com:9000/api/games";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        //GUI Items
        TextView textResponse = findViewById(R.id.mainResponseText);
        EditText textSend = findViewById(R.id.editSendText);
        ImageButton btnSend = findViewById(R.id.mainSendButton);
        RequestQueue queue = Volley.newRequestQueue(this);
        // Button press event listener
        btnSend.setOnClickListener(v -> {

            // get the text message on the text field
            //api_message = textSend.getText().toString();

            //Volley
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, api_message,
                    response -> {
                        // Display the first 500 characters of the response string.
                        String stringTmp = "Response is:" + response;
                        textResponse.setText(stringTmp);
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String stringError = "That didn't work!";
                    textResponse.setText(stringError);
                }
            });

            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        });
    }
}
