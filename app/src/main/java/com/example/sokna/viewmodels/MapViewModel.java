package com.example.sokna.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.libraries.maps.model.LatLng;

import java.util.List;

public class MapViewModel extends ViewModel {
    private MutableLiveData<List<String>> ListPlacesNames;
    private MutableLiveData<List<LatLng>> ListPlaceslatlng;
    private MutableLiveData<String> PlaceSelectedName;
    private MutableLiveData<LatLng> PlaceSelectedlatlng;
    private MutableLiveData<Integer> sheetState;

    public void setListPlacesNames(List<String> listPlacesNames) {
        ListPlacesNames.setValue(listPlacesNames);
    }

    public void setListPlaceslatlng(List<LatLng> listPlaceslatlng) {
        ListPlaceslatlng.setValue(listPlaceslatlng);
    }

    public void setPlaceSelectedName(String placeSelectedName) {
        PlaceSelectedName.setValue(placeSelectedName);
    }

    public void setPlaceSelectedlatlng(LatLng placeSelectedlatlng) {
        PlaceSelectedlatlng.setValue(placeSelectedlatlng);
    }

    public void setSheetState(Integer sheetState) {
        this.sheetState.setValue(sheetState);
    }

    public MapViewModel() {
        ListPlacesNames = new MutableLiveData<>();
        ListPlaceslatlng = new MutableLiveData<>();
        PlaceSelectedName = new MutableLiveData<>();
        PlaceSelectedlatlng = new MutableLiveData<>();
        sheetState = new MutableLiveData<>();

    }

    public MutableLiveData<Integer> getSheetState() {
        return sheetState;
    }

    public MutableLiveData<String> getPlaceSelectedName() {
        return PlaceSelectedName;
    }

    public MutableLiveData<LatLng> getPlaceSelectedlatlng() {
        return PlaceSelectedlatlng;
    }

    public MutableLiveData<List<String>> getListPlacesNames() {
        return ListPlacesNames;
    }

    public MutableLiveData<List<LatLng>> getListPlaceslatlng() {
        return ListPlaceslatlng;
    }

}
