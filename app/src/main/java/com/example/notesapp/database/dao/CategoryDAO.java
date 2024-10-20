package com.example.notesapp.database.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.notesapp.models.Category;
import com.example.notesapp.models.Note;

import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    private static final String tag = "CategoryDAO";
    public static final String TABLE_NAME = "ST_CATEGORY";
    public static final String COLUMN_CATEGORY_ID = "INT_CATEGORY_ID";
    public static final String COLUMN_CATEGORY_NAME = "STR_CATEGORY_NAME";
    public static final String COLUMN_CATEGORY_ICON = "STR_CATEGORY_ICON";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_CATEGORY_NAME + " TEXT,"
                    + COLUMN_CATEGORY_ICON + " TEXT,"
                    + "DT_CREATED TIMESTAMP DEFAULT CURRENT_TIMESTAMP," // Automatically set on created
                    + "DT_UPDATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP" // Automatically updated
                    + ")";

    private SQLiteDatabase db;

    public CategoryDAO(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * This method is used to insert category data into SQLite database
     * @param category
     * @return
     */
    public long insert(Category category) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, category.getCategoryName());
        values.put(COLUMN_CATEGORY_ICON, category.getCategoryIcon());
        return db.insert(TABLE_NAME, null, values);
    }


    public int update(Category category) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, category.getCategoryName());
        values.put(COLUMN_CATEGORY_ICON, category.getCategoryIcon());
        return db.update(TABLE_NAME, values, COLUMN_CATEGORY_ID + "=?", new String[]{String.valueOf(category.getCategoryId())});
    }

    public void deleteByCategoryId(long id) {
        db.delete(TABLE_NAME, COLUMN_CATEGORY_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteAll() {
        db.delete(TABLE_NAME, null, null);
    }

    @SuppressLint("Range")
    public Category getCategoryById(long id, List<Note> notes){
        Category category = null;
        try {
            Cursor cursor = db.query(TABLE_NAME, null, COLUMN_CATEGORY_ID + "=?", new String[]{String.valueOf(id)},
                    null, null, null);
            if (cursor.moveToFirst()) {
                category = new Category(
                        id,
                        cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_ICON)),
                        false,
                        notes
                );
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(tag, "getNoteById - Exception occurred: " + e.getMessage());
        }
        return category;
    }

    @SuppressLint("Range")
    public Category getCategoryByName(String categoryName, List<Note> notes) {
        Category category = null;
        try {
            Cursor cursor = db.query(TABLE_NAME, null, COLUMN_CATEGORY_NAME + "=?", new String[]{categoryName}, null, null, null);
            if (cursor.moveToFirst()) {
                category = new Category(
                        cursor.getLong(cursor.getColumnIndex(COLUMN_CATEGORY_ID)),
                        categoryName,
                        cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_ICON)),
                        false,
                        notes
                );
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(tag, "getCategoryByName - Exception occurred: " + e.getMessage());
        }
        return category;
    }

    @SuppressLint("Range")
    public List<Category> getAllCategories(NoteDAO noteDAO, boolean latestThreeNotes) {
        List<Category> categories = new ArrayList<Category>();

        try {
            Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    long categoryId = cursor.getLong(cursor.getColumnIndex(COLUMN_CATEGORY_ID));
                    String categoryName = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME));
                    String categoryIcon = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_ICON));
                    List<Note> notes = noteDAO.getNotesByCategory(categoryId, latestThreeNotes);

                    Category category = new Category(categoryId, categoryName, categoryIcon, false, notes);
                    categories.add(category);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(tag, "getAllCategories - Exception occurred: " + e.getMessage());
        }
        return categories;
    }
}
