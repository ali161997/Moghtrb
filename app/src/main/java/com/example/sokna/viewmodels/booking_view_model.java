package com.example.sokna.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sokna.models.Room;
import com.example.sokna.models.RoomDetail;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class booking_view_model extends ViewModel {
    private static final String TAG = "booking_view_model";
    private MutableLiveData<Room> room;
    private FirebaseFirestore db;
    private MutableLiveData<RoomDetail> roomDtail;
    public booking_view_model() {
        room = new MutableLiveData<>();
        roomDtail = new MutableLiveData<>();
        db = FirebaseFirestore.getInstance();
    }
    public MutableLiveData<Room> getRoom() {
        return room;
    }
    public void set_room(Room room1) {
        room.setValue(room1);
        getRoomRepo();
    }
    private void getRoomRepo() {
        //room.getValue().getDetailLink()
        DocumentReference docRef = db.collection("Rooms").document("host:0").collection("Detail0").document("hzJe1CsyQgZFaEapBZV2");
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    roomDtail.setValue(document.toObject(RoomDetail.class));
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }
    public MutableLiveData<RoomDetail> getRoomDtail() {
        //roomDtail.setValue(new RoomDetail(400.0, 2, 4, 2, 3, "assiut", true, 4, true, true, true, "assd"));
        return roomDtail;
    }


}
