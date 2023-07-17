package com.example.chitchat.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatCallback;
import androidx.core.app.TaskStackBuilder;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.chitchat.R;
import com.example.chitchat.utilities.MySessionManager;

public class MainActivity extends AppCompatActivity implements AppCompatCallback, TaskStackBuilder.SupportParentable, ActionBarDrawerToggle.DelegateProvider {
    private static final int SPLASH_DELAY = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogoLauncher logoLauncher = new LogoLauncher();
        logoLauncher.start();
    }

    private class LogoLauncher extends Thread {
        public void run() {
            try {
                // this method will be executed in a separate thread
                // show a logo splash screen for a few seconds
                sleep(SPLASH_DELAY); // Pause the thread for 2000 milliseconds (2 seconds)

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // navigate to the OnBording1 activity after the logo is shown
            Intent intent = new Intent(MainActivity.this, OnBording1.class);
            startActivity(intent);

            // Finish the current activity so that the user cannot go back to the splash screen
            MainActivity.this.finish();
        }
    }
}

