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
import com.example.notesapp.adapters.NotesAdapter;
import com.example.notesapp.database.controllers.DatabaseController;
import com.example.notesapp.database.managers.CategoryManager;
import com.example.notesapp.database.managers.NoteManager;
import com.example.notesapp.databinding.FragmentHomeBinding;
import com.example.notesapp.models.Category;
import com.example.notesapp.utils.GlobalVariables;

import java.util.List;

public class HomeFragmentViewController extends Fragment implements View.OnClickListener {
    private static String tag = "HomeFragmentViewController";
    private FragmentHomeBinding binding;

    private NoteManager noteManager;
    private CategoryManager categoryManager;

    private NotesAdapter notesAdapter;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);

        List<Category> categories = categoryManager.getAllCategories(true);

        notesAdapter = new NotesAdapter(categories);
        binding.rvNotes.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvNotes.setAdapter(notesAdapter);

        binding.ivSettings.setOnClickListener(this);

        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_settings) {
            Intent intent = new Intent(GlobalVariables.getInstance().getMainActivity(), SettingsViewController.class);
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadNotes();
    }

    private void reloadNotes() {
        List<Category> categories = categoryManager.getAllCategories(true);
        notesAdapter.updateCategories(categories);
    }
}
