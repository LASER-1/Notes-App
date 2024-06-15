package com.example.notes.model;

import android.content.Context;

public class DatabaseManager {
    private static DatabaseManager instance;
    private final NoteDbHelper noteDbHelper;

    private DatabaseManager(Context context) {
        noteDbHelper = new NoteDbHelper(context);
    }

    public static synchronized DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context.getApplicationContext());
        }
        return instance;
    }

    public NoteDbHelper getNoteDbHelper() {
        return noteDbHelper;
    }
}