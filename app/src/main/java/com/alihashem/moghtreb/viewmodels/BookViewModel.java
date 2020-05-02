package com.alihashem.moghtreb.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alihashem.moghtreb.models.Room;
import com.alihashem.moghtreb.models.RoomDetail;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class BookViewModel extends ViewModel {
    private static final String TAG = "booking_view_model";
    private MutableLiveData<Room> room;
    private FirebaseFirestore db;
    private MutableLiveData<RoomDetail> roomDetail;
    private MutableLiveData<String> roomId;
    private MutableLiveData<Boolean> completed;
    private MutableLiveData<Integer> numGuests;
    private MutableLiveData<String> Date;
    private MutableLiveData<String> type;


    public BookViewModel() {
        room = new MutableLiveData<>();
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
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    room.setValue(document.toObject(Room.class));
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
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
                    roomDetail.setValue(document.toObject(RoomDetail.class));
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
        return room;
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
