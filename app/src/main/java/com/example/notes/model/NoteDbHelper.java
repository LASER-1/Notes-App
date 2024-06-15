package com.example.notes.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.notes.SharedPreferencesManager;


import java.util.ArrayList;

public class NoteDbHelper extends SQLiteOpenHelper {
    private static final String name = "notes.db";
    private static final int version = 1;
    private static final String TABLE_NAME = "notes";
    public NoteDbHelper(@Nullable Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_NAME + "(" +
                        "COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "COLUMN_TITLE TEXT, COLUMN_DESCRIPTION TEXT," +
                        "COLUMN_EMAIL TEXT)"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
    public long insertNote(Note note){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("COLUMN_TITLE",note.getTitle());
        values.put("COLUMN_DESCRIPTION",note.getDescription());
        values.put("COLUMN_EMAIL",note.getEmail());
        long newRowId = db.insert(TABLE_NAME,null,values);
        if(newRowId == -1) {
            Log.e("NoteDbHelper", "Error Inserting note");
        }
        return newRowId;
    }
    public ArrayList<Note> getAllNotes(Context context){
        String email1 = retrieveUserEmail(context);
        ArrayList<Note> notes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();


        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE COLUMN_EMAIL = ?", new String[]{email1});

        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("COLUMN_ID"));
            String title = cursor.getString(cursor.getColumnIndexOrThrow("COLUMN_TITLE"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("COLUMN_DESCRIPTION"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("COLUMN_EMAIL"));
            Note note = new Note(id,title,description,email);
            notes.add(note);
        }

        cursor.close();
        return notes;
    }
    public Note getNoteByID(int noteId){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE COLUMN_ID = " + noteId,null);
        cursor.moveToFirst();
        String title = cursor.getString(cursor.getColumnIndexOrThrow("COLUMN_TITLE"));
        String description = cursor.getString(cursor.getColumnIndexOrThrow("COLUMN_DESCRIPTION"));
        cursor.close();
        return new Note(noteId,title,description);
    }
    public void updateNote(Note note){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("COLUMN_TITLE",note.getTitle());
        values.put("COLUMN_DESCRIPTION",note.getDescription());
        db.update(TABLE_NAME,values,"COLUMN_ID = " + note.getId(),null);
    }
    public void deleteNote(int noteId){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME,"COLUMN_ID = " + noteId,null);
    }
    private String retrieveUserEmail(Context context){
        SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager.getInstance(context);
        return sharedPreferencesManager.getString("email",null);
    }
}
