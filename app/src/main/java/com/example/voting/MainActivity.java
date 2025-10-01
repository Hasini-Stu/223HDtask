package com.example.voting;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.example.voting.data.model.User;
import com.example.voting.data.model.Vote;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.example.voting.data.db.PollDatabase;
import com.example.voting.data.model.Poll;
import com.example.voting.ui.PollAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fabAdd;
    private FloatingActionButton fabCreate;
    private FirebaseAuth firebaseAuth;
    private User currentUser;
    private RecyclerView pollsRecyclerView;
    private PollAdapter pollAdapter;
    private View centerContent;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize views
        fabAdd = findViewById(R.id.fabAdd);
        fabCreate = findViewById(R.id.fabCreate);
        pollsRecyclerView = findViewById(R.id.pollsRecyclerView);
        centerContent = findViewById(R.id.centerContent);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Setup RecyclerView
        pollsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pollAdapter = new PollAdapter(new ArrayList<>(), poll -> {
            // TODO: Launch poll details/voting activity
            Toast.makeText(this, "Clicked: " + poll.getTitle(), Toast.LENGTH_SHORT).show();
        });
        pollsRecyclerView.setAdapter(pollAdapter);

        // Setup swipe-to-delete
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Poll pollToDelete = pollAdapter.getPollAt(position);
                pollAdapter.removePollAt(position);
                PollDatabase.getInstance(MainActivity.this).deletePoll(pollToDelete.getId());
                Snackbar.make(pollsRecyclerView, "Poll deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo", v -> {
                            PollDatabase.getInstance(MainActivity.this).insertPoll(pollToDelete);
                            loadPolls();
                        }).show();
            }
        });
        itemTouchHelper.attachToRecyclerView(pollsRecyclerView);

        // Setup add poll FABs
        View.OnClickListener createPollListener = v -> {
            if (currentUser != null && currentUser.isAdmin()) {
                startActivity(new Intent(this, CreatePollActivity.class));
            } else {
                Toast.makeText(this, "Only admins can create polls", Toast.LENGTH_SHORT).show();
            }
        };
        if (fabAdd != null) fabAdd.setOnClickListener(createPollListener);
        if (fabCreate != null) fabCreate.setOnClickListener(createPollListener);

        // Check authentication
        checkAuthentication();

        // Setup BottomNavigationView
        bottomNavigationView.setSelectedItemId(R.id.nav_polls);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_dashboard) {
                startActivity(new Intent(this, DashboardActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_polls) {
                return true;
            }
            return false;
        });

        ImageView avatarImage = findViewById(R.id.avatarImage);
        avatarImage.setOnClickListener(v -> {
            startActivity(new Intent(this, UserProfileActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPolls();
    }

    private void checkAuthentication() {
        if (firebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            loadUserData();
        }
    }

    private void loadUserData() {
        // TODO: Implement user data loading from Firebase
        // For now, create a dummy admin user
        currentUser = new User(
            firebaseAuth.getCurrentUser().getUid(),
            firebaseAuth.getCurrentUser().getEmail(),
            "Test User",
            true,
            "en"
        );
    }

    private void loadPolls() {
        try {
            List<Poll> polls = PollDatabase.getInstance(this).getAllPolls();
            pollAdapter.setPolls(polls);
            updateVisibility(polls.isEmpty());
        } catch (Exception e) {
            Toast.makeText(this, "Error loading polls: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            updateVisibility(true);
        }
    }

    private void updateVisibility(boolean showEmptyState) {
        if (showEmptyState) {
            centerContent.setVisibility(View.VISIBLE);
            pollsRecyclerView.setVisibility(View.GONE);
        } else {
            centerContent.setVisibility(View.GONE);
            pollsRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            firebaseAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}