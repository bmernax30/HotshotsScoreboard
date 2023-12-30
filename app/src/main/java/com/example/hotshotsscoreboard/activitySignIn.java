package com.example.hotshotsscoreboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class activitySignIn extends AppCompatActivity {
    String passwordb = "courtbref1989";
    String passwordc = "courtcref1989";
    String passwordd = "courtdref1989";
    String passworde = "courteref1989";
    String passwordf = "courtfref1989";
    String username = "ref";
    String username_master = "masterref";
    String password_master = "masterref1989";
    int user_id = 0;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signin);

        Intent activityMain = new Intent(getApplicationContext(), activityMain.class);
        ImageButton buttonLogin = findViewById(R.id.signinButton);
        EditText textUsername = findViewById(R.id.signinNameInput);
        EditText textPassword = findViewById(R.id.signinPasswordInput);
        buttonLogin.setOnTouchListener((view, event) ->
        {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
            {
                Log.d("Pressed", "Button pressed");
                buttonLogin.setBackgroundResource(R.drawable.login_login_button_pressed);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d("Released", "Button released");
                view.performClick();
                buttonLogin.setBackgroundResource(R.drawable.login_login_button_not_pressed);

                switch(textUsername.getText().toString())
                {
                    case "masterref":
                        user_id = 6;
                        break;
                    case "ref":
                        user_id = 1;
                        break;
                    default:
                        user_id = 0;
                        break;
                }
                if(user_id != 0) {
                    switch (textPassword.getText().toString()) {
                        case "courtbref1989":
                            user_id = 1;
                            activityMain.putExtra("key_court","Court B");
                            break;
                        case "courtcref1989":
                            user_id = 2;
                            activityMain.putExtra("key_court","Court C");
                            break;
                        case "courtdref1989":
                            user_id = 3;
                            activityMain.putExtra("key_court","Court D");
                            break;
                        case "courteref1989":
                            user_id = 4;
                            activityMain.putExtra("key_court","Court E");
                            break;
                        case "courtfref1989":
                            user_id = 5;
                            activityMain.putExtra("key_court","Court F");
                            break;
                        case "masterref1989":
                            user_id = 6;
                            activityMain.putExtra("key_court","Court B");
                            break;
                        default:
                            user_id = 0;
                            break;
                    }
                }
                if(user_id != 0)
                {
                    activityMain.putExtra("key_user_id",user_id);
                    startActivity(activityMain);
                }
            }
            return true;
        });
    }
    @Override
    public void onBackPressed() {
        Intent signinActivity = new Intent(getApplicationContext(), activitySignIn.class);
        startActivity(signinActivity);
    }
}
