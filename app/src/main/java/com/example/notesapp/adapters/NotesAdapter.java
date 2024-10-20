package com.example.notesapp.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.R;
import com.example.notesapp.controllers.EditNoteViewController;
import com.example.notesapp.controllers.NewNoteViewController;
import com.example.notesapp.databinding.ItemCategoryNoteBinding;
import com.example.notesapp.databinding.ItemNoteBinding;
import com.example.notesapp.models.Category;
import com.example.notesapp.models.Note;
import com.example.notesapp.utils.GlobalVariables;
import com.example.notesapp.utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.CategoryNoteViewHolder> {

    private List<Category> categories;

    public NotesAdapter(List<Category> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public NotesAdapter.CategoryNoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Use View Binding to inflate the layout
        ItemCategoryNoteBinding binding = ItemCategoryNoteBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CategoryNoteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.CategoryNoteViewHolder holder, int position) {
        Category category = categories.get(position);

        // Set category name and icon
        holder.binding.tvCategoryName.setText(category.getCategoryName());
        Utility.getInstance().updateImageViewIcon(category.getCategoryIcon(), holder.binding.ivCategoryIcon);
        // Handle the initial displaying notes
        setupNoteList(holder, category, false);

        // Handle expand / collapse functionality
        holder.binding.ivCategoryDropdown.setOnClickListener(v -> {
            if(category.isExpanded()) {
                // Collapse
                holder.binding.llNoteList.setVisibility(View.GONE);
                holder.binding.ivCategoryDropdown.setImageResource(R.drawable.ic_down_arrow);
                category.setExpanded(false);
            } else {
                // Expand
                holder.binding.llNoteList.setVisibility(View.VISIBLE);
                holder.binding.ivCategoryDropdown.setImageResource(R.drawable.ic_up_arrow);
                category.setExpanded(true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class CategoryNoteViewHolder extends RecyclerView.ViewHolder {
        private final ItemCategoryNoteBinding binding;
        public CategoryNoteViewHolder(ItemCategoryNoteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    /**
     * This method is used to setup the initial view of the note list
     * @param holder
     * @param category
     * @param isMorePressed
     */
    private void setupNoteList(CategoryNoteViewHolder holder, Category category, boolean isMorePressed) {
        List<Note> notes = category.getNotes();
        holder.binding.llNoteList.removeAllViews(); // Clear all the notes in the categories

        int notesCount = notes.size();

        if (notesCount == 0) {
            holder.binding.tvNoNote.setVisibility(View.VISIBLE);
            holder.binding.llNoteList.addView(holder.binding.tvNoNote);
            holder.binding.tvNoNote.setOnClickListener(v -> {
                Intent intent = new Intent(holder.binding.getRoot().getContext(), NewNoteViewController.class);
                holder.binding.getRoot().getContext().startActivity(intent);
            });
        }

        for (int i =0; i < (isMorePressed ? notesCount : Math.min(2, notesCount)); i++)
        {
            // Inflate the note item layout using view binding
            ItemNoteBinding itemNoteBinding = ItemNoteBinding.inflate(LayoutInflater.from(holder.itemView.getContext()), holder.binding.llNoteList, false);

            // Setup note desc to only display first 20 characters if exceed
            String desc = notes.get(i).getNoteDesc().length() > 20 ? notes.get(i).getNoteDesc().substring(0, 20) + "..." : notes.get(i).getNoteDesc();
            itemNoteBinding.tvNoteDesc.setText(desc);

            // Click event for the note
            Note note = notes.get(i);
            itemNoteBinding.rlNoteItem.setOnClickListener(v -> {
                redirectToEditNoteScreen(note);
            });

            // Add the created note view to the note list linear layout
            holder.binding.llNoteList.addView(itemNoteBinding.getRoot());
        }

        // Show more option when notes more than 2
        if (notesCount > 2) {
            holder.binding.tvBottomButton.setVisibility(View.VISIBLE);
            holder.binding.tvBottomButton.setText(R.string.STR_MORE);
            holder.binding.llNoteList.addView(holder.binding.tvBottomButton);
        } else {
            holder.binding.tvBottomButton.setVisibility(View.GONE);
        }

        // More button handling
        holder.binding.tvBottomButton.setText(isMorePressed ? R.string.STR_SHOW_LESS : R.string.STR_MORE);
        holder.binding.tvBottomButton.setOnClickListener(v -> {
            if (holder.binding.tvBottomButton.getText().equals(holder.binding.getRoot().getResources().getString(R.string.STR_MORE))) {
                setupNoteList(holder, category, true);
            } else {
                setupNoteList(holder, category, false);
            }
        });
    }

    public void updateCategories(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    private void redirectToEditNoteScreen(Note note) {
        Intent intent = new Intent(GlobalVariables.getInstance().getMainActivity(), EditNoteViewController.class);
        intent.putExtra("selectedNote", note);
        GlobalVariables.getInstance().getMainActivity().startActivity(intent);
    }
}
