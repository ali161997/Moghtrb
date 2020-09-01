package com.moghtrb.viewmodels;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.libraries.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.moghtrb.models.MyLatLong;
import com.moghtrb.models.Room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapViewModel extends ViewModel {
    private static final String TAG = "MapViewModel";
    private MutableLiveData<String> PlaceSelectedName;
    private MutableLiveData<MyLatLong> PlaceSelectedlatlng;
    private MutableLiveData<Integer> sheetState;
    private MutableLiveData<Context> context;
    private MutableLiveData<List<Room>> listRooms;
    private FirebaseFirestore db;
    private MutableLiveData<Boolean> isScrolling;
    private MutableLiveData<Boolean> isLastItemReached;
    private DocumentSnapshot lastVisible;
    private int limit = 10;

    public MapViewModel() {
        db = FirebaseFirestore.getInstance();
        isScrolling = new MutableLiveData<>(false);
        isLastItemReached = new MutableLiveData<>(false);
        PlaceSelectedName = new MutableLiveData<>();
        PlaceSelectedlatlng = new MutableLiveData<>();
        context = new MutableLiveData<>();
        listRooms = new MutableLiveData<>(new ArrayList<>());
        sheetState = new MutableLiveData<>(BottomSheetBehavior.STATE_COLLAPSED);


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

    public void setIsLastItemReached(Boolean isLastItemReached) {
        this.isLastItemReached.setValue(isLastItemReached);
    }

    public MutableLiveData<List<Room>> getListRooms() {

        return listRooms;
    }

    public MutableLiveData<Context> getContext() {
        return context;
    }

    public void setContext(Context context1) {

        this.context.setValue(context1);
    }

    public MutableLiveData<Integer> getSheetState() {
        return sheetState;
    }

    public void setSheetState(Integer sheetState) {

        this.sheetState.setValue(sheetState);
    }

    public MutableLiveData<String> getPlaceSelectedName() {
        return PlaceSelectedName;
    }

    public void setPlaceSelectedName(String placeSelectedName) {
        PlaceSelectedName.setValue(placeSelectedName);

    }

    public MutableLiveData<MyLatLong> getPlaceSelectedlatlng() {
        return PlaceSelectedlatlng;

    }

    public void setPlaceSelectedlatlng(MyLatLong placeSelectedlatlng) {
        PlaceSelectedlatlng.setValue(placeSelectedlatlng);
    }

    public void downloadRooms() {
        listRooms.setValue(new ArrayList<>());
        isLastItemReached.setValue(false);
        db.collection("Rooms").
                limit(limit).
                get().addOnSuccessListener(
                queryDocumentSnapshots -> {

                }).addOnFailureListener(e -> {
            Log.i(TAG, "setFilter: failed search bitween " + e);
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot d : task.getResult()) {
                    try {
                        HashMap<String, Double> latlong = (HashMap<String, Double>) d.get("latLng");
                        Log.i(TAG, "downloadRooms: roomtest");
                        LatLng myLocation = new LatLng(getPlaceSelectedlatlng().getValue().getLat(), getPlaceSelectedlatlng().getValue().getLon());
                        LatLng roomLocation = new LatLng(latlong.get("lat"), latlong.get("lon"));
                        if (distanceBetween(myLocation, roomLocation) < 500.0) {
                            listRooms.getValue().add(getRoom(d));
                            Log.i(TAG, "downloadRooms: room added");
                        }

                    } catch (Exception e) {
                        Log.i(TAG, "downloadRooms: " + e.getMessage());
                    }
                }
                if (task.getResult().size() != 0)
                    lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
                if (task.getResult().size() < limit) {
                    isLastItemReached.setValue(true);
                }

                Log.i(TAG, "setFilter: Success search between  ");
            }
        });
    }

    public void downloadNextRooms() {
        db.collection("Rooms").
                whereEqualTo("exist", true)
                .limit(limit)
                .startAfter(lastVisible)
                .get().addOnSuccessListener(
                queryDocumentSnapshots -> {

                }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot d : task.getResult()) {
                    {
                        HashMap<String, Double> latlong;
                        latlong = (HashMap<String, Double>) d.get("latLng");
                        LatLng myLocation = new LatLng(getPlaceSelectedlatlng().getValue().getLat(), getPlaceSelectedlatlng().getValue().getLon());
                        LatLng roomLocation = new LatLng(latlong.get("lat"), latlong.get("lon"));
                        if (distanceBetween(myLocation, roomLocation) < 500.0) {
                            listRooms.getValue().add(getRoom(d));
                        }
                    }

                }
            }

        }).addOnFailureListener(e -> {
            Log.i(TAG, "setFilter: failed search failed getting near rooms" + e);
        });
    }

    private Room getRoom(DocumentSnapshot d) {
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
        room.setGender(d.get("gender").toString());
        room.setCityIndex(d.getLong("cityIndex").intValue());
        room.setRegionIndex(d.getLong("regionIndex").intValue());
        return room;
    }

    private float distanceBetween(LatLng latLng1, LatLng latLng2) {

        Location loc1 = new Location(LocationManager.GPS_PROVIDER);
        Location loc2 = new Location(LocationManager.GPS_PROVIDER);

        loc1.setLatitude(latLng1.latitude);
        loc1.setLongitude(latLng1.longitude);

        loc2.setLatitude(latLng2.latitude);
        loc2.setLongitude(latLng2.longitude);
        Log.i(TAG, "distanceBetween: " + loc1.distanceTo(loc2));
        return loc1.distanceTo(loc2);
    }
}
