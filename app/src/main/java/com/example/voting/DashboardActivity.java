package com.example.voting;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voting.data.db.PollDatabase;
import com.example.voting.data.model.Poll;
import com.example.voting.ui.PopularPollsAdapter;
import com.example.voting.ui.RecentActivityAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    private TextView textActivePolls;
    private TextView textTotalVotes;
    private RecyclerView recentActivityRecyclerView;
    private RecyclerView popularPollsRecyclerView;
    private BottomNavigationView bottomNavigationView;
    private RecentActivityAdapter recentActivityAdapter;
    private PopularPollsAdapter popularPollsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize views
        textActivePolls = findViewById(R.id.textActivePolls);
        textTotalVotes = findViewById(R.id.textTotalVotes);
        recentActivityRecyclerView = findViewById(R.id.recentActivityRecyclerView);
        popularPollsRecyclerView = findViewById(R.id.popularPollsRecyclerView);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Setup adapters
        recentActivityAdapter = new RecentActivityAdapter();
        popularPollsAdapter = new PopularPollsAdapter();

        // Setup RecyclerViews
        recentActivityRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        popularPollsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recentActivityRecyclerView.setAdapter(recentActivityAdapter);
        popularPollsRecyclerView.setAdapter(popularPollsAdapter);

        // Setup bottom navigation
        bottomNavigationView.setSelectedItemId(R.id.nav_dashboard);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_polls) {
                startActivity(new android.content.Intent(this, MainActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_dashboard) {
                return true;
            }
            return false;
        });

        // Load dashboard data
        loadDashboardData();
    }

    private void loadDashboardData() {
        try {
            List<Poll> allPolls = PollDatabase.getInstance(this).getAllPollsIncludingExpired();
            
            // Count active polls
            int activePolls = 0;
            for (Poll poll : allPolls) {
                if (poll.getEndTimeMillis() > System.currentTimeMillis()) {
                    activePolls++;
                }
            }
            textActivePolls.setText(String.valueOf(activePolls));

            // TODO: Implement total votes counting when voting is implemented
            textTotalVotes.setText("0");

            // Get recent polls (last 5)
            List<Poll> recentPolls = new ArrayList<>();
            for (int i = 0; i < Math.min(5, allPolls.size()); i++) {
                recentPolls.add(allPolls.get(i));
            }
            recentActivityAdapter.setPolls(recentPolls);

            // Get popular polls (most votes)
            // TODO: Sort by vote count when voting is implemented
            List<Poll> popularPolls = new ArrayList<>(allPolls);
            popularPolls.sort(Comparator.comparingLong(Poll::getId).reversed());
            popularPolls = popularPolls.subList(0, Math.min(5, popularPolls.size()));
            popularPollsAdapter.setPolls(popularPolls);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDashboardData();
    }
} 