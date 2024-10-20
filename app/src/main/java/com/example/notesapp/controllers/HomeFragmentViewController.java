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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);

        categories = categoryManager.getAllCategories(true);

        if (!categories.isEmpty()) {
            notesAdapter = new NotesAdapter(categories);
            binding.rvNotes.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rvNotes.setAdapter(notesAdapter);
        } else {
            binding.tvNoCategory.setVisibility(View.VISIBLE);
        }

        binding.ivSettings.setOnClickListener(this);
        binding.tvNoCategory.setOnClickListener(this);

        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_settings || v.getId() == R.id.tv_no_category) {
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
        GlobalVariables.getHandlerUI().post(new Runnable() {
            @Override
            public void run() {
                if (categories.isEmpty()) {
                    binding.tvNoCategory.setVisibility(View.VISIBLE);
                    return;
                }
                List<Category> categories = categoryManager.getAllCategories(true);
                notesAdapter.updateCategories(categories);
                binding.tvNoCategory.setVisibility(View.GONE);
            }
        });
    }
}
