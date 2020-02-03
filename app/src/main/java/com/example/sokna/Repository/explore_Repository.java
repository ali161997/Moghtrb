package com.example.sokna.Repository;


import androidx.lifecycle.MutableLiveData;

import com.example.sokna.models.Room;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class explore_Repository {
    private static final String TAG = "explore_Repository";

    private static explore_Repository instance;
    private ArrayList<Room> datasetrooms = new ArrayList<>();
    private MutableLiveData<Double> Min = new MutableLiveData<>();
    private MutableLiveData<Double> Max = new MutableLiveData<>();
    private Double min, max;
    private MutableLiveData<List<Room>> datalist = new MutableLiveData<>();
    private MutableLiveData<List<String>> cities = new MutableLiveData<>();
    private MutableLiveData<List<String>> regions = new MutableLiveData<>();
    MutableLiveData<List<Double>> minMax = new MutableLiveData<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    public static explore_Repository getInstance() {
        if (instance == null) {
            instance = new explore_Repository();

        }
        return instance;

    }

    public MutableLiveData<List<String>> getCities() {
        //set cities and return it
        setCities();
        return cities;
    }

    public MutableLiveData<List<String>> getRegions() {

        return regions;
    }

    public void setSelectedCity(String City) {
        setRegions(City);

    }

    private void setRegions(String city) {
        List<String> regionsDo = new ArrayList<>();
        regionsDo.add("select Region");
        regionsDo.add("seed1");
        regionsDo.add("seed4");
        regionsDo.add("akhmim");
        regionsDo.add("akhmim");
        regions.setValue(regionsDo);


    }

    public MutableLiveData<List<Room>> getdata() {
        setDatasetrooms();

        datalist.setValue(datasetrooms);
        return datalist;
    }

    public MutableLiveData<Double> getMinSeekbar() {
        setMinMaxSeekBar();
        Min.setValue(min);
        return Min;

    }

    public MutableLiveData<Double> getMaxSeekbar() {
        setMinMaxSeekBar();
        Max.setValue(max);
        return Max;

    }

    private void setMinMaxSeekBar() {
        min = 1.0;
        max = 1500.0;
    }
    private void setDatasetrooms() {

        db.collection("Rooms")
                .get().addOnSuccessListener(
                queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> list_rooms = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list_rooms) {
                            Room room = d.toObject(Room.class);
                            datasetrooms.add(room);
                        }
                    }

                }).addOnFailureListener(e -> {
        });


    }

    private void setCities() {
        //get Cities from database
        List<String> ci = new ArrayList<>();
        ci.add("Select City");
        ci.add("assiut");
        ci.add("assiut");
        ci.add("assiut");
        ci.add("assiut");
        cities.setValue(ci);
    }


}
