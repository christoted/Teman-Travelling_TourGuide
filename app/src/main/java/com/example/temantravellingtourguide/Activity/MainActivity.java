package com.example.temantravellingtourguide.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.temantravellingtourguide.Model.History;
import com.example.temantravellingtourguide.R;

public class MainActivity extends AppCompatActivity {
    LinearLayout itemTourGuide, itemHistory, itemAccount, itemWallet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemTourGuide = findViewById(R.id.item_tour_guide);
        itemWallet = findViewById(R.id.item_wallet);
        itemHistory = findViewById(R.id.item_history);
        itemAccount = findViewById(R.id.item_account);

        itemTourGuide.setOnClickListener((view -> {
            startActivity(new Intent(MainActivity.this, JobActivity.class));
        }));

        itemHistory.setOnClickListener((view -> {
            startActivity(new Intent(MainActivity.this, HistoryActivity.class));
        }));

        itemWallet.setOnClickListener((view -> {
            startActivity(new Intent(MainActivity.this, WalletActivity.class));
        }));

        itemAccount.setOnClickListener((view -> {
            startActivity(new Intent(MainActivity.this, AccountActivity.class));
        }));

    }
}