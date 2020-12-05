package com.example.temantravellingtourguide.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.temantravellingtourguide.Fragment.Job;
import com.example.temantravellingtourguide.R;

public class JobActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);

        Job job = new Job();

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, job).commit();
    }
}