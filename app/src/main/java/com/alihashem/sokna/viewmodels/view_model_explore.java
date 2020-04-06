package com.alihashem.sokna.viewmodels;


import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alihashem.sokna.Repository.Uploading_explore;
import com.alihashem.sokna.Repository.explore_Repository;
import com.alihashem.sokna.models.Room;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class view_model_explore extends ViewModel {
    private static final String TAG = "view_model_explore";
    Uploading_explore uploading_explore;
    private explore_Repository repository;
    private MutableLiveData<Integer> state_where;
    private MutableLiveData<Integer> state_when;
    private MutableLiveData<Integer> state_filter;
    private MutableLiveData<Boolean> state_search;
    private MutableLiveData<Boolean> makeRefresh;
    private MutableLiveData<Integer> state_num_guests;
    private MutableLiveData<List<Room>> listRooms;
    private MutableLiveData<List<String>> listCities;
    private MutableLiveData<Integer> selectedCityIndex;
    private MutableLiveData<Integer> selectedRegionIndex;
    private List<Room> list = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<HashMap<String, String>> SearchText;
    private MutableLiveData<Context> context;
    private MutableLiveData<Double> startPrice;
    private MutableLiveData<Double> endPrice;
    private MutableLiveData<String> gender;
    private MutableLiveData<Boolean> shared;
    private MutableLiveData<List<String>> faculties;
    private MutableLiveData<Integer> numGuests;
    private MutableLiveData<String> hostId;
    private MutableLiveData<Integer> numGuestCounter;
    private MutableLiveData<String> checkInData;
    private MutableLiveData<String> checkOutData;
    private String lang;

    public view_model_explore() {
        repository = explore_Repository.getInstance();
        context = new MutableLiveData<>();
        state_where = new MutableLiveData<>();
        state_when = new MutableLiveData<>();
        state_filter = new MutableLiveData<>();
        state_search = new MutableLiveData<>(false);
        makeRefresh = new MutableLiveData<>(true);
        state_num_guests = new MutableLiveData<>();
        listRooms = new MutableLiveData<>();
        selectedCityIndex = new MutableLiveData<>();
        listCities = new MutableLiveData<>();
        startPrice = new MutableLiveData<>();
        endPrice = new MutableLiveData<>();
        gender = new MutableLiveData<>();
        listRooms.setValue(repository.getdata().getValue());
        SearchText = new MutableLiveData<>();
        uploading_explore = new Uploading_explore();
        selectedRegionIndex = new MutableLiveData<>();
        faculties = new MutableLiveData<>();
        shared = new MutableLiveData<>();
        numGuests = new MutableLiveData<>();
        numGuestCounter = new MutableLiveData<>(1);
        hostId = new MutableLiveData<>();
        checkInData = new MutableLiveData<>();
        checkOutData = new MutableLiveData<>();


    }


    public MutableLiveData<String> getCheckInData() {
        return checkInData;
    }

    public void setCheckInData(String checkInData) {
        this.checkInData.setValue(checkInData);
    }

    public MutableLiveData<String> getCheckOutData() {
        return checkOutData;
    }

    public void setCheckOutData(String checkOutData) {
        this.checkOutData.setValue(checkOutData);
    }

    public MutableLiveData<Integer> getSelectedCityIndex() {
        return selectedCityIndex;
    }

    public void setSelectedCityIndex(Integer city) {
        selectedCityIndex.setValue(city);
        repository.setSelectedCityIndex(city);

    }

    public MutableLiveData<Integer> getSelectedRegionIndex() {
        return selectedRegionIndex;
    }

    public MutableLiveData<String> getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId.setValue(hostId);
        getRoomsWithHostId();

    }

    private void getRoomsWithHostId() {
        listRooms.setValue(null);
        list.clear();
        CollectionReference roomsRef = db.collection("Rooms");
        Query query = roomsRef.whereEqualTo("hostId", hostId.getValue());

        query.get().addOnSuccessListener(
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
        });
    }

    public MutableLiveData<Integer> getNumGuestCounter() {
        return numGuestCounter;
    }

    public void setNumGuestCounter(Integer numGuestCounter) {
        this.numGuestCounter.setValue(numGuestCounter);
    }

    public MutableLiveData<Integer> getNumGuests() {
        return numGuests;
    }

    public void setNumGuests(Integer numGuests) {

        this.numGuests.setValue(numGuests);
        excute();
    }

    public MutableLiveData<String> getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender.setValue(gender);
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
        this.lang = context.getResources().getConfiguration().locale.getLanguage();
    }

    public MutableLiveData<List<String>> getListCities() {
        listCities = repository.getCities();
        return listCities;
    }

    public MutableLiveData<List<Map<String, String>>> getListRigions() {

        return repository.getRegionMaps();
    }

    //gets
    public MutableLiveData<Integer> getState_num_guests() {
        return state_num_guests;
    }

    //Bottom sheets states and search Bar
    //sets
    public void setState_num_guests(Integer state_num_guests1) {
        state_num_guests.setValue(state_num_guests1);
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

    public MutableLiveData<Integer> getState_where() {
        return state_where;
    }

    public void setState_where(Integer state_where1) {
        state_where.setValue(state_where1);
    }

    public MutableLiveData<Boolean> getState_search() {
        return state_search;
    }

    public void setState_search(Boolean state_search1) {
        state_search.setValue(state_search1);
    }

    //Control Refreshing
    public void setMakeRefresh(Boolean makeRefresh1) {

        makeRefresh.setValue(makeRefresh1);
    }

    public MutableLiveData<Boolean> getmakeRefesh() {
        return makeRefresh;
    }

    public MutableLiveData<List<Room>> getrooms() {

        return listRooms;
    }

    public MutableLiveData<HashMap<String, String>> getSearchText() {
        return SearchText;
    }

    //for Search
    public void setSearchText(HashMap<String, String> searchText) {
        SearchText.setValue(searchText);
        uploading_explore.performUploadSearch(SearchText.getValue());
    }

    public void setPlace(Integer cityIndex, Integer regionIndex) {
        selectedRegionIndex.setValue(regionIndex);
        selectedCityIndex.setValue(cityIndex);
        excute();
    }

    public MutableLiveData<List<String>> getFaculties() {
        return repository.getFaculties();
    }

    public MutableLiveData<Double> getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(Double startPrice) {
        this.startPrice.setValue(startPrice);
    }

    public MutableLiveData<Double> getEndPrice() {
        return endPrice;
    }

    public void setEndPrice(Double endPrice) {
        this.endPrice.setValue(endPrice);
    }

    //for default min max
    public MutableLiveData<Double> getMinSeek() {

        return repository.getMinSeekbar();
    }

    public MutableLiveData<Double> getMaxSeek() {
        return repository.getMaxSeekbar();
    }


    private void excute() {
        listRooms.setValue(null);
        list.clear();
        CollectionReference roomsRef = db.collection("Rooms");
        if (startPrice.getValue() == null)
            startPrice.setValue(repository.getMinSeekbar().getValue());
        if (endPrice.getValue() == null)
            endPrice.setValue(repository.getMaxSeekbar().getValue());
        Query query = roomsRef.
                whereGreaterThanOrEqualTo("price", startPrice.getValue())
                .whereLessThanOrEqualTo("price", endPrice.getValue())
                .whereEqualTo("regionIndex", selectedRegionIndex.getValue())
                .whereEqualTo("cityIndex", selectedCityIndex.getValue());
        if (gender.getValue() != null)
            query.whereEqualTo("gender", gender.getValue().toLowerCase());

        query.get().addOnSuccessListener(
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

}
