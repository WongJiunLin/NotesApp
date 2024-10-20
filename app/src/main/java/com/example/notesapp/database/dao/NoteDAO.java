package com.example.notesapp.database.dao;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.notesapp.models.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteDAO {

    private static final String tag = "NoteDAO";
    public static final String TABLE_NAME = "ST_NOTES";
    public static final String COLUMN_NOTE_ID = "INT_NOTE_ID";
    public static final String COLUMN_NOTE_DESCRIPTION = "STR_NOTE_DESC";
    public static final String COLUMN_CATEGORY_ID = "INT_CATEGORY_ID";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NOTE_DESCRIPTION + " TEXT,"
                    + COLUMN_CATEGORY_ID + " INTEGER,"
                    + "DT_CREATED TIMESTAMP DEFAULT CURRENT_TIMESTAMP," // Automatically set on created
                    + "DT_UPDATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " // Automatically updated
                    + "FOREIGN KEY (" + COLUMN_CATEGORY_ID + ") REFERENCES ST_CATEGORY(INT_CATEGORY_ID)"
                    + ")";

    private SQLiteDatabase db;

    public NoteDAO(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * This method is used to insert note data into SQLite database
     * @param note
     * @return
     */
    public long insert(Note note) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_DESCRIPTION, note.getNoteDesc());
        values.put(COLUMN_CATEGORY_ID, note.getCategoryId());
        return db.insert(TABLE_NAME, null, values);
    }

    @SuppressLint("Range")
    public List<Note> getNotesByCategory(long categoryId, boolean latestThreeNotes) {
        List<Note> notes = new ArrayList<Note>();
        try {
            Cursor cursor = db.query(TABLE_NAME, null, COLUMN_CATEGORY_ID +
                    "=?", new String[]{String.valueOf(categoryId)}, null, null, "DT_UPDATE DESC");
            if (cursor.moveToFirst()) {
                do {
                    Note note = new Note(
                            cursor.getLong(cursor.getColumnIndex(COLUMN_NOTE_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_DESCRIPTION)),
                            cursor.getLong(cursor.getColumnIndex(COLUMN_CATEGORY_ID))
                    );
                    notes.add(note);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(tag,"getNotesByCategory - Exception occurred: " + e.getMessage());
        }
        return latestThreeNotes && notes.size() > 3 ? notes.subList(0, 3) : notes;
    }

    public int update(Note note) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_DESCRIPTION, note.getNoteDesc());
        values.put(COLUMN_CATEGORY_ID, note.getCategoryId());
        values.put("DT_UPDATE",System.currentTimeMillis());
        return db.update(TABLE_NAME, values, COLUMN_NOTE_ID + "=?", new String[]{String.valueOf(note.getNoteId())});
    }

    public void delete(long id) {
        db.delete(TABLE_NAME, COLUMN_NOTE_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteAllNotes() {
        db.delete(TABLE_NAME, null, null);
    }

    public void deleteNotesByCategoryId(long categoryId) {
        db.delete(TABLE_NAME, COLUMN_CATEGORY_ID + "=?", new String[]{String.valueOf(categoryId)});
    }

    @SuppressLint("Range")
    public Note getNoteById(long id){
        Note note = null;
        try {
            Cursor cursor = db.query(TABLE_NAME, null, COLUMN_NOTE_ID + "=?", new String[]{String.valueOf(id)},
                    null, null, null);
            if (cursor.moveToFirst()) {
                note = new Note(
                        id,
                        cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_DESCRIPTION)),
                        cursor.getLong(cursor.getColumnIndex(COLUMN_CATEGORY_ID))
                );
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(tag, "getNoteById - Exception occurred: " + e.getMessage());
        }
        return note;
    }
}
