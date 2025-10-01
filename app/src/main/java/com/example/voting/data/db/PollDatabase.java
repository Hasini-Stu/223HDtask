package com.example.voting.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

import com.example.voting.data.model.Poll;

public class PollDatabase extends SQLiteOpenHelper {
    private static final String DB_NAME = "polls.db";
    private static final int DB_VERSION = 1;
    private static PollDatabase instance;

    public static synchronized PollDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new PollDatabase(context.getApplicationContext());
        }
        return instance;
    }

    private PollDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE polls (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "description TEXT, " +
                "optionsJson TEXT, " +
                "endTimeMillis INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS polls");
        onCreate(db);
    }

    public long insertPoll(Poll poll) {
        ContentValues values = new ContentValues();
        values.put("title", poll.getTitle());
        values.put("description", poll.getDescription());
        values.put("optionsJson", poll.getOptionsJson());
        values.put("endTimeMillis", poll.getEndTimeMillis());
        return getWritableDatabase().insert("polls", null, values);
    }

    public List<Poll> getAllPolls() {
        List<Poll> polls = new ArrayList<>();
        long now = System.currentTimeMillis();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM polls WHERE endTimeMillis > ? ORDER BY endTimeMillis ASC", new String[]{String.valueOf(now)});
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String optionsJson = cursor.getString(cursor.getColumnIndexOrThrow("optionsJson"));
                long endTimeMillis = cursor.getLong(cursor.getColumnIndexOrThrow("endTimeMillis"));
                polls.add(new Poll(id, title, description, optionsJson, endTimeMillis));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return polls;
    }

    public List<Poll> getAllPollsIncludingExpired() {
        List<Poll> polls = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM polls ORDER BY id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String optionsJson = cursor.getString(cursor.getColumnIndexOrThrow("optionsJson"));
                long endTimeMillis = cursor.getLong(cursor.getColumnIndexOrThrow("endTimeMillis"));
                polls.add(new Poll(id, title, description, optionsJson, endTimeMillis));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return polls;
    }

    public void deletePoll(long pollId) {
        getWritableDatabase().delete("polls", "id=?", new String[]{String.valueOf(pollId)});
    }
} 