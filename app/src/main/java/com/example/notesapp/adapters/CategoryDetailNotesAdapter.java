package com.example.notesapp.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.controllers.EditNoteViewController;
import com.example.notesapp.databinding.ItemNoteBinding;
import com.example.notesapp.models.Note;
import com.example.notesapp.utils.GlobalVariables;

import java.util.List;

public class CategoryDetailNotesAdapter extends RecyclerView.Adapter<CategoryDetailNotesAdapter.NoteViewHolder> {

    private static String tag = "CategoryDetailNotesAdapter";
    private List<Note> notes;

    public CategoryDetailNotesAdapter(List<Note> notes)
    {
        this.notes = notes;
    }

    @NonNull
    @Override
    public CategoryDetailNotesAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNoteBinding binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new NoteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryDetailNotesAdapter.NoteViewHolder holder, int position) {
        Note note = notes.get(position);

        holder.binding.tvNoteDesc.setText(note.getNoteDesc());
        holder.binding.ivNoteRedirectionButton.setVisibility(View.GONE);
        holder.binding.rlNoteItem.setOnClickListener(v -> {
            // Redirect to Edit Note
            Intent intent = new Intent(GlobalVariables.getInstance().getMainActivity(), EditNoteViewController.class);
            intent.putExtra("selectedNote", note);
            holder.binding.getRoot().getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        private final ItemNoteBinding binding;

        public NoteViewHolder(ItemNoteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
