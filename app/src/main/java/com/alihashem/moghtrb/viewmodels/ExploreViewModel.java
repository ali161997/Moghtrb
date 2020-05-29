package com.alihashem.moghtrb.viewmodels;


import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alihashem.moghtrb.models.MyLatLong;
import com.alihashem.moghtrb.models.Room;
import com.alihashem.moghtrb.repositories.explore_Repository;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExploreViewModel extends ViewModel {
    private static final String TAG = "view_model_explore";
    private explore_Repository repository;
    private MutableLiveData<Integer> stateWhere;
    private MutableLiveData<Integer> stateWhen;
    private MutableLiveData<Integer> stateFilter;
    private MutableLiveData<Boolean> stateSearch;
    private MutableLiveData<Boolean> makeRefresh;
    private MutableLiveData<Integer> stateGuests;
    private MutableLiveData<List<Room>> listRooms;
    private MutableLiveData<List<String>> listCities;
    private MutableLiveData<Integer> selectedCityIndex;

    public void setSelectedRegionIndex(Integer selectedRegionIndex) {
        this.selectedRegionIndex.setValue(selectedRegionIndex);
    }

    private MutableLiveData<Integer> selectedRegionIndex;
    private MutableLiveData<Integer> selectionCityIndex;
    private MutableLiveData<Integer> selectionRegionIndex;

    public static String getTAG() {
        return TAG;
    }

    public MutableLiveData<Integer> getSelectionCityIndex() {
        return selectionCityIndex;
    }

    public void setSelectionCityIndex(Integer selectionCityIndex) {
        repository.setSelectedCityIndex(selectionCityIndex);
        this.selectionCityIndex.setValue(selectionCityIndex);
    }

    public MutableLiveData<Integer> getSelectionRegionIndex() {
        return selectionRegionIndex;
    }

    public void setSelectionRegionIndex(Integer selectionRegionIndex) {
        this.selectionRegionIndex.setValue(selectionRegionIndex);
    }

    private MutableLiveData<Context> context;
    private MutableLiveData<Double> startPrice;
    private MutableLiveData<Double> endPrice;
    private MutableLiveData<String> gender;
    private MutableLiveData<Integer> numGuests;
    private MutableLiveData<String> hostId;
    private MutableLiveData<Integer> numGuestCounter;
    private MutableLiveData<String> dates;
    private List<Room> list;
    private FirebaseFirestore db;
    private MutableLiveData<String> placeData;

    public ExploreViewModel() {
        repository = explore_Repository.getInstance();
        context = new MutableLiveData<>();
        list = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        stateWhere = new MutableLiveData<>();
        stateWhen = new MutableLiveData<>();
        stateFilter = new MutableLiveData<>();
        stateSearch = new MutableLiveData<>(false);
        makeRefresh = new MutableLiveData<>(true);
        stateGuests = new MutableLiveData<>();
        listRooms = new MutableLiveData<>();
        selectedCityIndex = new MutableLiveData<>(0);
        selectionCityIndex = new MutableLiveData<>(0);
        listCities = new MutableLiveData<>();
        startPrice = new MutableLiveData<>();
        endPrice = new MutableLiveData<>();
        gender = new MutableLiveData<>();
        listRooms.setValue(repository.getdata().getValue());
        selectedRegionIndex = new MutableLiveData<>(0);
        numGuests = new MutableLiveData<>();
        numGuestCounter = new MutableLiveData<>(1);
        hostId = new MutableLiveData<>();
        dates = new MutableLiveData<>();
        placeData = new MutableLiveData<>();
        selectionRegionIndex = new MutableLiveData<>(0);


    }

    public MutableLiveData<String> getPlaceData() {
        String key = String.format("%s-name", context.getValue().getResources().getConfiguration().locale.getLanguage());
        if (selectedCityIndex.getValue() != null && selectedCityIndex.getValue() != 0 && selectedRegionIndex.getValue() != null)
            placeData.setValue(getListCities().getValue().get(selectedCityIndex.getValue()) + "-" + getListRegions().getValue().get(selectedRegionIndex.getValue()).get(key));
        return placeData;
    }

    public MutableLiveData<String> getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates.setValue(dates);
    }

    public MutableLiveData<Integer> getSelectedCityIndex() {
        return selectedCityIndex;
    }

    public void setSelectedCityIndex(Integer city) {
        selectedCityIndex.setValue(city);
        getPlaceData();

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
    }

    public MutableLiveData<String> getGender() {
        return gender;
    }

    public void setGender(String gender) {

        this.gender.setValue(gender);
    }

    public void resetStates() {
        stateWhere.setValue(4);
        stateWhen.setValue(4);
        stateFilter.setValue(4);
        stateSearch.setValue(false);
        stateGuests.setValue(4);

    }

    public void setContext(Context context) {
        this.context.setValue(context);
    }

    public MutableLiveData<List<String>> getListCities() {
        listCities = repository.getCities();
        return listCities;
    }

    public MutableLiveData<List<Map<String, String>>> getListRegions() {

        return repository.getRegionMaps();
    }

    //gets
    public MutableLiveData<Integer> getStateGuests() {
        return stateGuests;
    }

    //Bottom sheets states and search Bar
    //sets
    public void setStateGuests(Integer stateGuests1) {
        stateGuests.setValue(stateGuests1);
    }

    public MutableLiveData<Integer> getStateWhen() {
        return stateWhen;
    }

    public void setStateWhen(Integer stateWhen1) {
        stateWhen.setValue(stateWhen1);
    }

    public MutableLiveData<Integer> getStateFilter() {
        return stateFilter;
    }

    public void setStateFilter(Integer stateFilter1) {
        stateFilter.setValue(stateFilter1);
    }

    public MutableLiveData<Integer> getStateWhere() {
        return stateWhere;
    }

    public void setStateWhere(Integer stateWhere1) {
        stateWhere.setValue(stateWhere1);
    }

    public MutableLiveData<Boolean> getStateSearch() {
        return stateSearch;
    }

    public void setStateSearch(Boolean stateSearch1) {
        stateSearch.setValue(stateSearch1);
    }

    //Control Refreshing
    public void setMakeRefresh(Boolean makeRefresh1) {

        makeRefresh.setValue(makeRefresh1);
    }

    public MutableLiveData<Boolean> getMakeRefresh() {
        return makeRefresh;
    }

    public MutableLiveData<List<Room>> getRooms() {

        return listRooms;
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


    public void execute() {
        listRooms.setValue(null);
        list.clear();

        Query query = db.collection("Rooms").whereEqualTo("exist", true);

        if (startPrice.getValue() != null)
            query = query.whereGreaterThanOrEqualTo("price", startPrice.getValue());

        if (endPrice.getValue() != null)
            query = query.whereLessThanOrEqualTo("price", endPrice.getValue());

        if (selectedCityIndex.getValue() != 0 && selectedCityIndex.getValue() != null)
            query = query.whereEqualTo("cityIndex", selectedCityIndex.getValue());

        if (selectedRegionIndex.getValue() != null && selectedRegionIndex.getValue() != 0 && selectedCityIndex.getValue() != 0)
            query = query.whereEqualTo("regionIndex", selectedRegionIndex.getValue());

        if (gender.getValue() != null)
            query = query.whereEqualTo("gender", gender.getValue().toLowerCase());
        if (numGuests.getValue() != null) {

        }

        Log.i(TAG, "execute: " + query);
        try {
            query.get().addOnSuccessListener(
                    queryDocumentSnapshots -> {
                        Log.i(TAG, "execute: ");
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list_rooms = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list_rooms) {
                                Room room = new Room();
                                room.setPrice(d.getDouble("price"));
                                room.setRate(d.getLong("rate").intValue());
                                room.setNum_reviews(d.getLong("num_reviews").intValue());
                                room.setStreet(d.get("street").toString());
                                room.setBedsAvailable(d.getLong("bedsAvailable").intValue());
                                room.setTotalBeds(d.getLong("totalBeds").intValue());
                                room.setHostId(d.get("hostId").toString());
                                room.setCity(d.get("city").toString());
                                room.setRegion(d.get("region").toString());
                                room.setUrlImage1(d.get("urlImage1").toString());
                                room.setDepartId(d.get("departId").toString());
                                room.setRoomId(d.get("roomId").toString());
                                room.setArAddress(d.get("arAddress").toString());
                                room.setLatLng(d.get("latLng", MyLatLong.class));
                                room.setGender(d.get("gender").toString());
                                room.setCityIndex(d.getLong("cityIndex").intValue());
                                room.setRegionIndex(d.getLong("regionIndex").intValue());
                                list.add(room);

                            }
                            listRooms.setValue(list);
                            Log.i(TAG, "setFilter:success search bitween ");
                        }

                    }).addOnFailureListener(e -> {
                Log.i(TAG, "setFilter: failed search bitween " + e);
            });
        } catch (Exception e) {
            Log.i(TAG, "execute: " + e.getMessage());
        }

    }

}
