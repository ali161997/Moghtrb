package com.alihashem.sokna.viewmodels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alihashem.sokna.models.MyLatLong;
import com.alihashem.sokna.models.Room;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
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
        CollectionReference roomsRef = db.collection("Rooms");
        roomsRef.get().addOnSuccessListener(
                queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> list_rooms = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list_rooms) {
                            Room room = d.toObject(Room.class);
                            if (distance(room.getLatLng().getLat(), room.getLatLng().getLon(), PlaceSelectedlatlng.getValue().getLat(), PlaceSelectedlatlng.getValue().getLon()) < .3)
                                list.add(room);
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
