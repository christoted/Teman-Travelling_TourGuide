package com.example.temantravellingtourguide.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.temantravellingtourguide.Adapter.ShowHistoryAdapter;
import com.example.temantravellingtourguide.Fragment.LoadingDialog;
import com.example.temantravellingtourguide.Model.History;
import com.example.temantravellingtourguide.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class HistoryActivity extends AppCompatActivity implements ShowHistoryAdapter.ShowHistoryItemListener{

    ShowHistoryAdapter showHistoryAdapter;
    ArrayList<History> historyArrayList = new ArrayList<>();
    RecyclerView recyclerView;
    LinearLayout empty;
    FirebaseFirestore db;
    LoadingDialog loadingDialog;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        setupRecyclerView();

        toolbar = findViewById(R.id.toolbar);
        empty = findViewById(R.id.empty);
        db = FirebaseFirestore.getInstance();
        loadingDialog = new LoadingDialog();

        addBackArrow();
        prepareData();
    }

    private void prepareData(){
        showDialog();
        db.collection("history")
                .orderBy("historyTimestamp", Query.Direction.DESCENDING)
                .whereEqualTo("partnerUID", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
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

                                historyArrayList.add(new History(userUID,city,language,dateAndTime,duration,timeType,needVehicle,paymentMethod,price,status));
                            }

                            if(historyArrayList.isEmpty()){
                                //show empty layout
                                empty.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                            else{
                                //show history layout
                                empty.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);

                                showHistoryAdapter = new ShowHistoryAdapter(HistoryActivity.this, historyArrayList,HistoryActivity.this::onShowHistoryItemClick);
                                recyclerView.setAdapter(showHistoryAdapter);
                                showHistoryAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d("Error", "Error getting documents: ", task.getException());
                            //show empty layout
                            empty.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }

                        loadingDialog.dismiss();
                    }
                });
    }


    public void setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewShowHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onShowHistoryItemClick(int position) {
        History history = historyArrayList.get(position);
        //  Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
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