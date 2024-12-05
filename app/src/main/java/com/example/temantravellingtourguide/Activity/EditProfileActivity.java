package com.example.temantravellingtourguide.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.temantravellingtourguide.Manager.PreferencesManager;
import com.example.temantravellingtourguide.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {

    CountryCodePicker ccp;
    MaterialButton btnSave;
    TextInputEditText etName, etNoWA;
    PreferencesManager preferencesManager;
    Spinner city;
    FirebaseFirestore db;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ccp = findViewById(R.id.ccp);
        btnSave = findViewById(R.id.btn_save);
        etName = findViewById(R.id.et_name);
        etNoWA = findViewById(R.id.et_no_wa);
        city = findViewById(R.id.spinner_city);
        toolbar = findViewById(R.id.toolbar);
        preferencesManager = new PreferencesManager(this);
        db = FirebaseFirestore.getInstance();

        addBackArrow();
        setSpinnerCity();

        etName.setText(preferencesManager.getName());
        etNoWA.setText(preferencesManager.getNoWhatsapp());
        ccp.setDefaultCountryUsingNameCode(preferencesManager.getKodeNegara());

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputIsValid()){
                    updateUserDataInFirestore();
                }
            }
        });
    }

    private void updateUserDataInFirestore() {
        Map<String, Object> data = new HashMap<>();
        data.put("nama",etName.getText().toString());
        data.put("noWhatsapp",etNoWA.getText().toString());
        data.put("kodeNegara",ccp.getSelectedCountryCode());
        data.put("kota",city.getSelectedItem().toString());

        db.collection("partners")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Success", "DocumentSnapshot successfully updated!");
                        updateUserDataInSharedPreferences();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Error", "Error updating document", e);
                    }
                });
    }

    private void updateUserDataInSharedPreferences() {
        preferencesManager.setName(etName.getText().toString());
        preferencesManager.setNoWhatsapp(etNoWA.getText().toString());
        preferencesManager.setKodeNegara(ccp.getSelectedCountryCode());
        preferencesManager.setKota(city.getSelectedItem().toString());
        finish();
    }

    private boolean inputIsValid() {
        if(etName.getText().toString().isEmpty()){
            Toast.makeText(this, "Nama tidak boleh kosong", Toast.LENGTH_LONG).show();
            return false;
        }

        if(etNoWA.getText().toString().isEmpty()){
            Toast.makeText(this, "No Whatsapp tidak boleh kosong", Toast.LENGTH_LONG).show();
            return false;
        }

        if(etNoWA.getText().toString().length() < 9 || etNoWA.getText().toString().length() > 12){
            Toast.makeText(this, "No Whatsapp harus terdiri dari 9-12 digit", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void setSpinnerCity(){
        String[] cities = {"Jakarta","Surabaya","Medan","Solo","Kudus"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_list, cities);

        city.setAdapter(adapter);

        //set spinner city
        String kota = preferencesManager.getKota();
        int idx = 0;

        for(String s : cities){
            if(s.equals(kota)){
                break;
            }
            idx++;
        }

        if(idx == cities.length){
            idx = -1;
        }
        city.setSelection(idx);
    }

    private void addBackArrow(){
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}