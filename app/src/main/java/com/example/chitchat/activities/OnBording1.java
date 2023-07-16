package com.example.chitchat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.chitchat.R;
import com.example.chitchat.utilities.MySessionManager;

public class OnBording1 extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private static final String PREF_FIRST_TIME_KEY = "firstTime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_bording1);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Check if the user has already seen the welcome screens
        boolean isFirstTime = sharedPreferences.getBoolean(PREF_FIRST_TIME_KEY, true);

        if (!isFirstTime) {
            startLoginActivity();
        }

        Button nextButton = findViewById(R.id.button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update the shared preferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(PREF_FIRST_TIME_KEY, false);
                editor.apply();

                startLoginActivity();
            }
        });
    }

    private void startLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), login.class);
        startActivity(intent);
        finish();
    }
}
