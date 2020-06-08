package com.alihashem.moghtrb.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.alihashem.moghtrb.Interfaces.IOnBackPressed;
import com.alihashem.moghtrb.R;
import com.alihashem.moghtrb.fragments.Explore;
import com.alihashem.moghtrb.fragments.Inbox;
import com.alihashem.moghtrb.fragments.Map;
import com.alihashem.moghtrb.fragments.Menu;
import com.alihashem.moghtrb.repositories.explore_Repository;
import com.alihashem.moghtrb.viewmodels.HomeViewModel;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import static androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;


public class Home extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "home_explore";
    public static String dataFrom = "";
    BottomNavigationView navigation;
    Intent intentFrom;
    private HomeViewModel viewModel;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BadgeDrawable badgeDrawable = navigation.getOrCreateBadge(R.id.inbox_id);
            badgeDrawable.setBackgroundColor(R.color.quantum_googred);
            badgeDrawable.setNumber(1);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver),
                new IntentFilter("MyData")
        );
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Intent homeintent = new Intent(Home.this, signing.class);
            Log.d(TAG, "onCreate: failed go sign");
            startActivity(homeintent);


        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        // LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Fresco.initialize(getApplicationContext());
        explore_Repository.getInstance().setLang(getResources().getConfiguration().locale.getLanguage());
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        if (getIntent().getExtras() != null)
            if (getIntent().getExtras().containsKey("from")) {
                if (getIntent().getExtras().get("from").equals("service")) {
                    viewModel.setFragment_Selected(new Inbox());
                    navigation.removeBadge(R.id.inbox_id);
                }
            }

        if (viewModel.getSelectedFragment().getValue().equals(new Explore())) {
            viewFragment(viewModel.getSelectedFragment().getValue(), "FRAGMENT_HOME");
        } else {
            viewFragment(viewModel.getSelectedFragment().getValue(), "Fragment_other");

        }
        if (!isFinishing()) {
            Places.initialize(getApplicationContext(), getString(R.string.api_key));
        }

        //getting bottom navigation view and attaching the listener


    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                    }

                });
        FirebaseMessaging.getInstance().subscribeToTopic(FirebaseAuth.getInstance().getUid())
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.i(TAG, "onComplete: Subscribed failed");
                    } else Log.i(TAG, "onComplete: Subscribed completed");

                });


    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            viewFragment(new Explore(), "FRAGMENT_HOME");
            super.onBackPressed();
            viewModel.getSelectedFragment().setValue(new Explore());
        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.explore_id:
                viewFragment(new Explore(), "FRAGMENT_HOME");
                viewModel.setFragment_Selected(new Explore());
                return true;

            case R.id.inbox_id:
                viewFragment(new Inbox(), "Fragment_other");
                viewModel.setFragment_Selected(new Inbox());
                return true;

            case R.id.map_id:
                viewFragment(new Map(), "Fragment_other");
                viewModel.setFragment_Selected(new Map());
                return true;

            case R.id.profile_id:
                viewFragment(new Menu(), "Fragment_other");
                viewModel.setFragment_Selected(new Menu());

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
