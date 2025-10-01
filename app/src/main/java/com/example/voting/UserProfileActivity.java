package com.example.voting;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class UserProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // TODO: Load user details and set avatar, name, etc.
        ImageView avatarImage = findViewById(R.id.avatarImage);
        TextView userName = findViewById(R.id.userName);
        // Set dummy data for now
        userName.setText("Nivekz");
        avatarImage.setImageResource(R.drawable.ic_avatar_placeholder);

        // Set icons and texts for profile options
        setProfileOption(R.id.optionAccount, R.drawable.ic_account, R.string.profile_account);
        setProfileOption(R.id.optionPreferences, R.drawable.ic_settings, R.string.profile_preferences);
        setProfileOption(R.id.optionTip, R.drawable.ic_lightbulb, R.string.profile_tip);
        setProfileOption(R.id.optionHelp, R.drawable.ic_help, R.string.profile_help);
        setProfileOption(R.id.optionArchivedPolls, R.drawable.ic_archive, R.string.profile_archived_polls);
        setProfileOption(R.id.optionPollHistory, R.drawable.ic_history, R.string.profile_poll_history);
        setProfileOption(R.id.optionArchivedQuestionnaires, R.drawable.ic_archive, R.string.profile_archived_questionnaires);
        setProfileOption(R.id.optionQuestionnaireHistory, R.drawable.ic_history, R.string.profile_questionnaire_history);

        View premiumCard = findViewById(R.id.premiumCard);
        if (premiumCard != null) {
            premiumCard.setOnClickListener(v -> {
                startActivity(new android.content.Intent(this, PremiumSubscriptionActivity.class));
            });
        }
    }

    private void setProfileOption(int includeId, int iconRes, int textRes) {
        View option = findViewById(includeId);
        if (option != null) {
            ImageView icon = option.findViewById(R.id.optionIcon);
            TextView text = option.findViewById(R.id.optionText);
            if (icon != null) icon.setImageResource(iconRes);
            if (text != null) text.setText(textRes);
        }
    }
} 