package com.example.voting;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.voting.data.db.PollDatabase;
import com.example.voting.data.model.Poll;
import org.json.JSONArray;

public class CreatePollActivity extends AppCompatActivity {
    private TextInputEditText editTextTitle, editTextDescription, editTextEndTime;
    private LinearLayout optionsContainer;
    private MaterialButton buttonAddOption, buttonSavePoll;
    private final List<EditText> optionEditTexts = new ArrayList<>();
    private long endTimeMillis = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poll);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextEndTime = findViewById(R.id.editTextEndTime);
        optionsContainer = findViewById(R.id.optionsContainer);
        buttonAddOption = findViewById(R.id.buttonAddOption);
        buttonSavePoll = findViewById(R.id.buttonSavePoll);

        // Add two default options
        addOptionField(null);
        addOptionField(null);

        buttonAddOption.setOnClickListener(v -> addOptionField(null));
        editTextEndTime.setOnClickListener(v -> showDateTimePicker());
        buttonSavePoll.setOnClickListener(v -> savePoll());
    }

    private void addOptionField(String text) {
        LinearLayout optionLayout = new LinearLayout(this);
        optionLayout.setOrientation(LinearLayout.HORIZONTAL);
        optionLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        EditText optionEditText = new EditText(this);
        optionEditText.setHint("Option");
        optionEditText.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        if (text != null) optionEditText.setText(text);
        optionLayout.addView(optionEditText);

        MaterialButton removeButton = new MaterialButton(this, null, com.google.android.material.R.attr.materialButtonOutlinedStyle);
        removeButton.setIconResource(android.R.drawable.ic_menu_close_clear_cancel);
        removeButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        removeButton.setOnClickListener(v -> {
            optionsContainer.removeView(optionLayout);
            optionEditTexts.remove(optionEditText);
        });
        optionLayout.addView(removeButton);

        optionsContainer.addView(optionLayout);
        optionEditTexts.add(optionEditText);
    }

    private void showDateTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                endTimeMillis = calendar.getTimeInMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                editTextEndTime.setText(sdf.format(new Date(endTimeMillis)));
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
            timePickerDialog.show();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void savePoll() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        List<String> options = new ArrayList<>();
        for (EditText et : optionEditTexts) {
            String opt = et.getText().toString().trim();
            if (!TextUtils.isEmpty(opt)) options.add(opt);
        }
        // Remove duplicate options
        List<String> uniqueOptions = new ArrayList<>();
        for (String opt : options) {
            if (!uniqueOptions.contains(opt)) uniqueOptions.add(opt);
        }
        if (TextUtils.isEmpty(title)) {
            editTextTitle.setError("Title required");
            return;
        }
        if (uniqueOptions.size() < 2) {
            Snackbar.make(buttonSavePoll, "At least 2 unique, non-empty options required", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (endTimeMillis == 0) {
            editTextEndTime.setError("End time required");
            return;
        }
        try {
            JSONArray optionsArray = new JSONArray(uniqueOptions);
            Poll poll = new Poll(title, description, optionsArray.toString(), endTimeMillis);
            PollDatabase.getInstance(this).insertPoll(poll);
            // Clear fields
            editTextTitle.setText("");
            editTextDescription.setText("");
            editTextEndTime.setText("");
            for (EditText et : optionEditTexts) et.setText("");
            Snackbar.make(buttonSavePoll, "Poll created!", Snackbar.LENGTH_SHORT).show();
            buttonSavePoll.postDelayed(this::finish, 800); // Return to home after short delay
        } catch (Exception e) {
            Snackbar.make(buttonSavePoll, "Failed to save poll", Snackbar.LENGTH_SHORT).show();
        }
    }
} 