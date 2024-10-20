package com.example.notesapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.databinding.ItemMenuBinding;
import com.example.notesapp.models.Menu;
import com.example.notesapp.utils.Utility;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuHolder> {

    private List<Menu> menuOptions;
    private static String tag = "MenuAdapter";

    public MenuAdapter(List<Menu> menuList) {
        this.menuOptions = menuList;
    }

    @NonNull
    @Override
    public MenuAdapter.MenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMenuBinding binding = ItemMenuBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MenuHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.MenuHolder holder, int position) {
        Menu menu = menuOptions.get(position);

        holder.binding.tvMenuName.setText(menu.getMenuName());
        Utility.getInstance().updateImageViewIcon(menu.getMenuIcon(), holder.binding.ivMenuIcon);
        holder.binding.rlMenuItem.setOnClickListener(v -> {
            Utility.getInstance().openBrowser(v.getContext(), menu.getUrl());
        });
    }

    @Override
    public int getItemCount() {
        return menuOptions.size();
    }

    public static class MenuHolder extends RecyclerView.ViewHolder {
        private final ItemMenuBinding binding;
        public MenuHolder(ItemMenuBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
