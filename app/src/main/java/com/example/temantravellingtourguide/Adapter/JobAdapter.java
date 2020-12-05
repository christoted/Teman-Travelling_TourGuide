package com.example.temantravellingtourguide.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.temantravellingtourguide.Model.History;
import com.example.temantravellingtourguide.R;

import java.util.ArrayList;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {

    Context context;
    ArrayList<History> histories;

    public JobAdapter(Context context, ArrayList<History> histories){
        this.context = context;
        this.histories = histories;
    }


    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_job_single_item, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        History history = histories.get(position);

        holder.tvTitle.setText(history.getTitle1());
        holder.tvLocation.setText(history.getLocation());
        holder.tvNumDays.setText(history.getNumDay()+" days");
        holder.tvDate.setText(history.getDate());
        holder.tvCustomer.setText(history.getCustomerName());
        holder.ivDest.setImageResource(history.getImage());
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

    class JobViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvLocation, tvNumDays, tvDate, tvCustomer;
        ImageView ivDest;
        public JobViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.id_tv_title1_activity_history_single_item);
            tvLocation = itemView.findViewById(R.id.id_tv_location_activity_history_single_item);
            tvNumDays = itemView.findViewById(R.id.id_tv_days_activity_history_single_item);
            tvDate = itemView.findViewById(R.id.id_tv_date_activity_history_single_item);
            tvCustomer = itemView.findViewById(R.id.id_tv_customer_activity_history_single_item);
            ivDest = itemView.findViewById(R.id.id_iv_activity_history_single_item);
        }
    }
}
