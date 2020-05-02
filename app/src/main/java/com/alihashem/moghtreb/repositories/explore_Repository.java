package com.alihashem.moghtreb.repositories;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.alihashem.moghtreb.models.Room;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class explore_Repository {
    private static final String TAG = "explore_Repository";

    private static explore_Repository instance;
    private static MutableLiveData<String> lang;
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
            lang = new MutableLiveData<>();

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
        Min.setValue(50.0);
        return Min;

    }

    public MutableLiveData<Double> getMaxSeekbar() {
        Max.setValue(1000.0);
        return Max;

    }


    //Rooms Functions
    private void setDatasetrooms() {
        datasetrooms.clear();
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
