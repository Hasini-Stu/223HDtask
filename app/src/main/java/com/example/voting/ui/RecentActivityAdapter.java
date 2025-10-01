package com.example.voting.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voting.R;
import com.example.voting.data.model.Poll;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecentActivityAdapter extends RecyclerView.Adapter<RecentActivityAdapter.ViewHolder> {
    private List<Poll> polls = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recent_activity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Poll poll = polls.get(position);
        holder.textPollTitle.setText(poll.getTitle());
        // No createdAt field; using poll ID as a placeholder for creation order
        holder.textDate.setText("ID: " + poll.getId());
        holder.textStatus.setText(poll.getEndTimeMillis() > System.currentTimeMillis() ? "Active" : "Ended");
    }

    @Override
    public int getItemCount() {
        return polls.size();
    }

    public void setPolls(List<Poll> polls) {
        this.polls = polls;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textPollTitle;
        TextView textDate;
        TextView textStatus;

        ViewHolder(View itemView) {
            super(itemView);
            textPollTitle = itemView.findViewById(R.id.textPollTitle);
            textDate = itemView.findViewById(R.id.textDate);
            textStatus = itemView.findViewById(R.id.textStatus);
        }
    }
} 