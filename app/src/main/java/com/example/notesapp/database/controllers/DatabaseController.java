package com.example.notesapp.database.controllers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.notesapp.database.dao.CategoryDAO;
import com.example.notesapp.database.dao.NoteDAO;

public class DatabaseController extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notes_app_db";
    private static final int DATABASE_VERSION = 1;

    private static DatabaseController dbController;

    private DatabaseController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseController getInstance(Context context) {
        if (dbController == null)
            dbController = new DatabaseController(context);
        return dbController;
    }

    public static synchronized void resetInstance() {
        dbController = null;
    }

    public synchronized SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tables creation
        db.execSQL(CategoryDAO.CREATE_TABLE);
        db.execSQL(NoteDAO.CREATE_TABLE);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NoteDAO.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CategoryDAO.TABLE_NAME);
        onCreate(db);
    }
}
