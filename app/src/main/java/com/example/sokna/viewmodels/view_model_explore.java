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
    private MutableLiveData<List<Room>> listRooms;
    private MutableLiveData<List<String>> listCities;
    private MutableLiveData<String> selectedCity;

    public void resetStates() {
        state_where.setValue(4);
        state_when.setValue(4);
        state_filter.setValue(4);
        state_search.setValue(false);
        makeRefresh.setValue(true);
        state_num_guests.setValue(4);

    }

    public view_model_explore() {
        // if(repository.equals(null))
        //{
        repository = explore_Repository.getInstance();
        state_where = new MutableLiveData<>();
        state_when = new MutableLiveData<>();
        state_filter = new MutableLiveData<>();
        state_search = new MutableLiveData<>(false);
        makeRefresh = new MutableLiveData<>();
        state_num_guests = new MutableLiveData<>();
        listRooms = new MutableLiveData<>();
        selectedCity = new MutableLiveData<>();
        listCities = new MutableLiveData<>();

        // }
        listRooms.setValue(repository.getdata().getValue());
        listCities.setValue(repository.getCities().getValue());
    }

    public MutableLiveData<String> getSelectedCity() {
        return selectedCity;
    }

    public void setSelectedCity(String city) {
        repository.setSelectedCity(city);
        selectedCity.setValue(city);

    }


    public MutableLiveData<List<String>> getListCities() {
        return listCities;
    }

    public MutableLiveData<List<String>> getListRigions() {
        return repository.getRegions();
    }

    public MutableLiveData<Boolean> getState_search() {
        return state_search;
    }

    public void setState_search(Boolean state_search1) {
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
        return listRooms;
    }

    public LiveData<List<Double>> getMinMaxSeek() {
        return repository.getMinMaxSeekbar();
    }

    public void setPlace(String city, String Region) {

    }

    public void setNumGuest(int numGuests) {

    }

    public void setTime(String[] StudentTime) {

    }

    public void setTime(String checkIn, String Checkout) {

    }

    public void setFilter(double Startprice, double endPrice, boolean shared, boolean smoking, String preferredFaculty) {

    }


}
