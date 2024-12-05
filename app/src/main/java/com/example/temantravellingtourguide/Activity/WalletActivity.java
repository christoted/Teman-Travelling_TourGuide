package com.example.temantravellingtourguide.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.temantravellingtourguide.Adapter.IncomeAdapter;
import com.example.temantravellingtourguide.Fragment.LoadingDialog;
import com.example.temantravellingtourguide.Model.History;
import com.example.temantravellingtourguide.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class WalletActivity extends AppCompatActivity {

    RecyclerView rvIncome;
    TextView tvIncome;
    Toolbar toolbar;
    LinearLayout empty;
    FirebaseFirestore db;
    LoadingDialog loadingDialog;
    ArrayList<History> incomeArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        toolbar = findViewById(R.id.toolbar);
        tvIncome = findViewById(R.id.tv_income);
        rvIncome = findViewById(R.id.rv_income);
        empty = findViewById(R.id.empty);
        db = FirebaseFirestore.getInstance();
        loadingDialog = new LoadingDialog();
        incomeArrayList = new ArrayList<>();

        addBackArrow();
        prepareData();
    }

    private void prepareData(){
        showDialog();
        db.collection("history")
                .whereEqualTo("partnerUID", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int totalIncome = 0;

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Success", document.getId() + " => " + document.getData());

                                String userUID = document.getString("userUID");
                                String city = document.getString("city");
                                String language = document.getString("language");
                                String dateAndTime = document.getString("dateAndTime");
                                int duration = document.getLong("duration").intValue();
                                String timeType = document.getString("timeType");
                                boolean needVehicle =  document.getBoolean("needVehicle");
                                String paymentMethod = document.getString("paymentMethod");
                                long price = document.getLong("price");
                                String status = document.getString("status");

                                totalIncome += price;

                                incomeArrayList.add(new History(userUID,city,language,dateAndTime,duration,timeType,needVehicle,paymentMethod,price,status));
                            }

                            if(incomeArrayList.isEmpty()){
                                //show empty layout
                                empty.setVisibility(View.VISIBLE);
                                rvIncome.setVisibility(View.GONE);
                            }
                            else{
                                //show history layout
                                empty.setVisibility(View.GONE);
                                rvIncome.setVisibility(View.VISIBLE);

                                IncomeAdapter incomeAdapter = new IncomeAdapter(WalletActivity.this,incomeArrayList);
                                rvIncome.setLayoutManager(new LinearLayoutManager(WalletActivity.this));
                                rvIncome.setAdapter(incomeAdapter);
                            }

                            //show total income
                            tvIncome.setText("Rp "+totalIncome);

                        } else {
                            Log.d("Error", "Error getting documents: ", task.getException());
                        }
                        loadingDialog.dismiss();
                    }
                });
    }

    private void showDialog() {
        FragmentManager fm = getSupportFragmentManager();
        loadingDialog.show(fm, "fragment_loading_dialog");
    }

    private void addBackArrow(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(view1 -> Objects.requireNonNull(this).onBackPressed());
    }
}