package com.example.sokna.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.sokna.Interfaces.IOnBackPressed;
import com.example.sokna.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class inbox extends Fragment implements View.OnClickListener, IOnBackPressed {
    private Button btnExplore;
    private boolean needReset;
    private static final String TAG = "inbox";
    BottomNavigationView navigation;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String MESSAGE_URL = "http://friendlychat.firebase.google.com/message/";
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        if (container == null) {

            return null;
        }

        return inflater.inflate(R.layout.fragment_inbox, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnExplore = getView().findViewById(R.id.btnExploreInbox);
        btnExplore.setOnClickListener(this);
        needReset = false;
        navigation = getActivity().findViewById(R.id.navigation);
        //navigation.setOnNavigationItemSelectedListener(getActivity());


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnExploreInbox:
                NavigateFragment();
                break;
        }
    }

    private void NavigateFragment() {
        Fragment fragment = new explore();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        navigation.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public boolean onBackPressed() {
        if (needReset) {
            //action not popBackStack
            Log.i(TAG, "onBackPressed: clicked need reset");
            return true;
        } else {
            Log.i(TAG, "onBackPressed: clicked not need reset");
            return false;
        }
    }
}
