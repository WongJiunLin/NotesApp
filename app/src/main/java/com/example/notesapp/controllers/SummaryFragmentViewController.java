package com.example.notesapp.controllers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.notesapp.adapters.CategoryAdapter;
import com.example.notesapp.database.controllers.DatabaseController;
import com.example.notesapp.database.managers.CategoryManager;
import com.example.notesapp.database.managers.NoteManager;
import com.example.notesapp.databinding.FragmentSummaryBinding;
import com.example.notesapp.models.Category;
import com.example.notesapp.utils.GlobalVariables;

import java.util.List;

public class SummaryFragmentViewController extends Fragment {

    private static String tag = "SummaryFragmentViewController";
    private FragmentSummaryBinding binding;

    private NoteManager noteManager;
    private CategoryManager categoryManager;
    private CategoryAdapter categoryAdapter;

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

        List<Category> categories = categoryManager.getAllCategories(false);

        categoryAdapter = new CategoryAdapter(categories, noteManager);
        binding.rvCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvCategories.setAdapter(categoryAdapter);

        return binding.getRoot();
    }

    private void reloadCategories() {
        List<Category> categories = categoryManager.getAllCategories(false);
        categoryAdapter.updateCategories(categories);
    }
}
