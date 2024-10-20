package com.example.notesapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.notesapp.R;
import com.example.notesapp.adapters.CategoryAdapter;
import com.example.notesapp.adapters.MenuAdapter;
import com.example.notesapp.components.CustomConfirmationDialogFragment;
import com.example.notesapp.database.controllers.DatabaseController;
import com.example.notesapp.database.managers.NoteManager;
import com.example.notesapp.databinding.ActivitySettingsBinding;
import com.example.notesapp.enums.DialogTypeEnum;
import com.example.notesapp.models.Menu;
import com.example.notesapp.utils.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class SettingsViewController extends AppCompatActivity implements View.OnClickListener {
    private static String tag = "SettingsViewController";
    private ActivitySettingsBinding binding;

    private NoteManager noteManager;
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ivBackBtn.setOnClickListener(this);
        binding.btnCreateNewCategory.setOnClickListener(this);
        binding.btnDeleteAllNotes.setOnClickListener(this);

        noteManager = NoteManager.getInstance(DatabaseController.getInstance(getApplicationContext()).getWritableDatabase());

        String jsonString = Utility.getInstance().readJsonFromResource(getApplicationContext(),"settings_menu");
        Gson gson = new Gson();
        Type menuListType = new TypeToken<List<Menu>>() {}.getType();
        List<Menu> menus = gson.fromJson(jsonString, menuListType);

        MenuAdapter menuAdapter = new MenuAdapter(menus);
        binding.rvMenus.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.rvMenus.setAdapter(menuAdapter);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back_btn) {
            finish();
            return;
        }
        if (v.getId() == R.id.btn_create_new_category) {
            Intent intent = new Intent(this, ManageCategoryViewController.class);
            startActivity(intent);
            return;
        }
        if (v.getId() == R.id.btn_delete_all_notes) {
            displayDialog();
        }
    }

    private void displayDialog(){
        CustomConfirmationDialogFragment dialogFragment = new CustomConfirmationDialogFragment(()-> {
            deleteAllNotes();
        }, getResources().getString(R.string.STR_SURE_TO_DELETE_ALL_NOTES), DialogTypeEnum.ALERT);
        dialogFragment.show(getSupportFragmentManager(), "DeleteAllNotesConfirmationDialog");
    }

    private void deleteAllNotes() {
        noteManager.deleteAllNotes();
        Toast.makeText(this, "Successfully delete all notes", Toast.LENGTH_SHORT).show();
        finish();
    }
}
