package com.example.sokna.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sokna.models.Room;

public class booking_view_model extends ViewModel {
    private MutableLiveData<Room> room;

    public booking_view_model() {
        room = new MutableLiveData<>();
    }

    public MutableLiveData<Room> getRoom() {
        return room;
    }

    public void set_room(Room room1) {
        room.setValue(room1);
    }

}
