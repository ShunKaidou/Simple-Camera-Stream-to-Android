package com.example.intruderalert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private static final String SHARED_PREFS_NAME = "MySharedPrefs";
    private SharedPreferences sp;


    EditText username;
    EditText password;
    Button loginButton;



    EditText urledit2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent backgroundServiceIntent = new Intent(this, MyBackgroundService.class);
        startService(backgroundServiceIntent);

        Intent foregroundServiceIntent = new Intent(this, MyForegroundService.class);
        startService(foregroundServiceIntent);


        //SharedPreferences sp = getPreferences(MODE_PRIVATE);
        sp = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);



        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginbutton);


        TextView urlTextview = findViewById(R.id.heading);
        urledit2 = findViewById(R.id.urledit);


        urlTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("somethingurl","clickedbro");

                String savedValue = sp.getString("url", "http://192.168.43.27:5556/video_feed");




                if (urledit2.getVisibility()==View.INVISIBLE){
                    urledit2.setVisibility(View.VISIBLE);
                    urledit2.setText(savedValue);

                }
                else{

                    urledit2.setVisibility(View.INVISIBLE);

                    String updatedurl = urledit2.getText().toString();
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("url", updatedurl);
                    editor.apply();


                }






            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().equals("admin") && password.getText().toString().equals("admin123")){
                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, VideoFeed.class);
                    startActivity(intent);

                }
                else {
                    Toast.makeText(MainActivity.this, "Try Again!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}