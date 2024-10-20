package com.example.notesapp.models;

import java.io.Serializable;

public class Note implements Serializable {

    private long noteId;
    private String noteDesc;
    private long categoryId;

    public Note(long noteId, String noteDesc, long categoryId) {
        this.noteId = noteId;
        this.noteDesc = noteDesc;
        this.categoryId = categoryId;
    }

    public long getNoteId() {
        return noteId;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }

    public String getNoteDesc() {
        return noteDesc;
    }

    public void setNoteDesc(String noteDesc) {
        this.noteDesc = noteDesc;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }
}
