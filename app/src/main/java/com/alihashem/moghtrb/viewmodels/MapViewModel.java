package com.alihashem.moghtrb.viewmodels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alihashem.moghtrb.models.MyLatLong;
import com.alihashem.moghtrb.models.Room;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
    private List<Room> list;
    private FirebaseFirestore db;

    public MapViewModel() {
        db = FirebaseFirestore.getInstance();
        PlaceSelectedName = new MutableLiveData<>();
        PlaceSelectedlatlng = new MutableLiveData<>();
        context = new MutableLiveData<>();
        listRooms = new MutableLiveData<>();
        list = new ArrayList<>();

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
        if (sheetState == null) {
            sheetState = new MutableLiveData<>();
        }
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
        downloadRooms();
    }

    private void downloadRooms() {
        listRooms.setValue(null);
        list.clear();
        db.collection("Rooms").
                whereEqualTo("exist", true).
                get().addOnSuccessListener(
                queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> list_rooms = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list_rooms) {
                            try {
                                {

                                    Room room = new Room();
                                    HashMap<String, Double> latlong;
                                    latlong = (HashMap<String, Double>) d.get("latLng");
                                    Log.i(TAG, "downloadRooms: " + room.getLatLng());
                                    if (distance(latlong.get("lat"), latlong.get("lon"), PlaceSelectedlatlng.getValue().getLat(), PlaceSelectedlatlng.getValue().getLon()) < .3) {
                                        room.setPrice(d.getDouble("price"));
                                        room.setRate(d.getLong("rate").intValue());
                                        room.setNum_reviews(d.getLong("num_reviews").intValue());
                                        room.setEnAddress(d.get("enAddress").toString());
                                        room.setBedsAvailable(d.getLong("bedsAvailable").intValue());
                                        room.setTotalBeds(d.getLong("totalBeds").intValue());
                                        room.setHostId(d.get("hostId").toString());
                                        room.setUrlImage1(d.get("urlImage1").toString());
                                        room.setDepartId(d.get("departId").toString());
                                        room.setRoomId(d.get("roomId").toString());
                                        room.setArAddress(d.get("arAddress").toString());
                                        room.setGender(d.get("gender").toString());
                                        room.setCityIndex(d.getLong("cityIndex").intValue());
                                        room.setRegionIndex(d.getLong("regionIndex").intValue());
                                        list.add(room);
                                    }
                                }
                            } catch (Exception e) {
                                Log.i(TAG, "downloadRooms: " + e.getMessage());
                            }
                        }
                        listRooms.setValue(list);
                        Log.i(TAG, "setFilter: Success search between  ");
                    }
                }).addOnFailureListener(e -> {
            Log.i(TAG, "setFilter: failed search bitween " + e);
        });
    }

    //returned by kilo
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
