package com.moghtrb.repositories;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.moghtrb.models.Room;

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
    private MutableLiveData<List<String>> allCities = new MutableLiveData<>();

    public static explore_Repository getInstance() {
        if (instance == null) {
            instance = new explore_Repository();

        }
        return instance;

    }

    public MutableLiveData<List<String>> getAllCities() {
        return allCities;
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
                    regionMaps.setValue((List<Map<String, String>>) document.get(fieldName));
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
        Max.setValue(4000.0);
        return Max;
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
                    cities.setValue((List<String>) document.get(fieldName));
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

        return faculties;
    }

    public void setLang(String lang) {

        explore_Repository.lang.setValue(lang);
        setFaculties();
        downloadAllCities();
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

    private void downloadAllCities() {

        String fieldName;
        if (lang.getValue().equals("ar"))
            fieldName = "ar-cities";
        else
            fieldName = "en-cities";
        Log.i(TAG, "setCities: " + fieldName);


        db.collection("Egypt").document("allCities")
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    allCities.setValue((List<String>) document.get(fieldName));
                    Log.d(TAG, "succes getting cities");

                } else {
                    Log.d(TAG, "No such document cities");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });


    }


}
