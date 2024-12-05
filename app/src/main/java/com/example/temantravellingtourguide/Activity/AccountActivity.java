package com.example.temantravellingtourguide.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.temantravellingtourguide.Manager.PreferencesManager;
import com.example.temantravellingtourguide.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {

    MaterialButton button;
    Button btnEditProfile;
    Toolbar toolbar;
    CircleImageView profileImage;
    TextView tvName, tvEmail;
    FirebaseFirestore db;
    PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        button = findViewById(R.id.id_btn_logout);
        toolbar = findViewById(R.id.toolbar2);
        profileImage = findViewById(R.id.profile_image);
        tvName = findViewById(R.id.tv_name);
        tvEmail = findViewById(R.id.tv_email);
        btnEditProfile = findViewById(R.id.btn_edit_profile);
        db = FirebaseFirestore.getInstance();
        preferencesManager = new PreferencesManager(this);

        addBackArrow();
        getUserData();

        //noinspection deprecation
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        button.setOnClickListener((view -> {
            //remove all data in sharedPreferences
            new PreferencesManager(this).resetAllUserData();

            //sign out from firebase auth
            FirebaseAuth.getInstance().signOut();

            Intent i = new Intent(AccountActivity.this, WelcomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }));

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AccountActivity.this, EditProfileActivity.class);
                startActivity(i);
            }
        });
    }

    private void getUserData() {
        //profileImage.setImageURI(user.getPhotoUrl());
        tvName.setText(preferencesManager.getName());
        tvEmail.setText(preferencesManager.getEmail());
    }

    private void addBackArrow(){
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}