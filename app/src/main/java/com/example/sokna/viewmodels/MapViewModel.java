package com.example.sokna.viewmodels;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sokna.models.Room;
import com.google.android.libraries.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MapViewModel extends ViewModel {
    private MutableLiveData<String> PlaceSelectedName;
    private MutableLiveData<LatLng> PlaceSelectedlatlng;
    private List<Room> list;

    public void setSheetState(Integer sheetState) {
        this.sheetState.setValue(sheetState);
    }

    private MutableLiveData<Integer> sheetState;
    private MutableLiveData<Context> context;
    private MutableLiveData<List<Room>> listRooms;
    private static final String TAG = "MapViewModel";
    public MapViewModel() {

        PlaceSelectedName = new MutableLiveData<>();
        PlaceSelectedlatlng = new MutableLiveData<>();
        context = new MutableLiveData<>();
        listRooms = new MutableLiveData<>();
        list = new ArrayList<>();
        downloadRooms();
        listRooms.setValue(list);

    }

    public MutableLiveData<List<Room>> getListRooms() {

        return listRooms;
    }

    public void setContext(Context context1) {

        this.context.setValue(context1);
    }

    public MutableLiveData<Context> getContext() {
        return context;
    }

    public MutableLiveData<Integer> getSheetState() {
        if (sheetState == null) {
            sheetState = new MutableLiveData<>();
        }
        return sheetState;
    }

    public MutableLiveData<String> getPlaceSelectedName() {
        if (PlaceSelectedName.equals(null))
            PlaceSelectedName = new MutableLiveData<>();
        return PlaceSelectedName;
    }

    public MutableLiveData<LatLng> getPlaceSelectedlatlng() {
        if (PlaceSelectedlatlng.equals(null))
            PlaceSelectedName = new MutableLiveData<>();
        return PlaceSelectedlatlng;
    }

    private void downloadRooms() {
        ArrayList<String> imgs = new ArrayList<>();
        imgs.add("https://firebasestorage.googleapis.com/v0/b/sokna-281e7.appspot.com/o/asd.jpg?alt=media&token=a6cc3cd5-886f-484f-9c72-87b3b47fd32e");
        imgs.add("https://firebasestorage.googleapis.com/v0/b/sokna-281e7.appspot.com/o/pic4.jpg?alt=media&token=1acba683-5596-48b1-bfa9-83f8a5f4d6e8");
        imgs.add("https://firebasestorage.googleapis.com/v0/b/sokna-281e7.appspot.com/o/pic3.jpg?alt=media&token=e85692f3-e4d9-4b84-953f-e05d8678e022");
        imgs.add("https://firebasestorage.googleapis.com/v0/b/sokna-281e7.appspot.com/o/pic6.jpg?alt=media&token=dfdc2bd9-1f5d-486b-b486-f9cd5ab1f802");
        Room room1 = new Room(400.0, 3, 100, "assiut-asyut-seed factoty-shehab street", 2, "firstLink",
                imgs, "assiut", "seed4");
        list.add(room1);
        list.add(room1);
    }

}
