package com.example.voting.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voting.R;
import com.example.voting.data.model.Poll;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PollAdapter extends RecyclerView.Adapter<PollAdapter.PollViewHolder> {
    public interface OnPollClickListener {
        void onPollClick(Poll poll);
    }

    private List<Poll> polls;
    private final OnPollClickListener listener;

    public PollAdapter(List<Poll> polls, OnPollClickListener listener) {
        this.polls = polls;
        this.listener = listener;
    }

    public void setPolls(List<Poll> polls) {
        this.polls = polls;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PollViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_poll, parent, false);
        return new PollViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PollViewHolder holder, int position) {
        Poll poll = polls.get(position);
        holder.title.setText(poll.getTitle());
        holder.description.setText(poll.getDescription());
        String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                .format(new Date(poll.getEndTimeMillis()));
        holder.endTime.setText("Ends: " + endTime);
        
        // Set click listeners
        holder.itemView.setOnClickListener(v -> listener.onPollClick(poll));
        holder.shareButton.setOnClickListener(v -> sharePoll(holder.itemView.getContext(), poll));
    }

    private void sharePoll(Context context, Poll poll) {
        String shareText = String.format(
            "Check out this poll: %s\n\n%s\n\nEnds: %s\n\nVote here: https://securevote.app/poll/%d",
            poll.getTitle(),
            poll.getDescription(),
            new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                .format(new Date(poll.getEndTimeMillis())),
            poll.getId()
        );

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(shareIntent, "Share Poll"));
    }

    @Override
    public int getItemCount() {
        return polls == null ? 0 : polls.size();
    }

    public Poll getPollAt(int position) {
        return polls.get(position);
    }

    public void removePollAt(int position) {
        polls.remove(position);
        notifyItemRemoved(position);
    }

    static class PollViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, endTime;
        ImageButton shareButton;

        PollViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textTitle);
            description = itemView.findViewById(R.id.textDescription);
            endTime = itemView.findViewById(R.id.textEndTime);
            shareButton = itemView.findViewById(R.id.buttonShare);
        }
    }
} 