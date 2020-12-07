package com.example.temantravellingtourguide.SplashScreen;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.temantravellingtourguide.Activity.MainActivity;
import com.example.temantravellingtourguide.Activity.WelcomeActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set theme to light
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null){
            startActivity(new Intent(this, WelcomeActivity.class));
        }else{
            startActivity(new Intent(this, MainActivity.class));
        }

        finish();
    }
}