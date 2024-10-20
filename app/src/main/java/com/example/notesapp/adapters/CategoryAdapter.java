package com.example.notesapp.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.R;
import com.example.notesapp.controllers.CategoryDetailViewController;
import com.example.notesapp.database.managers.NoteManager;
import com.example.notesapp.databinding.ItemCategorySummaryBinding;
import com.example.notesapp.models.Category;
import com.example.notesapp.models.Note;
import com.example.notesapp.utils.GlobalVariables;
import com.example.notesapp.utils.Utility;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategorySummaryViewHolder> {
    private List<Category> categoryList;
    private NoteManager noteManager;
    private static String tag = "CategoryAdapter";
    public CategoryAdapter(List<Category> categories, NoteManager noteManager) {
        this.categoryList = categories;
        this.noteManager = noteManager;
    }

    @NonNull
    @Override
    public CategoryAdapter.CategorySummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // View Binding to inflate the layout
        ItemCategorySummaryBinding binding = ItemCategorySummaryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CategorySummaryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategorySummaryViewHolder holder, int position) {
        Category category = categoryList.get(position);

        // Set category name and icon
        holder.binding.tvCategoryName.setText(category.getCategoryName());
        Utility.getInstance().updateImageViewIcon(category.getCategoryIcon(), holder.binding.ivCategoryIcon);
        holder.binding.btnViewDetail.setOnClickListener(v -> {
            // TODO: navigate to Category Detail
            Intent intent = new Intent(GlobalVariables.getInstance().getMainActivity(), CategoryDetailViewController.class);
            intent.putExtra("selectedCategory", category);
            holder.binding.getRoot().getContext().startActivity(intent);
        });
        updateSummaryDetail(holder, category);
    }

    /**
     * This method is used to update the total records for each category
     * @param holder
     * @param category
     */
    private void updateSummaryDetail(CategorySummaryViewHolder holder, Category category) {
        List<Note> notes = noteManager.getNotesByCategoryId(category.getCategoryId());
        String totalRecords = holder.binding.getRoot().getResources().getString(R.string.STR_TOPIC_TOTAL)
                + " " + String.valueOf(notes.size()) + " "
                + holder.binding.getRoot().getResources().getString(R.string.STR_RECORDS);
        holder.binding.tvCategorySummary.setText(totalRecords);
    }

    public void updateCategories(List<Category> categories) {
        this.categoryList = categories;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategorySummaryViewHolder extends RecyclerView.ViewHolder {
        private final ItemCategorySummaryBinding binding;
        public CategorySummaryViewHolder(ItemCategorySummaryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
