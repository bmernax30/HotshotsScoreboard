package com.example.hotshotsscoreboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class activitySettings extends AppCompatActivity {
    String url = "http://hotshotsvballscoreboard.com:9000/api/games";
    dataScoreboard scoreboard_data = new dataScoreboard("B",1,0,0,"Team 1","Team 2");
    String selected_court = "B";
    int user_id = 0;
    String court = "";
    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_settings);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_id = extras.getInt("key_user_id");
            String tmpStr = null;
            court = extras.getString("key_court");
        }

        String[] court_array = {"Court B","Court C","Court D","Court E","Court F"};
        Spinner select_court = findViewById(R.id.settingCourtSelection);
        ArrayAdapter<String> court_adapter = new ArrayAdapter<>(this, R.layout.spinner_item,court_array);
        select_court.setAdapter(court_adapter);
        ImageButton buttonSave = findViewById(R.id.settingsEnterTeamNames);
        EditText team1_name = findViewById(R.id.settingsInputTeam1);
        EditText team2_name = findViewById(R.id.settingsInputTeam2);

        //Set Spinner
        switch(court)
        {
            case "Court B":
                scoreboard_data.setCourtname("B");
                break;
            case "Court C":
                scoreboard_data.setCourtname("C");
                break;
            case "Court D":
                scoreboard_data.setCourtname("D");
                break;
            case "Court E":
                scoreboard_data.setCourtname("E");
                break;
            case "Court F":
                scoreboard_data.setCourtname("F");
                break;
        }
        select_court.setSelection(getIndex(select_court,court));

        JSONObject data = new JSONObject();
        try {
            data.put("courtname",scoreboard_data.getCourtname());
            data.put("gamenum",scoreboard_data.getGamenum());
            data.put("score1",scoreboard_data.getScore1());
            data.put("score2",scoreboard_data.getScore2());
            data.put("team1",scoreboard_data.getTeam1());
            data.put("team2",scoreboard_data.getTeam2());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        select_court.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                if(user_id == 6) {
                    //Get the Course Selection
                    selected_court = parentView.getItemAtPosition(position).toString();

                    switch (selected_court) {
                        case "Court B":
                            scoreboard_data.setCourtname("B");
                            break;
                        case "Court C":
                            scoreboard_data.setCourtname("C");
                            break;
                        case "Court D":
                            scoreboard_data.setCourtname("D");
                            break;
                        case "Court E":
                            scoreboard_data.setCourtname("E");
                            break;
                        case "Court F":
                            scoreboard_data.setCourtname("F");
                            break;
                    }
                }
                else
                {
                    select_court.setSelection(getIndex(select_court,court));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        buttonSave.setOnTouchListener((view, event) ->
        {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
            {
                Log.d("Pressed", "Button pressed");
                buttonSave.setBackgroundResource(R.drawable.button_small_save_pressed);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d("Released", "Button released");
                view.performClick();
                buttonSave.setBackgroundResource(R.drawable.button_small_save);

                scoreboard_data.setTeam1(team1_name.getText().toString());
                scoreboard_data.setTeam2(team2_name.getText().toString());
                try {
                    data.put("team1",scoreboard_data.getTeam1());
                    data.put("team2",scoreboard_data.getTeam2());
                    data.put("courtname",scoreboard_data.getCourtname());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                try {
                    sendVolleyCmd(Request.Method.PUT,url,data);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            return true;
        });
    }

    private void sendVolleyCmd(int method, String url, JSONObject data) throws JSONException {

        //Set up the Request
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        switch(method)
        {
            case Request.Method.GET:
                break;
            case Request.Method.POST:
                break;
            case Request.Method.DELETE:
                break;
            case Request.Method.PUT:
                url = url + "/" + scoreboard_data.getCourtname();
                break;
        }
        JsonObjectRequest request = new JsonObjectRequest(method, url, data, response -> {
            Log.d("REST",response.toString());
            //Success load the other screen
            Intent activityMain = new Intent(getApplicationContext(), activityMain.class);
            activityMain.putExtra("key_court",selected_court);
            activityMain.putExtra("key_user_id",user_id);
            startActivity(activityMain);
        }, error -> {
            Log.d("ERROR",error.toString());
        });
        queue.add(request);
    }
    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }
}
