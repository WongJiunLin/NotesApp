package com.example.notesapp.controllers;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.notesapp.R;
import com.example.notesapp.adapters.CategoryDetailNotesAdapter;
import com.example.notesapp.components.CustomConfirmationDialogFragment;
import com.example.notesapp.database.controllers.DatabaseController;
import com.example.notesapp.database.managers.CategoryManager;
import com.example.notesapp.database.managers.NoteManager;
import com.example.notesapp.databinding.ActivityCategoryDetailBinding;
import com.example.notesapp.enums.DialogTypeEnum;
import com.example.notesapp.models.Category;
import com.example.notesapp.models.Note;
import com.example.notesapp.utils.GlobalVariables;
import com.example.notesapp.utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class CategoryDetailViewController extends AppCompatActivity implements View.OnClickListener {
    private static String tag = "CategoryDetailViewController";
    private ActivityCategoryDetailBinding binding;
    private Category curCategory;
    private NoteManager noteManager;
    private CategoryManager categoryManager;
    private CategoryDetailNotesAdapter categoryDetailNotesAdapter;

    private List<Note> curCategoryNotes = new ArrayList<Note>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCategoryDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ivBackBtn.setOnClickListener(this);
        binding.btnNegative.setOnClickListener(this);

        noteManager = NoteManager.getInstance(DatabaseController.getInstance(getApplicationContext()).getWritableDatabase());
        categoryManager = CategoryManager.getInstance(DatabaseController.getInstance(getApplicationContext()).getWritableDatabase());

        curCategory = (Category) getIntent().getSerializableExtra("selectedCategory");
        if (curCategory != null) {
            binding.tvCategoryName.setText(curCategory.getCategoryName());
            Utility.getInstance().updateImageViewIcon(curCategory.getCategoryIcon(), binding.ivCategoryIcon);
            binding.ivCategoryIcon.setColorFilter(GlobalVariables.getInstance().getDefaultButtonColor());
            curCategoryNotes = noteManager.getNotesByCategoryId(curCategory.getCategoryId());
        }

        if (!curCategoryNotes.isEmpty()) {
            categoryDetailNotesAdapter = new CategoryDetailNotesAdapter(curCategoryNotes);
            binding.rvNotes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            binding.rvNotes.setAdapter(categoryDetailNotesAdapter);
        } else {
            binding.tvNoNote.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back_btn) {
            finish();
            return;
        }
        if (v.getId() == R.id.btn_negative) {
            displayDialog();
        }
    }

    private void displayDialog() {
        CustomConfirmationDialogFragment dialogFragment = new CustomConfirmationDialogFragment(() -> {
            deleteCurrentCategoryAndNotes();
        }, getResources().getString(R.string.STR_SURE_TO_DELETE_ALL_NOTES_IN_CURRENT_CATEGORY), DialogTypeEnum.ALERT);
        dialogFragment.show(getSupportFragmentManager(), "DeleteCurrentCategoryAndNoteConfirmationDialog");
    }

    private void deleteCurrentCategoryAndNotes() {
        noteManager.deleteNotesByCategoryId(curCategory.getCategoryId());
        categoryManager.deleteByCategoryId(curCategory.getCategoryId());
        Toast.makeText(this, "You have deleted " + curCategory.getCategoryName() + " category", Toast.LENGTH_SHORT).show();
        finish();
    }
}
