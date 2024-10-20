package com.example.notesapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notesapp.R;
import com.example.notesapp.database.controllers.DatabaseController;
import com.example.notesapp.database.managers.CategoryManager;
import com.example.notesapp.database.managers.NoteManager;
import com.example.notesapp.databinding.AbstractActivityNoteDetailBinding;
import com.example.notesapp.databinding.ItemCategoryBinding;
import com.example.notesapp.models.Category;
import com.example.notesapp.models.Note;
import com.example.notesapp.utils.GlobalVariables;
import com.example.notesapp.utils.Utility;

import java.util.List;

public abstract class AbstractNoteViewController extends AppCompatActivity implements View.OnClickListener {

    protected AbstractActivityNoteDetailBinding binding;
    protected boolean categoryExpanded = false;

    protected long selectedCategoryId = -1;
    protected String noteDesc;

    protected CategoryManager categoryManager;
    protected NoteManager noteManager;
    protected List<Category> categories;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = AbstractActivityNoteDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupClickListeners();

        categoryManager = CategoryManager.getInstance(DatabaseController.getInstance(getApplicationContext()).getWritableDatabase());
        noteManager = NoteManager.getInstance(DatabaseController.getInstance(getApplicationContext()).getWritableDatabase());
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
        if (v.getId() == R.id.ll_choose_category_container) {
            hideShowCategoryOption();
            return;
        }
        if (v.getId() == R.id.tv_no_category) {
            navigateToCategoryCreation();
            return;
        }
        if (v.getId() == R.id.btn_positive) {
            onSaveOrUpdateNote();
            return;
        }
        if (v.getId() == R.id.btn_negative) {
            deleteNote();
            return;
        }
    }

    private void setupClickListeners() {
        binding.ivBackBtn.setOnClickListener(this);
        binding.llChooseCategoryContainer.setOnClickListener(this);
        binding.btnPositive.setOnClickListener(this);
        binding.btnNegative.setOnClickListener(this);
        binding.tvNoCategory.setOnClickListener(this);
    }

    protected void setupCategoryList(List<Category> categories) {
        binding.llCategoryList.removeAllViews();
        // If no categories display the alert message
        if (categories.isEmpty()) {
            binding.tvNoCategory.setVisibility(View.VISIBLE);
            binding.llCategoryList.addView(binding.tvNoCategory);
        }
        // Setup each category option
        for (Category category : categories) {
            ItemCategoryBinding itemCategoryBinding = ItemCategoryBinding.inflate(
                    LayoutInflater.from(getApplicationContext()),
                    binding.llCategoryList,
                    false
            );
            itemCategoryBinding.tvCategoryName.setText(category.getCategoryName());
            Utility.getInstance().updateImageViewIcon(category.getCategoryIcon(), itemCategoryBinding.ivCategoryIcon);
            if (category.getCategoryId() == selectedCategoryId) {
                itemCategoryBinding.ivCategoryChecked.setColorFilter(getColor(R.color.dark_green));
                itemCategoryBinding.ivCategoryChecked.setVisibility(View.VISIBLE);
            }
            itemCategoryBinding.rlCategoryItem.setOnClickListener(v -> {
                updateSelectedCategory(category, true);
                // Reload the categories list
                setupCategoryList(categories);
            });
            binding.llCategoryList.addView(itemCategoryBinding.getRoot());
        }
    }

    protected void updateSelectedCategory(Category category, boolean reloadCategoryDropDown) {
        Utility.getInstance().updateImageViewIcon(category.getCategoryIcon(), binding.ivCategoryIcon);
        binding.ivCategoryIcon.setVisibility(View.VISIBLE);
        binding.tvSelectedCategoryName.setText(category.getCategoryName());
        selectedCategoryId = category.getCategoryId();
        if (reloadCategoryDropDown)
            hideShowCategoryOption();
    }

    private void navigateToCategoryCreation() {
        Intent intent = new Intent(this, ManageCategoryViewController.class);
        startActivity(intent);
    }

    private void hideShowCategoryOption() {
        if (!categoryExpanded) {
            // Expand the category list
            categoryExpanded = true;
            binding.ivCategoryDropdown.setImageResource(R.drawable.ic_up_arrow);
            binding.llCategoryList.setVisibility(View.VISIBLE);
        } else {
            // Collapse the category list
            categoryExpanded = false;
            binding.ivCategoryDropdown.setImageResource(R.drawable.ic_down_arrow);
            binding.llCategoryList.setVisibility(View.GONE);
        }
    }

    protected boolean inputFieldValidation() {
        noteDesc = binding.edtNoteDesc.getText().toString();
        // Check if Category is selected
        if (selectedCategoryId == -1) {
            Toast.makeText(this, "Please choose a category", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Check description input
        if (noteDesc.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please fill in the description", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    protected void deleteNote() {

    }

    protected abstract void onSaveOrUpdateNote();
}
