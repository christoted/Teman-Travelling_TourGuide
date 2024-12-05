package com.example.temantravellingtourguide.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.temantravellingtourguide.Model.History;
import com.example.temantravellingtourguide.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class IncomeAdapter extends RecyclerView.Adapter<com.example.temantravellingtourguide.Adapter.IncomeAdapter.ShowHistoryViewHolder> {

    private Context context;
    private ArrayList<History> arrayList;

    public IncomeAdapter(Context context, ArrayList<History> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ShowHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.income_single_item,parent, false);
        return new ShowHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowHistoryViewHolder holder, int position) {
        History history = arrayList.get(position);

        holder.tvCity.setText(history.getCity());
        holder.tvDateAndTime.setText(formatDateAndTime(history.getDateAndTime()));
        holder.tvDuration.setText(history.getDuration()+" "+history.getTimeType());
        holder.tvPrice.setText("Rp "+history.getPrice());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ShowHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCity, tvDateAndTime, tvDuration, tvPrice;

        public ShowHistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCity = itemView.findViewById(R.id.tv_city);
            tvDateAndTime = itemView.findViewById(R.id.tv_date_and_time);
            tvDuration = itemView.findViewById(R.id.tv_duration);
            tvPrice = itemView.findViewById(R.id.tv_price);
        }
    }

    private String formatDateAndTime(String dateAndTime) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateAndTime);
            return new SimpleDateFormat("dd MMMM yyyy, HH:mm").format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
