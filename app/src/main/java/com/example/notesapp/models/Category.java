package com.example.notesapp.models;

import java.io.Serializable;
import java.util.List;

public class Category implements Serializable {
    private long categoryId;
    private String categoryName;
    private String categoryIcon;
    private boolean isExpanded;
    private List<Note> notes;

    public Category(long categoryId, String categoryName, String categoryIcon, boolean isExpanded, List<Note> notes) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryIcon = categoryIcon;
        this.isExpanded = isExpanded;
        this.notes = notes;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
}
