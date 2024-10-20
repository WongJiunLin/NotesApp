package com.example.notesapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notesapp.MainActivity;
import com.example.notesapp.R;
import com.example.notesapp.components.CustomConfirmationDialogFragment;
import com.example.notesapp.database.controllers.DatabaseController;
import com.example.notesapp.database.managers.CategoryManager;
import com.example.notesapp.databinding.ActivityManageCategoryBinding;
import com.example.notesapp.enums.DialogTypeEnum;
import com.example.notesapp.models.Category;
import com.example.notesapp.utils.GlobalVariables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ManageCategoryViewController extends AppCompatActivity implements View.OnClickListener {

    private static String tag = "ManageCategoryViewController";
    private ActivityManageCategoryBinding binding;
    private ArrayList<Integer> categoryIconIds = new ArrayList<>();
    private ImageView selectedIcon;
    private String categoryName;

    private CategoryManager categoryManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityManageCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        categoryManager = CategoryManager.getInstance(DatabaseController.getInstance(getApplicationContext()).getWritableDatabase());

        updateClickListeners();
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
        }
        if (categoryIconIds.contains(v.getId())) {
            onCategoryIconClick((ImageView) v);
        }
        if (v.getId() == R.id.btn_add_category) {
            addNewCategory();
        }
    }

    private void updateClickListeners(){
        binding.ivBackBtn.setOnClickListener(this);
        binding.btnAddCategory.setOnClickListener(this);
        // Category icon
        for (int i = 0; i < binding.glIconSelection.getChildCount(); i++) {
            ImageView imageView = (ImageView) binding.glIconSelection.getChildAt(i);
            imageView.setOnClickListener(this);
            categoryIconIds.add(imageView.getId());
        }
    }

    private void onCategoryIconClick(ImageView selectedImageView){
        // reset all icons to default color
        for (int i = 0; i < binding.glIconSelection.getChildCount(); i++) {
            ImageView imageView = (ImageView) binding.glIconSelection.getChildAt(i);
            imageView.setColorFilter(GlobalVariables.getInstance().getDefaultButtonColor());
        }
        selectedImageView.setColorFilter(GlobalVariables.getInstance().getSelectedButtonColor());
        selectedIcon = selectedImageView;
    }

    private void addNewCategory() {
        categoryName = binding.edtCategoryName.getText().toString().trim();
        if (!categoryName.equalsIgnoreCase("") && selectedIcon != null) {
            Category category = new Category(
                    0,
                    categoryName,
                    String.valueOf(selectedIcon.getTag()),
                    false,
                    null
            );
            long result = categoryManager.addCategory(category);
            if (result != -1) {
                CustomConfirmationDialogFragment dialogFragment = new CustomConfirmationDialogFragment(() -> {
                    proceedToHomeScreen();
                }, getResources().getString(R.string.STR_SUCCESSFUL_CREATED_CATEGORY), DialogTypeEnum.SUCCESS);
                GlobalVariables.getHandlerUI().post(new Runnable() {
                    @Override
                    public void run() {
                        dialogFragment.show(getSupportFragmentManager(), "SuccessCreateCategoryDialog");
                    }
                });
            } else {
                Toast.makeText(this, "Failed added category with status - "+result, Toast.LENGTH_SHORT).show();
            }
        } else {
            CustomConfirmationDialogFragment dialogFragment = new CustomConfirmationDialogFragment(
                    null,
                    getResources().getString(R.string.STR_FILL_CATEGORY_NAME_AND_ICON),
                    DialogTypeEnum.INFO
            );
            dialogFragment.show(getSupportFragmentManager(), "AddCategoryInfoDialog");
        }
    }

    private void proceedToHomeScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
