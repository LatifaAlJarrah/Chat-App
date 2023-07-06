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
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_bording1);
//       btnSend.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View view) {
//               Intent intent = new Intent(getApplicationContext(), OnBording2.class);
//               startActivity(intent);
//           }
//       });

    }
}