package com.example.temantravellingtourguide.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.temantravellingtourguide.Adapter.JobAdapter;
import com.example.temantravellingtourguide.Model.History;
import com.example.temantravellingtourguide.R;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    JobAdapter jobAdapter;
    ArrayList<History> historyArrayList = new ArrayList<>();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        setupRecyclerView();
        prepareData();
    }

    private void prepareData(){
        String[] CustomerName = {"Mr.James", "Mr.Tanoko"};
        String[] date = {"20 - 21 Januari 2020", "20 - 25 Oktober 2020"};
        int[] numDay = {2,6};
        String[] location = {"Central Java, Indonesia", "Bali, Indonesia"};
        String[] title1 = {"Explore to Jogjakarta", "Explore to Bali"};
        int[] image = {R.drawable.teman_travelling_logo, R.drawable.teman_travelling_logo};

        if (historyArrayList.size() != 0 ) {
            return;
        }

        for ( int i = 0; i < CustomerName.length; i++) {
            History history = new History();
            history.setImage(image[i]);
            history.setCustomerName(CustomerName[i]);
            history.setDate(date[i]);
            history.setNumDay(numDay[i]);
            history.setLocation(location[i]);
            history.setTitle1(title1[i]);

            historyArrayList.add(history);
            Log.d("12345", "prepareData: " + history.getNumDay());
        }

        jobAdapter = new JobAdapter(this, historyArrayList);
        recyclerView.setAdapter(jobAdapter);

        jobAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.id_fragment_rv);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}