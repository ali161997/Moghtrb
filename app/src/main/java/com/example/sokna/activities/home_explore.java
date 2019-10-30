package com.example.sokna.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.sokna.R;
import com.example.sokna.viewmodels.activityHomeViewModel;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;


public class home_explore extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView navigation;
    private activityHomeViewModel viewModel;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_explore);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Fresco.initialize(this);
        viewModel = ViewModelProviders.of(this).get(activityHomeViewModel.class);
        //loading the default fragment
        if (viewModel.getSelectedFragment().getValue().equals(new explore())) {
            viewFragment(viewModel.getSelectedFragment().getValue(), "FRAGMENT_HOME");
        } else {
            viewFragment(viewModel.getSelectedFragment().getValue(), "Fragment_other");
        }

        //getting bottom navigation view and attaching the listener
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);


    }


    @Override
    public void onBackPressed() {
        viewFragment(new explore(), "FRAGMENT_HOME");
        viewModel.getSelectedFragment().setValue(new explore());
        super.onBackPressed();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.explore_id:
                viewFragment(new explore(), "FRAGMENT_HOME");
                viewModel.setFragment_Selected(new explore());
                return true;

            case R.id.inbox_id:
                viewFragment(new inbox(), "Fragment_other");
                viewModel.setFragment_Selected(new inbox());
                return true;

            case R.id.map_id:
                viewFragment(new map(), "Fragment_other");
                viewModel.setFragment_Selected(new map());
                return true;

            case R.id.profile_id:
                viewFragment(new profile(), "Fragment_other");
                viewModel.setFragment_Selected(new profile());

                return true;
        }

        return false;

    }

    private void viewFragment(Fragment fragment, String name) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        // 1. Know how many fragments there are in the stack
        final int count = fragmentManager.getBackStackEntryCount();
        // 2. If the fragment is **not** "home type", save it to the stack
        if (name.equals("Fragment_other")) {
            fragmentTransaction.addToBackStack(name);
        }
        // Commit !
        fragmentTransaction.commit();
        // 3. After the commit, if the fragment is not an "home type" the back stack is changed, triggering the
        // OnBackStackChanged callback
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                // If the stack decreases it means I clicked the back button
                if (fragmentManager.getBackStackEntryCount() <= count) {
                    // pop all the fragment and remove the listener
                    fragmentManager.popBackStack("Fragment_other", POP_BACK_STACK_INCLUSIVE);
                    fragmentManager.removeOnBackStackChangedListener(this);
                    // set the home button selected
                    navigation.getMenu().getItem(0).setChecked(true);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
