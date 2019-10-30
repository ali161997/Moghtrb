package com.example.sokna.viewmodels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sokna.Repository.explore_Repository;
import com.example.sokna.models.Room;

import java.util.List;

public class view_model_explore extends ViewModel {
    private explore_Repository repository;
    private MutableLiveData<Integer> state_where;
    private MutableLiveData<Integer> state_when;
    private MutableLiveData<Integer> state_filter;
    private MutableLiveData<Boolean> state_search;
    private MutableLiveData<Boolean> makeRefresh;
    private MutableLiveData<Integer> state_num_guests;

    public view_model_explore() {
        repository = explore_Repository.getInstance();
        state_where = new MutableLiveData<>();
        state_when = new MutableLiveData<>();
        state_filter = new MutableLiveData<>();
        state_search = new MutableLiveData<>(false);
        makeRefresh = new MutableLiveData<>();
        state_num_guests = new MutableLiveData<>();
    }


    public MutableLiveData<Boolean> getState_search() {
        return state_search;
    }

    public void setState_search(boolean state_search1) {
        state_search.setValue(state_search1);
    }


    public MutableLiveData<Integer> getState_where() {
        return state_where;
    }

    public void setState_where(Integer state_where1) {
        state_where.setValue(state_where1);
    }

    public MutableLiveData<Integer> getState_when() {
        return state_when;
    }

    public void setState_when(Integer state_when1) {
        state_when.setValue(state_when1);
    }

    public MutableLiveData<Integer> getState_filter() {
        return state_filter;
    }

    public void setState_filter(Integer state_filter1) {
        state_filter.setValue(state_filter1);
    }

    public MutableLiveData<Integer> getState_num_guests() {
        return state_num_guests;
    }

    public void setState_num_guests(Integer state_num_guests1) {
        state_num_guests.setValue(state_num_guests1);
    }

    public MutableLiveData<Boolean> getmakeRefesh() {
        return makeRefresh;
    }

    public void setMakeRefresh(Boolean makeRefresh1) {
        makeRefresh.setValue(makeRefresh1);
    }

    public LiveData<List<Room>> getrooms() {
        return repository.getdata();
    }

    public LiveData<List<Double>> getMinMaxSeek() {
        return repository.getMinMaxSeekbar();
    }


}
