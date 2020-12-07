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

public class ShowHistoryAdapter extends RecyclerView.Adapter<com.example.temantravellingtourguide.Adapter.ShowHistoryAdapter.ShowHistoryViewHolder> {

    Context mContext;
    ArrayList<History> historyArrayList;
    ShowHistoryItemListener showHistoryItemListener;

    public ShowHistoryAdapter(Context context, ArrayList<History> histories, ShowHistoryItemListener showHistoryItemListener){
        this.historyArrayList = histories;
        this.mContext = context;
        this.showHistoryItemListener = showHistoryItemListener;
    }


    @NonNull
    @Override
    public ShowHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_history_single_item,parent, false);
        return new ShowHistoryViewHolder(view, showHistoryItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowHistoryViewHolder holder, int position) {
        History history = historyArrayList.get(position);

        holder.tvCity.setText(history.getCity());
        holder.tvDateAndTime.setText(formatDateAndTime(history.getDateAndTime()));
        holder.tvDuration.setText(history.getDuration()+" "+history.getTimeType());
        holder.tvStatus.setText(history.getStatus());
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

    @Override
    public int getItemCount() {
        return historyArrayList.size();
    }

    public class ShowHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCity, tvDateAndTime, tvDuration, tvStatus;
        public ShowHistoryViewHolder(@NonNull View itemView, ShowHistoryItemListener showHistoryItemListener) {
            super(itemView);
            tvCity = itemView.findViewById(R.id.tv_city);
            tvDateAndTime = itemView.findViewById(R.id.tv_date_and_time);
            tvDuration = itemView.findViewById(R.id.tv_duration);
            tvStatus = itemView.findViewById(R.id.tv_status);

            itemView.setOnClickListener((view) -> {
                showHistoryItemListener.onShowHistoryItemClick(getAdapterPosition());
            });
        }
    }

    public interface ShowHistoryItemListener{
        void onShowHistoryItemClick(int position);
    }
}
