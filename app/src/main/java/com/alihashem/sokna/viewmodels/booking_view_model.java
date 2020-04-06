package com.alihashem.sokna.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alihashem.sokna.models.Room;
import com.alihashem.sokna.models.RoomDetail;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class booking_view_model extends ViewModel {
    private static final String TAG = "booking_view_model";
    private MutableLiveData<Room> room;
    private FirebaseFirestore db;
    private MutableLiveData<RoomDetail> roomDetail;
    private MutableLiveData<String> roomId;
    public booking_view_model() {
        room = new MutableLiveData<>();
        roomId = new MutableLiveData<>();
        roomDetail = new MutableLiveData<>();
        db = FirebaseFirestore.getInstance();
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


}
