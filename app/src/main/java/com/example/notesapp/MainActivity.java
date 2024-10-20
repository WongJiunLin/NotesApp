package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notesapp.controllers.HomeFragmentViewController;
import com.example.notesapp.controllers.NewNoteViewController;
import com.example.notesapp.controllers.SummaryFragmentViewController;
import com.example.notesapp.database.controllers.DatabaseController;
import com.example.notesapp.database.managers.CategoryManager;
import com.example.notesapp.database.managers.NoteManager;
import com.example.notesapp.databinding.ActivityMainBinding;
import com.example.notesapp.utils.FragmentUtils;
import com.example.notesapp.utils.GlobalVariables;
import com.example.notesapp.utils.Utility;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String tag = "MainActivity";
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        startAllInstances();

        // If app is fresh launched, update the MainActivity fragment with Home Fragment
        if (savedInstanceState == null) {
            FragmentUtils.getInstance().loadFragment(getSupportFragmentManager(), new HomeFragmentViewController(), R.id.fragment_container);
            updateBottomNavActiveStatus(binding.bottomNav.navHome);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release the view binding to avoid memory leak
        binding = null;
        GlobalVariables.resetInstance();
        Utility.resetInstance();
        FragmentUtils.resetInstance();
        NoteManager.resetInstance();
        CategoryManager.resetInstance();
        DatabaseController.resetInstance();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.nav_home)
        {
            resetBottomNavStatus();
            updateBottomNavActiveStatus(v);
            performNavigation(v.getId());
            return;
        }
        if (v.getId() == R.id.nav_summary)
        {
            resetBottomNavStatus();
            updateBottomNavActiveStatus(v);
            performNavigation(v.getId());
        }
        if (v.getId() == R.id.nav_add)
        {
            resetBottomNavStatus();
            performNavigation(v.getId());
        }
    }

    /**
     * This method is used to setup all the necessary singletons and buttons' click events
     */
    private void startAllInstances(){
        GlobalVariables.getInstance().setMainActivity(this);
        Utility.getInstance().setMainActivity(this);
        FragmentUtils.getInstance().setMainActivity(this);


        binding.bottomNav.navHome.setOnClickListener(this);
        binding.bottomNav.navAdd.setOnClickListener(this);
        binding.bottomNav.navSummary.setOnClickListener(this);
    }

    /**
     *This method is used to reset the navigation bar button color back to default white
     */
    private void resetBottomNavStatus(){
        binding.bottomNav.ivHome.setColorFilter(GlobalVariables.getInstance().getDefaultButtonColor());
        binding.bottomNav.ivNotes.setColorFilter(GlobalVariables.getInstance().getDefaultButtonColor());
        binding.bottomNav.ivAdd.setColorFilter(GlobalVariables.getInstance().getDefaultButtonColor());
        binding.bottomNav.tvNotes.setTextColor(GlobalVariables.getInstance().getDefaultButtonColor());
        binding.bottomNav.tvHome.setTextColor(GlobalVariables.getInstance().getDefaultButtonColor());
    }

    /**
     * This method will update the selected view display color
     * @param v - View to update the status
     */
    private void updateBottomNavActiveStatus(View v){
        if (v.getId() == R.id.nav_home)
        {
            binding.bottomNav.ivHome.setColorFilter(GlobalVariables.getInstance().getSelectedButtonColor());
            binding.bottomNav.tvHome.setTextColor(GlobalVariables.getInstance().getSelectedButtonColor());
            return;
        }
        if (v.getId() == R.id.nav_summary)
        {
            binding.bottomNav.ivNotes.setColorFilter(GlobalVariables.getInstance().getSelectedButtonColor());
            binding.bottomNav.tvNotes.setTextColor(GlobalVariables.getInstance().getSelectedButtonColor());
        }
    }
    private void performNavigation(int buttonID) {
        if (buttonID == R.id.nav_home) {
            FragmentUtils.getInstance().loadFragment(getSupportFragmentManager(), new HomeFragmentViewController(), R.id.fragment_container);
            return;
        }
        if (buttonID == R.id.nav_summary) {
            FragmentUtils.getInstance().loadFragment(getSupportFragmentManager(), new SummaryFragmentViewController(), R.id.fragment_container);
            return;
        }
        if (buttonID == R.id.nav_add) {
            Intent intent = new Intent(this, NewNoteViewController.class);
            startActivity(intent);
            return;
        }
    }


}