package com.example.notesapp.database.managers;

import android.database.sqlite.SQLiteDatabase;

import com.example.notesapp.database.dao.NoteDAO;
import com.example.notesapp.models.Note;

import java.util.List;

public class NoteManager {
    private NoteDAO noteDAO;

    private static NoteManager noteManager;

    public static synchronized NoteManager getInstance(SQLiteDatabase db) {
        if (noteManager == null)
            noteManager = new NoteManager(db);
        return noteManager;
    }

    public static synchronized void resetInstance() {
        noteManager = null;
    }

    public NoteManager(SQLiteDatabase db) {
        noteDAO = new NoteDAO(db);
    }

    public List<Note> getNotesByCategoryId(long categoryId) {
        List<Note> notes = noteDAO.getNotesByCategory(categoryId, false);
        return notes;
    }

    public long updateNote(Note note) {
        return noteDAO.update(note);
    }

    public long addNote(Note note) {
        return noteDAO.insert(note);
    }

    public void deleteNoteById(long noteId) {
        noteDAO.delete(noteId);
    }

    public void deleteAllNotes(){
        noteDAO.deleteAllNotes();
    }

    public void deleteNotesByCategoryId(long categoryId) {
        noteDAO.deleteNotesByCategoryId(categoryId);
    }
}
