package com.example.notesapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.notesapp.R;
import com.example.notesapp.adapters.CategoryAdapter;
import com.example.notesapp.database.controllers.DatabaseController;
import com.example.notesapp.database.managers.CategoryManager;
import com.example.notesapp.database.managers.NoteManager;
import com.example.notesapp.databinding.FragmentSummaryBinding;
import com.example.notesapp.models.Category;
import com.example.notesapp.utils.GlobalVariables;

import java.util.List;

public class SummaryFragmentViewController extends Fragment implements View.OnClickListener {

    private static String tag = "SummaryFragmentViewController";
    private FragmentSummaryBinding binding;

    private NoteManager noteManager;
    private CategoryManager categoryManager;
    private CategoryAdapter categoryAdapter;

    List<Category> categories;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        noteManager = NoteManager.getInstance(DatabaseController.getInstance(GlobalVariables.getInstance().getMainActivity().getApplicationContext()).getWritableDatabase());
        categoryManager = CategoryManager.getInstance(DatabaseController.getInstance(GlobalVariables.getInstance().getMainActivity().getApplicationContext()).getWritableDatabase());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadCategories();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSummaryBinding.inflate(getLayoutInflater(), container, false);

        // Setup click listeners
        binding.tvNoCategory.setOnClickListener(this);

        categories = categoryManager.getAllCategories(false);
        if (!categories.isEmpty()) {
            categoryAdapter = new CategoryAdapter(categories, noteManager);
            binding.rvCategories.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rvCategories.setAdapter(categoryAdapter);
            binding.tvNoCategory.setVisibility(View.GONE);
        } else {
            binding.tvNoCategory.setVisibility(View.VISIBLE);
        }

        return binding.getRoot();
    }

    private void reloadCategories() {
        categories = categoryManager.getAllCategories(false);
        if (!categories.isEmpty()) {
            categoryAdapter.updateCategories(categories);
            binding.tvNoCategory.setVisibility(View.GONE);
        } else {
            binding.tvNoCategory.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_no_category) {
            Intent intent = new Intent(getContext(), ManageCategoryViewController.class);
            startActivity(intent);
        }
    }
}
