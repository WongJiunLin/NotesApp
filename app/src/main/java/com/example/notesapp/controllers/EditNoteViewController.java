package com.example.notesapp.controllers;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.notesapp.R;
import com.example.notesapp.components.CustomConfirmationDialogFragment;
import com.example.notesapp.enums.DialogTypeEnum;
import com.example.notesapp.models.Category;
import com.example.notesapp.models.Note;
import com.example.notesapp.utils.Utility;

public class EditNoteViewController extends AbstractNoteViewController{

    private Note curNote;
    private Category curSelectedCategory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.btnNegative.setVisibility(View.VISIBLE);
        binding.btnPositive.setText(getResources().getString(R.string.STR_UPDATE));
        binding.tvNoteDetailTile.setText(getResources().getString(R.string.STR_EDIT_NOTE));

        curNote = (Note) getIntent().getSerializableExtra("selectedNote");
        if (curNote != null) {
            curSelectedCategory = categoryManager.getCategoryById(curNote.getCategoryId());
            updateSelectedCategory(curSelectedCategory, false);
        }
        if (curNote != null && curSelectedCategory != null)
        {
            categories = categoryManager.getAllCategories(false);
            setupCategoryList(categories);
            updateUI();
        }

    }

    @Override
    protected void onSaveOrUpdateNote() {
        if (inputFieldValidation()) {
            Note note = new Note(
                    curNote.getNoteId(),
                    noteDesc,
                    selectedCategoryId
            );
            long status = noteManager.updateNote(note);
            if (status != -1) {
                Toast.makeText(this, "Successfully update note", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update current note with status - "+status, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void deleteNote() {
        super.deleteNote();
        CustomConfirmationDialogFragment dialogFragment = new CustomConfirmationDialogFragment(() -> {
            deleteCurrentNote();
        }, getResources().getString(R.string.STR_SURE_TO_DELETE_NOTE), DialogTypeEnum.ALERT);
        dialogFragment.show(getSupportFragmentManager(), "DeleteNoteConfirmationDialog");
    }

    /**
     * This method is used to update the Edit Note UI layout from abstract class
     */
    private void updateUI() {
        Utility.getInstance().updateImageViewIcon(curSelectedCategory.getCategoryIcon(), binding.ivCategoryIcon);
        binding.ivCategoryIcon.setVisibility(View.VISIBLE);
        binding.tvSelectedCategoryName.setText(curSelectedCategory.getCategoryName());
        binding.edtNoteDesc.setText(curNote.getNoteDesc());
    }

    private void deleteCurrentNote() {
        noteManager.deleteNoteById(curNote.getNoteId());
        Toast.makeText(this, "Note has been deleted", Toast.LENGTH_SHORT).show();
        finish();
    }
}
