package com.moghtrb.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.moghtrb.models.MyLatLong;
import com.moghtrb.models.Room;
import com.moghtrb.models.RoomDetailModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class RoomDetailViewModel extends ViewModel {
    private static final String TAG = "booking_view_model";
    private MutableLiveData<Room> roomIn;
    private MutableLiveData<RoomDetailModel> roomDetail;
    private MutableLiveData<String> roomId;
    private MutableLiveData<Integer> totalPlacesAvailable;
    private MutableLiveData<String> Date;
    private MutableLiveData<String> type;
    private MutableLiveData<Integer> numGuests;
    private MutableLiveData<List<String>> keysFloors;
    private MutableLiveData<List<Integer>> valuesFloors;
    private MutableLiveData<String> selectedFloor;
    private MutableLiveData<List<String>> listImgs;
    private MutableLiveData<Integer> numDays;
    private FirebaseFirestore db;

    public RoomDetailViewModel() {
        roomIn = new MutableLiveData<>();
        numDays = new MutableLiveData<>();
        selectedFloor = new MutableLiveData<>();
        totalPlacesAvailable = new MutableLiveData<>(3);
        roomId = new MutableLiveData<>();
        roomDetail = new MutableLiveData<>();
        numGuests = new MutableLiveData<>(1);
        Date = new MutableLiveData<>();
        type = new MutableLiveData<>();
        keysFloors = new MutableLiveData<>();
        valuesFloors = new MutableLiveData<>();
        listImgs = new MutableLiveData<>(new ArrayList<>());
        db = FirebaseFirestore.getInstance();
    }

    public MutableLiveData<Integer> getNumDays() {
        return numDays;
    }

    public void setNumDays(Integer numDays) {
        this.numDays.setValue(numDays);
    }

    public MutableLiveData<String> getRoomId() {
        return roomId;
    }

    public void setRoomId(String hostId) {
        this.roomId.setValue(hostId);
        getRoomFromDatabase();
        getRoomDetailFromDatabase();
    }

    public MutableLiveData<List<String>> getListImgs() {
        return listImgs;
    }

    public void setListImgs(MutableLiveData<List<String>> listImgs) {
        this.listImgs = listImgs;
    }

    public MutableLiveData<String> getSelectedFloor() {
        return selectedFloor;
    }

    public void setSelectedFloor(String selectedFloor) {
        this.selectedFloor.setValue(selectedFloor);
    }

    public MutableLiveData<List<Integer>> getValuesFloors() {

        return valuesFloors;
    }

    public void setValuesFloors(MutableLiveData<List<Integer>> valuesFloors) {
        this.valuesFloors = valuesFloors;
    }

    public MutableLiveData<List<String>> getKeysFloors() {
        return keysFloors;
    }

    public void setKeysFloors(MutableLiveData<List<String>> keysFloors) {
        this.keysFloors = keysFloors;
    }

    public MutableLiveData<Integer> getTotalPlacesAvailable() {
        return totalPlacesAvailable;
    }

    public void setTotalPlacesAvailable(Integer totalPlacesAvailable) {
        this.totalPlacesAvailable.setValue(totalPlacesAvailable);
    }


    public MutableLiveData<Integer> getNumGuests() {
        return numGuests;
    }

    public void setNumGuests(Integer numGuests) {
        this.numGuests.setValue(numGuests);
    }

    public MutableLiveData<String> getDate() {
        return Date;
    }

    public void setDate(MutableLiveData<String> date) {
        Date = date;
    }

    public void setDate(String date) {
        Date.setValue(date);
    }

    public MutableLiveData<String> getType() {
        return type;
    }

    public void setType(String type) {
        this.type.setValue(type);
    }

    private void prepareKeyFloors(HashMap<String, Integer> map) {
        Set<String> keySet = map.keySet();
        keysFloors.setValue(new ArrayList<>(keySet));
    }

    private void prepareValuesFloors(HashMap<String, Integer> map) {
        Collection<Integer> values = map.values();
        valuesFloors.setValue(new ArrayList<>(values));
    }

    private void getRoomFromDatabase() {
        DocumentReference docRef = db.collection("Rooms").document(roomId.getValue());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot d = task.getResult();
                if (d.exists()) {
                    listImgs.getValue().add(d.get("urlImage1").toString());
                    roomIn.setValue(setRoom(d));
                    Log.d(TAG, "DocumentSnapshot data: " + d.getData());
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    private void getRoomDetailFromDatabase() {
        DocumentReference docRef = db.collection("Rooms").document(roomId.getValue()).collection("Detail").document("RoomDetail");
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    List<String> temp = (ArrayList<String>) document.get("urlsImage");
                    for (String e : temp
                    ) {
                        listImgs.getValue().add(e);

                    }
                    roomDetail.setValue(setRoomDetail(document));

                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    private Room setRoom(DocumentSnapshot d) {
        Room room = new Room();
        room.setPrice(d.getDouble("price"));
        room.setRate(d.getLong("rate").intValue());
        room.setNum_reviews(d.getLong("num_reviews").intValue());
        room.setEnAddress(d.get("enAddress").toString());
        room.setTotalBeds(d.getLong("totalBeds").intValue());
        room.setHostId(d.get("hostId").toString());

        room.setDepartId(d.get("departId").toString());
        room.setRoomId(d.get("roomId").toString());
        room.setArAddress(d.get("arAddress").toString());

        HashMap<String, Double> latlong = (HashMap<String, Double>) d.get("latLng");
        room.setLatLng(new MyLatLong(latlong.get("lat"), latlong.get("lon")));
        Log.i(TAG, "getRoomFromDatabase: " + latlong.get("lat") + "" + latlong.get("lon"));
        room.setGender(d.get("gender").toString());
        room.setCityIndex(d.getLong("cityIndex").intValue());
        room.setRegionIndex(d.getLong("regionIndex").intValue());
        return room;
    }

    private RoomDetailModel setRoomDetail(DocumentSnapshot document) {
        RoomDetailModel roomDetail1 = new RoomDetailModel();
        roomDetail1.setNumRoomsInDepart(document.getLong("numRoomsInDepart").intValue());
        roomDetail1.setNumBath(document.getLong("numBath").intValue());
        roomDetail1.setHaveBalcony(document.getBoolean("haveBalcony"));
        roomDetail1.setHostRate(document.getDouble("hostRate"));
        roomDetail1.setFloors((HashMap<String, Integer>) document.get("floor"));
        prepareKeyFloors(roomDetail1.getFloors());
        prepareValuesFloors(roomDetail1.getFloors());
        roomDetail1.setServices(((HashMap<String, Boolean>) document.get("services")));

        roomDetail1.setHostComment(document.getString("hostComment"));
        return roomDetail1;

    }

    public MutableLiveData<RoomDetailModel> getRoomDetail() {
        return roomDetail;
    }

    public MutableLiveData<Room> getRoom() {
        return roomIn;
    }


}
