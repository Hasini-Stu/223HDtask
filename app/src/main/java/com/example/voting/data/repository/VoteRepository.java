package com.example.voting.data.repository;

import com.example.voting.data.model.Vote;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.Map;

public interface VoteRepository {
    Task<Void> createVote(Vote vote);
    Task<Void> castVote(String voteId, String option);
    Task<List<Vote>> getActiveVotes();
    Task<List<Vote>> getVotesByCreator(String creatorId);
    Task<Vote> getVoteById(String voteId);
    Task<Void> updateVoteStatus(String voteId, boolean isActive);
    Task<Map<String, Integer>> getVoteResults(String voteId);
    Task<Void> addVoteTranslation(String voteId, String language, String translation);
} 