package com.example.sokna.Repository;


import androidx.lifecycle.MutableLiveData;

import com.example.sokna.models.Room;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class explore_Repository {

    private static explore_Repository instance;
    private ArrayList<Room> datasetrooms = new ArrayList<>();
    private static ArrayList<Double> MinMaxSeekBar = new ArrayList<>();
    private MutableLiveData<List<Room>> datalist = new MutableLiveData<>();
    MutableLiveData<List<Double>> minMax = new MutableLiveData<>();
    //private static Search search = new Search();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    public static explore_Repository getInstance() {
        if (instance == null) {
            instance = new explore_Repository();

        }
        return instance;

    }

    public MutableLiveData<List<Room>> getdata() {
        setDatasetrooms();

        datalist.setValue(datasetrooms);
        return datalist;
    }

    public MutableLiveData<List<Double>> getMinMaxSeekbar() {
        setMinMaxSeekBar();
        minMax.setValue(MinMaxSeekBar);
        return minMax;

    }

    private void setMinMaxSeekBar() {
        MinMaxSeekBar.add(1.0);
        MinMaxSeekBar.add(100.0);
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

    /*private void setSearch() {
        db.collection("users").document("5XCsYesOlm959Nksslgc").
                collection("search_history").document("search1")
                .get().addOnSuccessListener(documentSnapshot -> {
            search = documentSnapshot.toObject(Search.class);
            Log.i("get search from db", "setSearch: suuccefull");
           // Log.i("start price", search.getFilter().getPrice_start()+" ");
          //  Log.i("end price", search.getFilter().getPrice_end()+" ");

        }).addOnFailureListener(e -> {
            Log.i("get search from db", "setSearch: failed");
        });

    }*/


}
