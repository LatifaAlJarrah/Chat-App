package com.example.chitchat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.chitchat.R;
import com.example.chitchat.databinding.ActivityCallBinding;

public class Call extends AppCompatActivity {

    private ActivityCallBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCallBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }


}