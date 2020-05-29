package com.alihashem.moghtrb.viewmodels;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alihashem.moghtrb.fragments.Explore;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<Fragment> fragment_Selected;
    private static final String TAG = "activityHomeViewModel";

    public HomeViewModel() {

        fragment_Selected = new MutableLiveData<>(new Explore());
    }

    public MutableLiveData<Fragment> getSelectedFragment() {
        return fragment_Selected;
    }

    public void setFragment_Selected(Fragment fragment_Selected) {
        this.fragment_Selected.setValue(fragment_Selected);
    }
}
