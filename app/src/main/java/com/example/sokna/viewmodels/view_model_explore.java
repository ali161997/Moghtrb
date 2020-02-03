package com.example.sokna.viewmodels;


import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sokna.Repository.Uploading_explore;
import com.example.sokna.Repository.explore_Repository;
import com.example.sokna.models.Room;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
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
    private static final String TAG = "view_model_explore";
    private MutableLiveData<List<String>> listCities;
    private MutableLiveData<String> selectedCity;
    Uploading_explore uploading_explore;
    private MutableLiveData<String> selectedRegion;
    private List<Room> list = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<HashMap<String, String>> SearchText;
    private MutableLiveData<Context> context;

    public view_model_explore() {
        repository = explore_Repository.getInstance();
        context = new MutableLiveData<>();
        state_where = new MutableLiveData<>();
        state_when = new MutableLiveData<>();
        state_filter = new MutableLiveData<>();
        state_search = new MutableLiveData<>(false);
        makeRefresh = new MutableLiveData<>();
        state_num_guests = new MutableLiveData<>();
        listRooms = new MutableLiveData<>();
        selectedCity = new MutableLiveData<>();
        listCities = new MutableLiveData<>();
        listRooms.setValue(repository.getdata().getValue());
        listCities.setValue(repository.getCities().getValue());
        SearchText = new MutableLiveData<>();
        uploading_explore = new Uploading_explore();
        selectedRegion = new MutableLiveData<>();
    }

    public void resetStates() {
        state_where.setValue(4);
        state_when.setValue(4);
        state_filter.setValue(4);
        state_search.setValue(false);
        makeRefresh.setValue(true);
        state_num_guests.setValue(4);

    }

    public void setContext(Context context) {
        this.context.setValue(context);
    }

    public MutableLiveData<HashMap<String, String>> getSearchText() {
        return SearchText;
    }

    public void setSearchText(HashMap<String, String> searchText) {
        SearchText.setValue(searchText);
        uploading_explore.performUploadSearch(SearchText.getValue());
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

    public MutableLiveData<List<Room>> getrooms() {
        return listRooms;
    }

    public MutableLiveData<Double> getMinSeek() {

        return repository.getMinSeekbar();
    }

    public MutableLiveData<Double> getMaxSeek() {

        return repository.getMaxSeekbar();
    }

    //for Search
    public void setPlace(String city, String Region) {
        selectedRegion.setValue(Region);
        selectedCity.setValue(city);
    }

    public void setPriceRange(Double startPrice, Double endPrice) {
        listRooms = new MutableLiveData<>();
        list.clear();
        db.collection("Rooms")
                .whereGreaterThanOrEqualTo("price", startPrice)
                .whereLessThanOrEqualTo("price", endPrice)
                .get().addOnSuccessListener(
                queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> list_rooms = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list_rooms) {
                            Room room = d.toObject(Room.class);
                            list.add(room);
                        }
                        listRooms.setValue(list);
                        Log.i(TAG, "setFilter: Success search between  ");
                    }

                }).addOnFailureListener(e -> {
            Log.i(TAG, "setFilter: failed search bitween " + e);
        });


    }

    private void getData() {


    }


}
