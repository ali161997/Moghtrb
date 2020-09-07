package com.moghtrb.viewmodels;


import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.moghtrb.models.MyLatLong;
import com.moghtrb.models.Room;
import com.moghtrb.models.StudentTime;
import com.moghtrb.repositories.explore_Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExploreViewModel extends ViewModel {
    private static final String TAG = "view_model_explore";
    private explore_Repository repository;
    private MutableLiveData<Integer> resultsFound;
    private MutableLiveData<Integer> stateWhere;
    private MutableLiveData<Integer> stateWhen;
    private MutableLiveData<Integer> stateFilter;
    private MutableLiveData<Boolean> stateSearch;
    private MutableLiveData<Boolean> makeRefresh;
    private MutableLiveData<Integer> stateGuests;
    private MutableLiveData<List<Room>> listRooms;
    private MutableLiveData<List<String>> listCities;
    private MutableLiveData<Integer> selectedCityIndex;
    private MutableLiveData<Integer> selectedRegionIndex;
    private MutableLiveData<Integer> selectionCityIndex;
    private MutableLiveData<Integer> selectionRegionIndex;
    private MutableLiveData<Context> context;
    private MutableLiveData<Double> startPrice;
    private MutableLiveData<Double> endPrice;
    private MutableLiveData<String> gender;
    private MutableLiveData<Integer> numGuests;
    private MutableLiveData<String> hostId;
    private MutableLiveData<Integer> numGuestCounter;
    private MutableLiveData<String> dates;
    private FirebaseFirestore db;
    private MutableLiveData<String> place;
    private int limit = 10;
    private MutableLiveData<Boolean> isScrolling;
    private MutableLiveData<Boolean> isLastItemReached;
    private DocumentSnapshot lastVisible;
    private MutableLiveData<Boolean> isStudent;
    private MutableLiveData<StudentTime> studentTimeMutableLiveData;

    public ExploreViewModel() {
        repository = explore_Repository.getInstance();
        context = new MutableLiveData<>();
        studentTimeMutableLiveData = new MutableLiveData<>(new StudentTime());
        isScrolling = new MutableLiveData<>(false);
        isLastItemReached = new MutableLiveData<>(false);
        db = FirebaseFirestore.getInstance();
        stateWhere = new MutableLiveData<>();
        stateWhen = new MutableLiveData<>();
        stateFilter = new MutableLiveData<>();
        stateSearch = new MutableLiveData<>(false);
        makeRefresh = new MutableLiveData<>(true);
        stateGuests = new MutableLiveData<>();
        listRooms = new MutableLiveData<>(new ArrayList<>());
        selectedCityIndex = new MutableLiveData<>();
        selectionCityIndex = new MutableLiveData<>();
        listCities = new MutableLiveData<>();
        isStudent = new MutableLiveData<>(true);
        startPrice = new MutableLiveData<>();
        endPrice = new MutableLiveData<>();
        resultsFound = new MutableLiveData<>();
        gender = new MutableLiveData<>();
        selectedRegionIndex = new MutableLiveData<>();
        numGuests = new MutableLiveData<>();
        numGuestCounter = new MutableLiveData<>(1);
        hostId = new MutableLiveData<>();
        dates = new MutableLiveData<>();
        place = new MutableLiveData<>();
        selectionRegionIndex = new MutableLiveData<>();
        execute();


    }

    public static String getTAG() {
        return TAG;
    }

    public MutableLiveData<Integer> getResultsFound() {
        return resultsFound;
    }

    public void setResultsFound(MutableLiveData<Integer> resultsFound) {
        this.resultsFound = resultsFound;
    }

    public MutableLiveData<StudentTime> getStudentTimeMutableLiveData() {
        return studentTimeMutableLiveData;
    }

    public void setStudentTimeMutableLiveData(StudentTime studentTimeMutableLiveData) {
        this.studentTimeMutableLiveData.setValue(studentTimeMutableLiveData);
    }

    public MutableLiveData<Boolean> getStudent() {
        return isStudent;
    }

    public void setStudent(Boolean student) {
        isStudent.setValue(student);
    }

    public MutableLiveData<String> getPlace() {
        return place;
    }

    public void setPlace(MutableLiveData<String> place) {
        this.place = place;
    }

    public MutableLiveData<Boolean> getIsScrolling() {
        return isScrolling;
    }

    public void setIsScrolling(Boolean isScrolling) {
        this.isScrolling.setValue(isScrolling);
    }

    public MutableLiveData<Boolean> getIsLastItemReached() {
        return isLastItemReached;
    }

    public void setIsLastItemReached(MutableLiveData<Boolean> isLastItemReached) {
        this.isLastItemReached = isLastItemReached;
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
        place.setValue(getListCities().getValue().get(city));


    }

    public MutableLiveData<Integer> getSelectedRegionIndex() {
        return selectedRegionIndex;
    }

    public void setSelectedRegionIndex(Integer selectedRegionIndex) {
        this.selectedRegionIndex.setValue(selectedRegionIndex);
        String key = String.format("%s-name", context.getValue().getResources().getConfiguration().locale.getLanguage());
        String str = place.getValue() + "-" + getListRegions().getValue().get(getSelectedRegionIndex().getValue()).get(key);
        place.setValue(str);
    }

    public MutableLiveData<String> getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId.setValue(hostId);
        getRoomsWithHostId();
        Log.i(TAG, "setHostId: in host set id");

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

    public MutableLiveData<Boolean> getMakeRefresh() {
        return makeRefresh;
    }

    //Control Refreshing
    public void setMakeRefresh(Boolean makeRefresh1) {

        makeRefresh.setValue(makeRefresh1);
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

    //get document fields and get their values to set in room object
    private Room setRoomModel(DocumentSnapshot d) {
        Room room = new Room();
        room.setPrice(d.getDouble("price"));
        room.setRate(d.getLong("rate").intValue());
        room.setNum_reviews(d.getLong("num_reviews").intValue());
        room.setEnAddress(d.get("enAddress").toString());
        room.setTotalBeds(d.getLong("totalBeds").intValue());
        room.setHostId(d.get("hostId").toString());
        room.setUrlImage1(d.get("urlImage1").toString());
        room.setDepartId(d.get("departId").toString());
        room.setRoomId(d.get("roomId").toString());
        room.setArAddress(d.get("arAddress").toString());
        room.setLatLng(d.get("latLng", MyLatLong.class));
        room.setGender(d.get("gender").toString());
        room.setCityIndex(d.getLong("cityIndex").intValue());
        room.setRegionIndex(d.getLong("regionIndex").intValue());
        room.setDayCost(d.getDouble("dayCost"));
        return room;
    }

    //execute first Query
    public void execute() {
        listRooms.setValue(new ArrayList<>());
        isLastItemReached.setValue(false);

        prepareQuery().limit(limit).get()
                .addOnFailureListener(e -> {
                    Log.i(TAG, "setFilter: Success Execute " + e);
                }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot d : task.getResult()) {
                    listRooms.getValue().add(setRoomModel(d));

                }
                if (task.getResult().size() != 0) {
                    resultsFound.setValue(1);
                    lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);

                } else resultsFound.setValue(0);
                if (task.getResult().size() < limit) {
                    isLastItemReached.setValue(true);
                }
            } else resultsFound.setValue(-1);

        });
    }

    //load more rooms
    public void nextExecute() {
        Log.i(TAG, "nextExecute: ");

        prepareQuery().startAfter(lastVisible).limit(limit).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot d : task.getResult()) {
                            listRooms.getValue().add(setRoomModel(d));
                        }
                        if (task.getResult().size() != 0)
                            lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);

                        if (task.getResult().size() < limit) {
                            isLastItemReached.setValue(true);
                        }
                    }

                }).addOnFailureListener(e -> Log.i(TAG, "onFailure: "));

    }

    //get rooms with host id that get from Qr code
    private void getRoomsWithHostId() {
        isLastItemReached.setValue(true);

        listRooms.setValue(new ArrayList<>());
        Log.i(TAG, "getRoomsWithHostId: ");
        CollectionReference roomsRef = db.collection("Rooms");
        Query query = roomsRef.whereEqualTo("hostId", hostId.getValue());

        query.get().addOnSuccessListener(
                queryDocumentSnapshots -> {

                }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i(TAG, "getRoomsWithHostId: succesful");
                for (DocumentSnapshot d : task.getResult()) {
                    listRooms.getValue().add(setRoomModel(d));
                    Log.i(TAG, "getRoomsWithHostId: rooms added");
                }
            }
        }).addOnFailureListener(e -> {
            Log.i(TAG, "getRoomsWithHostId: failur");
        });
    }

    //prepare query to execute
    private Query prepareQuery() {
        String price;
        Query query = db.collection("Rooms");
        if (isStudent.getValue())
            price = "price";
        else
            price = "dayCost";

        if (startPrice.getValue() != null)
            query = query.whereGreaterThanOrEqualTo(price, startPrice.getValue());

        if (endPrice.getValue() != null)
            query = query.whereLessThanOrEqualTo(price, endPrice.getValue());

        if (selectedCityIndex.getValue() != null && selectedCityIndex.getValue() != 0)
            query = query.whereEqualTo("cityIndex", selectedCityIndex.getValue());

        if (selectedRegionIndex.getValue() != null && selectedRegionIndex.getValue() != 0 && selectedCityIndex.getValue() != 0)
            query = query.whereEqualTo("regionIndex", selectedRegionIndex.getValue());

        if (gender.getValue() != null)
            query = query.whereEqualTo("gender", gender.getValue().toLowerCase());
        if (numGuests.getValue() != null) {

        }
        return query;
    }


}
