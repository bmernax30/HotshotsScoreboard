package com.example.hotshotsscoreboard;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class activityMain extends AppCompatActivity {
    String url = "http://hotshotsvballscoreboard.com:9000/api/games";
    dataScoreboard scoreboard_data = new dataScoreboard("",0,0,0,"","");
    int team1_score = 0;
    int team2_score = 0;
    float x,y;
    float score_line = 0;
    int total_courts = 5;
    int current_court = 0;
    int current_game = 1;

    String[] current_team1_names = {"","","","",""};
    String[] current_team2_names = {"","","","",""};
    int[] current_team1_scores = {0,0,0,0,0};
    int[] current_team2_scores = {0,0,0,0,0};
    int[] current_games = {0,0,0,0,0};
    TextView textTeam1;
    TextView textTeam2;
    TextView textCourt;
    TextView textGame;
    ImageButton imageScore1;
    ImageButton imageScore2;
    JSONObject data = new JSONObject();
    int user_id = 0;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        //Get GUI
        imageScore1 = findViewById(R.id.mainScore1);
        imageScore2 = findViewById(R.id.mainScore2);
        ImageButton buttonClear = findViewById(R.id.mainClearButton);
        ImageButton buttonSwitch = findViewById(R.id.mainSwitchButton);
        ImageButton buttonSettings = findViewById(R.id.mainSettingsButton);
        textCourt  = findViewById(R.id.mainSettingsCourt);
        textTeam1 = findViewById(R.id.mainTeam1);
        textTeam2 = findViewById(R.id.mainTeam2);
        textGame = findViewById(R.id.mainGameNum);

        //Get Data from Previous Activity
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            user_id = extras.getInt("key_user_id");
            String tmpStr = null;
            tmpStr = extras.getString("key_court");
            if(!tmpStr.equals(null))
            {
                switch (tmpStr) {
                    case "B":
                        current_court = 0;
                        break;
                    case "C":
                        current_court = 1;
                        break;
                    case "D":
                        current_court = 2;
                        break;
                    case "E":
                        current_court = 3;
                        break;
                    case "F":
                        current_court = 4;
                        break;
                }
            }
        }


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

        //Get Team Names
        try {
            updateData();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        imageScore1.setOnTouchListener((view, event) ->
        {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
            {
                Log.d("Pressed", "Button pressed");
                x = event.getX();
                y = event.getY();
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d("Released", "Button released");
                view.performClick();
                //Get Height to determine increase decrease score
                int height = imageScore1.getMeasuredHeight();
                score_line = (float) height/2;

                if (y < score_line) {
                    if (team1_score != 39) {
                        team1_score++;
                    }
                } else {
                    if (team1_score != 0) {
                        team1_score--;
                    }
                }

                //Update Image
                String name = "red_" + team1_score;
                int id = getResources().getIdentifier(name, "drawable", getPackageName());
                imageScore1.setBackgroundResource(id);

                //Update Database
                scoreboard_data.setScore1(team1_score);
                scoreboard_data.setScore2(team2_score);

                try {
                    sendVolleyCmd(Request.Method.PUT,url,data);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
                return true;
        });
        imageScore2.setOnTouchListener((view, event) ->
        {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
            {
                Log.d("Pressed", "Button pressed");
                x = event.getX();
                y = event.getY();
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d("Released", "Button released");
                view.performClick();
                //Get Height to determine increase decrease score
                int height = imageScore2.getMeasuredHeight();
                score_line = (float) height/2;

                if (y < score_line) {
                    if (team2_score != 40) {
                        team2_score++;
                    }
                } else {
                    if (team2_score != 0) {
                        team2_score--;
                    }
                }

                //Update Image
                String name = "blue_" + team2_score;
                int id = getResources().getIdentifier(name, "drawable", getPackageName());
                imageScore2.setBackgroundResource(id);

                //Update Database
                scoreboard_data.setScore1(team1_score);
                scoreboard_data.setScore2(team2_score);
                try {
                    sendVolleyCmd(Request.Method.PUT,url,data);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            return true;
        });
        buttonClear.setOnTouchListener((view, event) ->
        {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
            {
                Log.d("Pressed", "Button pressed");
                buttonClear.setBackgroundResource(R.drawable.clear_pressed);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d("Released", "Button released");
                view.performClick();
                buttonClear.setBackgroundResource(R.drawable.clear);
                team1_score = 0;
                team2_score = 0;
                //Update Team 1
                imageScore1.setBackgroundResource(R.drawable.red_0);
                //Update Team 2
                imageScore2.setBackgroundResource(R.drawable.blue_0);

                //Update Database
                scoreboard_data.setScore1(team1_score);
                scoreboard_data.setScore2(team2_score);
                try {
                    sendVolleyCmd(Request.Method.PUT,url,data);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            return true;
        });
        buttonSwitch.setOnTouchListener((view, event) ->
        {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
            {
                Log.d("Pressed", "Button pressed");
                buttonSwitch.setBackgroundResource(R.drawable.switch_scores_pressed);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d("Released", "Button released");
                view.performClick();
                buttonSwitch.setBackgroundResource(R.drawable.switch_scores);
                int tmpScore = team1_score;
                team1_score = team2_score;
                team2_score = tmpScore;

                //Update Team 1
                String name = "red_" + team1_score;
                int id = getResources().getIdentifier(name, "drawable", getPackageName());
                imageScore1.setBackgroundResource(id);

                //Update Team 2
                name = "blue_" + team2_score;
                id = getResources().getIdentifier(name, "drawable", getPackageName());
                imageScore2.setBackgroundResource(id);

                //Update Database
                scoreboard_data.setScore1(team1_score);
                scoreboard_data.setScore2(team2_score);
                try {
                    sendVolleyCmd(Request.Method.PUT,url,data);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            return true;
        });
        buttonSettings.setOnTouchListener((view, event) ->
        {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
            {
                Log.d("Pressed", "Button pressed");
                buttonSettings.setBackgroundResource(R.drawable.settings_pressed);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d("Released", "Button released");
                view.performClick();
                buttonSettings.setBackgroundResource(R.drawable.settings);
                Intent activitySettings = new Intent(getApplicationContext(), activitySettings.class);
                activitySettings.putExtra("key_court",textCourt.getText().toString());
                activitySettings.putExtra("key_user_id",user_id);
                startActivity(activitySettings);
            }
            return true;
        });
        textCourt.setOnClickListener(view -> {
            //Any other user id stays on their court
            if(user_id == 6) {
                current_court++;
                if (current_court > 4) {
                    current_court = 0;
                }
                switch (current_court) {
                    default:
                    case 0:
                        textCourt.setText(getResources().getString(R.string.main_court_b));
                        break;
                    case 1:
                        textCourt.setText(getResources().getString(R.string.main_court_c));
                        break;
                    case 2:
                        textCourt.setText(getResources().getString(R.string.main_court_d));
                        break;
                    case 3:
                        textCourt.setText(getResources().getString(R.string.main_court_e));
                        break;
                    case 4:
                        textCourt.setText(getResources().getString(R.string.main_court_f));
                        break;
                }
                try {
                    updateData();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        textGame.setOnClickListener(view -> {
            current_game++;
            if(current_game > 3)
            {
                current_game = 1;
            }
            switch(current_game)
            {
                default:
                case 1:
                    textGame.setText(getResources().getString(R.string.main_game_1));
                    break;
                case 2:
                    textGame.setText(getResources().getString(R.string.main_game_2));
                    break;
                case 3:
                    textGame.setText(getResources().getString(R.string.main_game_3));
                    break;
            }
            scoreboard_data.setGamenum(current_game);
            try {
                sendVolleyCmd(Request.Method.PUT,url,data);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

    }
    @Override
    protected void onResume()
    {
        super.onResume();
        //Get Data from Previous Activity
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            String tmpStr = null;
            tmpStr = extras.getString("key_court");
            if(!tmpStr.equals(null))
            {
                switch (tmpStr) {
                    default:
                    case "Court B":
                        current_court = 0;
                        scoreboard_data.setCourtname("B");
                        break;
                    case "Court C":
                        current_court = 1;
                        scoreboard_data.setCourtname("C");
                        break;
                    case "Court D":
                        current_court = 2;
                        scoreboard_data.setCourtname("D");
                        break;
                    case "Court E":
                        current_court = 3;
                        scoreboard_data.setCourtname("E");
                        break;
                    case "Court F":
                        current_court = 4;
                        scoreboard_data.setCourtname("F");
                        break;
                }
                textCourt.setText(tmpStr);
            }
        }

        //Get Team Names
        try {
            updateData();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    private void updateData() throws JSONException {
        //Get All Court Data
        for(int i=0; i < total_courts; i++)
        {
            switch(i)
            {
                default:
                case 0:
                    scoreboard_data.setCourtname("B");
                    break;
                case 1:
                    scoreboard_data.setCourtname("C");
                    break;
                case 2:
                    scoreboard_data.setCourtname("D");
                    break;
                case 3:
                    scoreboard_data.setCourtname("E");
                    break;
                case 4:
                    scoreboard_data.setCourtname("F");
                    break;
            }
            try {
                sendVolleyCmd(Request.Method.GET, url, data);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void saveData(JSONObject data) throws JSONException{
            //User ID Sets Court All Time
            switch(user_id)
            {
                case 1: //Court B
                case 2: //Court C
                case 3: //Court D
                case 4: //Court E
                case 5: //Court F
                    current_court = user_id-1;
                    break;
                default: //Master
                    break;
            }
            //Save data into arrays for ui access
            switch (data.getString("courtname"))
            {
                case "B":
                    current_team1_names[0] = data.getString("team1");
                    current_team2_names[0] = data.getString("team2");
                    current_team1_scores[0] = data.getInt("score1");
                    current_team2_scores[0] = data.getInt("score2");
                    current_games[0] = data.getInt("gamenum");
                    break;
                case "C":
                    current_team1_names[1] = data.getString("team1");
                    current_team2_names[1] = data.getString("team2");
                    current_team1_scores[1] = data.getInt("score1");
                    current_team2_scores[1] = data.getInt("score2");
                    current_games[1] = data.getInt("gamenum");
                    break;
                case "D":
                    current_team1_names[2] = data.getString("team1");
                    current_team2_names[2] = data.getString("team2");
                    current_team1_scores[2] = data.getInt("score1");
                    current_team2_scores[2] = data.getInt("score2");
                    current_games[2] = data.getInt("gamenum");
                    break;
                case "E":
                    current_team1_names[3] = data.getString("team1");
                    current_team2_names[3] = data.getString("team2");
                    current_team1_scores[3] = data.getInt("score1");
                    current_team2_scores[3] = data.getInt("score2");
                    current_games[3] = data.getInt("gamenum");
                    break;
                case "F":
                    current_team1_names[4] = data.getString("team1");
                    current_team2_names[4] = data.getString("team2");
                    current_team1_scores[4] = data.getInt("score1");
                    current_team2_scores[4] = data.getInt("score2");
                    current_games[4] = data.getInt("gamenum");
                    break;
            }
            //Update Court Name
            switch(current_court)
            {
                default:
                case 0:
                    scoreboard_data.setCourtname("B");
                    break;
                case 1:
                    scoreboard_data.setCourtname("C");
                    break;
                case 2:
                    scoreboard_data.setCourtname("D");
                    break;
                case 3:
                    scoreboard_data.setCourtname("E");
                    break;
                case 4:
                    scoreboard_data.setCourtname("F");
                    break;
            }

            //Update Team 1 Name
            scoreboard_data.setTeam1(current_team1_names[current_court]);
            textTeam1.setText(current_team1_names[current_court]);
            //Update Team 2 Name
            scoreboard_data.setTeam2(current_team2_names[current_court]);
            textTeam2.setText(current_team2_names[current_court]);
            //Update Score 1 Image
            team1_score = current_team1_scores[current_court];
            scoreboard_data.setScore1(current_team1_scores[current_court]);
            String name = "red_" + current_team1_scores[current_court];
            int id = getResources().getIdentifier(name, "drawable", getPackageName());
            imageScore1.setBackgroundResource(id);
            //Update Score 2 Image
            team2_score = current_team2_scores[current_court];
            scoreboard_data.setScore2(current_team2_scores[current_court]);
            name = "blue_" + current_team2_scores[current_court];
            id = getResources().getIdentifier(name, "drawable", getPackageName());
            imageScore2.setBackgroundResource(id);
            //Update Current Game
            current_game = current_games[current_court];
            scoreboard_data.setGamenum(current_game);
            switch (current_game) {
                default:
                case 1:
                    textGame.setText(getResources().getString(R.string.main_game_1));
                    break;
                case 2:
                    textGame.setText(getResources().getString(R.string.main_game_2));
                    break;
                case 3:
                    textGame.setText(getResources().getString(R.string.main_game_3));
                    break;

            }
    }
    private void sendVolleyCmd(int method, String url, JSONObject data) throws JSONException {

        //Set up the Request
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        //Load in from the scoreboard data class
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

        switch(method)
        {
            case Request.Method.POST:
                break;
            case Request.Method.DELETE:
                break;
            case Request.Method.GET:
            case Request.Method.PUT:
                url = url + "/" + scoreboard_data.getCourtname();
                break;
        }
        JsonObjectRequest request = new JsonObjectRequest(method, url, data, response -> {

            switch(method)
            {
                case Request.Method.GET:
                    Log.d("REST","GET:" + response.toString());
                    try {
                        saveData(response);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case Request.Method.POST:
                    Log.d("REST","POST:" + response.toString());
                    break;
                case Request.Method.DELETE:
                    Log.d("REST","DELETE:" + response.toString());
                    break;
                case Request.Method.PUT:
                    Log.d("REST","PUT:" + response.toString());
                    break;
            }
            Log.d("REST",response.toString());
        }, error -> {
            Log.d("ERROR",error.toString());
        });
        queue.add(request);
    }

}
