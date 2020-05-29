package com.alihashem.moghtrb.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alihashem.moghtrb.models.MyLatLong;
import com.alihashem.moghtrb.models.Room;
import com.alihashem.moghtrb.models.RoomDetail;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookViewModel extends ViewModel {
    private static final String TAG = "booking_view_model";
    private MutableLiveData<Room> roomIn;
    private FirebaseFirestore db;
    private MutableLiveData<RoomDetail> roomDetail;
    private MutableLiveData<String> roomId;
    private MutableLiveData<Boolean> completed;
    private MutableLiveData<Integer> numGuests;
    private MutableLiveData<String> Date;
    private MutableLiveData<String> type;


    public BookViewModel() {
        roomIn = new MutableLiveData<>();
        roomId = new MutableLiveData<>();
        roomDetail = new MutableLiveData<>();
        numGuests = new MutableLiveData<>();
        Date = new MutableLiveData<>();
        type = new MutableLiveData<>();
        completed = new MutableLiveData<>();
        db = FirebaseFirestore.getInstance();
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

    public void setDate(String date) {
        Date.setValue(date);
    }

    public MutableLiveData<String> getType() {
        return type;
    }

    public void setType(String type) {
        this.type.setValue(type);
    }

    public void setRoomId(String hostId) {
        this.roomId.setValue(hostId);
        getRoomFromDatabase();
        getRoomDetailFromDatabase();
    }

    private void getRoomFromDatabase() {
        DocumentReference docRef = db.collection("Rooms").document(roomId.getValue());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot d = task.getResult();
                if (d.exists()) {
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
                    roomIn.setValue(room);
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
                    RoomDetail roomDetail1 = new RoomDetail();
                    roomDetail1.setNumRoomsInDepart(document.getLong("numRoomsInDepart").intValue());
                    roomDetail1.setNumBath(document.getLong("numBath").intValue());
                    roomDetail1.setDepartOrder(document.getLong("departOrder").intValue());
                    roomDetail1.setHaveBalcony(document.getBoolean("haveBalcony"));
                    roomDetail1.setHostRate(document.getDouble("hostRate"));
                    roomDetail1.setServices(((HashMap<String, Boolean>) document.get("services")));
                    roomDetail1.setUrlsImage((ArrayList<String>) document.get("urlsImage"));
                    roomDetail.setValue(roomDetail1);
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    public MutableLiveData<RoomDetail> getRoomDetail() {
        return roomDetail;
    }

    public MutableLiveData<Room> getRoom() {
        return roomIn;
    }

    public MutableLiveData<Boolean> sendRequest(String userId) {
        Map<String, Object> request = new HashMap<>();
        Timestamp timestamp = Timestamp.now();
        request.put("roomId", roomId.getValue());
        request.put("userId", userId);
        request.put("timeRequest", timestamp);
        request.put("type", type.getValue());
        request.put("numGuests", numGuests.getValue());
        request.put("dates", Date.getValue());
        db.collection("Requests").document()
                .set(request)
                .addOnSuccessListener(aVoid -> {
                    completed.setValue(true);
                })
                .addOnFailureListener(e -> completed.setValue(false));

        return completed;
    }


}
