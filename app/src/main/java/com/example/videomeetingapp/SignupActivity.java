package com.example.videomeetingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        findViewById(R.id.imageBack).setOnClickListener(v -> { onBackPressed(); });
        findViewById(R.id.textSignIn).setOnClickListener(v -> { onBackPressed();});
    }
}
