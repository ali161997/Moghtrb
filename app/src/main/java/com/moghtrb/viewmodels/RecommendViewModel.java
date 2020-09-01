package com.moghtrb.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.moghtrb.models.MyLatLong;
import com.moghtrb.models.Room;

import java.util.List;

public class RecommendViewModel extends ViewModel {
    MutableLiveData<List<Room>> rooms;

    public RecommendViewModel() {
        rooms = new MutableLiveData<>();
    }


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
}
