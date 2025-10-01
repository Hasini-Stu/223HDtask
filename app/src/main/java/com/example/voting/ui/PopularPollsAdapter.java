package com.example.voting.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voting.R;
import com.example.voting.data.model.Poll;

import java.util.ArrayList;
import java.util.List;

public class PopularPollsAdapter extends RecyclerView.Adapter<PopularPollsAdapter.ViewHolder> {
    private List<Poll> polls = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_popular_poll, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Poll poll = polls.get(position);
        holder.textPollTitle.setText(poll.getTitle());
        
        // TODO: Implement vote counting when voting is implemented
        int totalVotes = 0;
        holder.textVoteCount.setText(String.valueOf(totalVotes));
        
        // For now, show a placeholder progress
        holder.progressBar.setProgress(0);
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
        TextView textVoteCount;
        ProgressBar progressBar;

        ViewHolder(View itemView) {
            super(itemView);
            textPollTitle = itemView.findViewById(R.id.textPollTitle);
            textVoteCount = itemView.findViewById(R.id.textVoteCount);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
} 