package com.example.notesapp.controllers;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.notesapp.R;
import com.example.notesapp.models.Note;

public class NewNoteViewController extends AbstractNoteViewController{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.btnNegative.setVisibility(View.GONE);
        binding.btnPositive.setText(getResources().getString(R.string.STR_SAVE));
        binding.tvNoteDetailTile.setText(getResources().getString(R.string.STR_NEW_NOTE));

        categories = categoryManager.getAllCategories(false);
        setupCategoryList(categories);
    }

    @Override
    protected void onSaveOrUpdateNote() {
        if (inputFieldValidation()) {
            Note note = new Note(
                    0,
                    noteDesc,
                    selectedCategoryId
            );

            long status = noteManager.addNote(note);
            if (status != -1) {
                Toast.makeText(this, "Successfully added new note", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Fail to add current note with status - "+status, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
