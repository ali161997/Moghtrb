package com.alihashem.moghtrb.repositories;

import android.util.Log;

import com.alihashem.moghtrb.models.MyLatLong;
import com.alihashem.moghtrb.models.Room;
import com.alihashem.moghtrb.models.RoomDetail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Uploading_explore {
    private static final String TAG = "Upload";
    private List<Room> roomSet = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Uploading_explore() {

    }


    public void performUploadSearch(HashMap<String, String> hashMap) {
        db.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("search_history")
                .document("search1").set(hashMap)
                .addOnSuccessListener(aVoid -> Log.i("if secces uplading", "Success uploading"))
                .addOnFailureListener(e -> Log.i("if failed uplading", "failed uploading  " + e.getMessage()));

    }

    public void settoUpload() {
        HashMap<String, Boolean> ser = new HashMap<>();
        ser.put("isInternet", true);
        ser.put("isGas", true);
        ser.put("isCleaning", false);
        ArrayList<String> imgs = new ArrayList<>();

        imgs.add("https://firebasestorage.googleapis.com/v0/b/sokna-281e7.appspot.com/o/image2.jpg?alt=media&token=809b6d98-7c79-4327-a2ea-15ffc9e3fbae");
        imgs.add("https://firebasestorage.googleapis.com/v0/b/sokna-281e7.appspot.com/o/image4.jpg?alt=media&token=530c0174-c43a-431f-a964-2ae7bedeecb6");
        imgs.add("https://firebasestorage.googleapis.com/v0/b/sokna-281e7.appspot.com/o/image3jpg.jpg?alt=media&token=32759828-21d5-4572-b1ec-46fc937d8418");
        String urlIMage = "https://firebasestorage.googleapis.com/v0/b/sokna-281e7.appspot.com/o/image1.jpg?alt=media&token=edf5c1e4-dfe9-411f-a18d-aa4041a4f49d";
        Room room1 = new Room(
                350.0,
                3,
                20,
                "El-Bsery street",
                2,
                5,
                "atef",
                "assiut",
                "Seed Factory",
                urlIMage,
                "id1",
                "room1",
                "اسيوط /مصنع سييد/شارع البيسرى",
                new MyLatLong(5.0, 10.0), "male", 1, 1);


        Room room2 = new Room(400.0, 2, 10, "City street", 3, 7, "atef", "assiut",
                "Seed Factory", urlIMage, "id1", "room2", "اسيوط /مصنع سييد/شارع السبيل", new MyLatLong(5.0, 10.0), "male", 1, 2);


        Room room3 = new Room(500.0, 1, 15, "street1 street", 2, 3, "atef1", "assiut", "Seed Factory", urlIMage, "id1", "room3", "اسيوط /مصنع سييد/شارع السبيل 1", new MyLatLong(5.0, 10.0), "female", 1, 2);
        Room room4 = new Room(600.0, 5, 18, "street2 street", 2, 4, "atef2", "assiut", "Seed Factory", urlIMage, "id2", "room4", "اسيوط /مصنع سييد/شارع السبيل 2", new MyLatLong(5.0, 10.0), "male", 2, 1);
        Room room5 = new Room(700.0, 4, 17, "street3 street", 1, 2, "atef1", "assiut", "Seed Factory", urlIMage, "id1", "room5", "اسيوط /مصنع سييد/شارع السبيل", new MyLatLong(5.0, 10.0), "male", 1, 2);
        ArrayList<Room> list = new ArrayList<>();
        list.add(room1);
        list.add(room2);
        list.add(room3);
        list.add(room4);
        list.add(room5);
        int c = 0;
        RoomDetail roomDetail = new RoomDetail(3, 1,
                3.0, true, 4, ser, imgs);
        for (Room r : list) {
            c++;
            db.collection("Rooms").document("room" + c).set(r).addOnSuccessListener(aVoid -> {
                Log.d(TAG, "Success Adding");

            }).addOnFailureListener(avoid ->
            {
                Log.d(TAG, "fail Adding");
            });
            db.collection("Rooms").document("room" + c).collection("Detail").document("RoomDetail").set(roomDetail).addOnSuccessListener(aVoid -> {
                Log.d(TAG, "Success Adding");

            }).addOnFailureListener(avoid ->
            {
                Log.d(TAG, "fail Adding");
            });
        }

    }


}
