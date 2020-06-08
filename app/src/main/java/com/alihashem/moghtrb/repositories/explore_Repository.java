package com.alihashem.moghtrb.repositories;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.alihashem.moghtrb.models.MyLatLong;
import com.alihashem.moghtrb.models.Room;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class explore_Repository {
    private static final String TAG = "explore_Repository";

    private static explore_Repository instance;
    private static MutableLiveData<String> lang = new MutableLiveData<>();
    private ArrayList<Room> datasetrooms = new ArrayList<>();
    private MutableLiveData<Double> Min = new MutableLiveData<>();
    private MutableLiveData<Double> Max = new MutableLiveData<>();
    private MutableLiveData<List<Room>> datalist = new MutableLiveData<>();
    private MutableLiveData<List<String>> cities = new MutableLiveData<>();
    private MutableLiveData<List<String>> regions = new MutableLiveData<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<List<String>> faculties = new MutableLiveData<>();
    private MutableLiveData<Integer> selectedCityIndex = new MutableLiveData<>();
    private MutableLiveData<List<Map<String, String>>> regionMaps = new MutableLiveData<>();

    public static explore_Repository getInstance() {
        if (instance == null) {
            instance = new explore_Repository();

        }
        return instance;

    }

    public MutableLiveData<List<Map<String, String>>> getRegionMaps() {
        return regionMaps;
    }

    public void setSelectedCityIndex(Integer cityIndex) {
        this.selectedCityIndex.setValue(cityIndex);
        DocumentReference docRef = db.collection("Egypt").document("Regions");
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String fieldName = String.format("%s", cityIndex);
                    List<Map<String, String>> group = (List<Map<String, String>>) document.get(fieldName);
                    regionMaps.setValue(group);
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });

    }

    //Min Max Functions

    public MutableLiveData<Double> getMinSeekbar() {
        Min.setValue(20.0);
        return Min;

    }

    public MutableLiveData<Double> getMaxSeekbar() {
        Max.setValue(2000.0);
        return Max;

    }

    //Rooms Functions
    private void setDatasetrooms() {
        datasetrooms.clear();
        db.collection("Rooms")
                .whereEqualTo("exist", true)
                .get().addOnSuccessListener(
                queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> list_rooms = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list_rooms) {
                            try {

                                Room room = new Room();
                                room.setPrice(d.getDouble("price"));
                                room.setRate(d.getLong("rate").intValue());
                                room.setNum_reviews(d.getLong("num_reviews").intValue());
                                room.setEnAddress(d.get("enAddress").toString());
                                room.setBedsAvailable(d.getLong("bedsAvailable").intValue());
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
                                datasetrooms.add(room);
                            } catch (Exception e) {
                                Log.i(TAG, "setDatasetrooms: " + e.getMessage());
                            }
                        }
                    }

                }).addOnFailureListener(e -> {
        });


    }

    public MutableLiveData<List<Room>> getdata() {
        setDatasetrooms();

        datalist.setValue(datasetrooms);
        return datalist;
    }

    //city and region Functions
    public MutableLiveData<List<String>> getCities() {
        setCities();
        return cities;
    }

    private void setCities() {
        //get Cities from database
        DocumentReference docRef = db.collection("Egypt").document("Cities");
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String fieldName = String.format("%s-Cities", lang.getValue());
                    List<String> group = (List<String>) document.get(fieldName);
                    cities.setValue(group);
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    //faculties functions
    public MutableLiveData<List<String>> getFaculties() {
        setFaculties();
        return faculties;
    }

    public void setLang(String lang) {

        explore_Repository.lang.setValue(lang);
    }

    private void setFaculties() {
        Log.i(TAG, "setFaculties: " + lang.getValue());
        String docName;
        if (lang.getValue().equals("ar"))
            docName = "ar-Faculties";
        else
            docName = "en-Faclties";
        Log.i(TAG, "setFaculties: " + docName);

        DocumentReference docRef = db.collection("Faculties").document(docName);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    List<String> group = (List<String>) document.get("faculties");
                    faculties.setValue(group);
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });


    }


}
