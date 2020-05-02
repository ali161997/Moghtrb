package com.alihashem.moghtreb.repositories;

import android.util.Log;

import com.alihashem.moghtreb.models.MyLatLong;
import com.alihashem.moghtreb.models.Room;
import com.alihashem.moghtreb.models.RoomDetail;
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
        imgs.add("https://firebasestorage.googleapis.com/v0/b/sokna-281e7.appspot.com/o/asd.jpg?alt=media&token=a6cc3cd5-886f-484f-9c72-87b3b47fd32e");
        imgs.add("https://firebasestorage.googleapis.com/v0/b/sokna-281e7.appspot.com/o/pic4.jpg?alt=media&token=1acba683-5596-48b1-bfa9-83f8a5f4d6e8");
        imgs.add("https://firebasestorage.googleapis.com/v0/b/sokna-281e7.appspot.com/o/pic3.jpg?alt=media&token=e85692f3-e4d9-4b84-953f-e05d8678e022");
        String urlIMage = "https://firebasestorage.googleapis.com/v0/b/sokna-281e7.appspot.com/o/asd.jpg?alt=media&token=a6cc3cd5-886f-484f-9c72-87b3b47fd32e";
        Room room1 = new Room(350.0, 3, 20, "El-Bsery street", 2, "atef", "assiut", "Seed Factory", urlIMage, "id1", "room1", "اسيوط /مصنع سييد/شارع البيسرى",
                new MyLatLong(5.0, 10.0), "male", 1, 1);
        Room room2 = new Room(400.0, 2, 10, "City street", 3, "atef", "assiut",
                "Seed Factory", urlIMage, "id1", "room2", "اسيوط /مصنع سييد/شارع السبيل", new MyLatLong(5.0, 10.0), "male", 1, 2);
        Room room3 = new Room(500.0, 1, 15, "street1 street", 1, "atef1", "assiut", "Seed Factory", urlIMage, "id1", "room3", "اسيوط /مصنع سييد/شارع السبيل 1", new MyLatLong(5.0, 10.0), "female", 1, 2);
        Room room4 = new Room(600.0, 5, 18, "street2 street", 2, "atef2", "assiut", "Seed Factory", urlIMage, "id2", "room4", "اسيوط /مصنع سييد/شارع السبيل 2", new MyLatLong(5.0, 10.0), "male", 2, 1);
        Room room5 = new Room(700.0, 4, 17, "street3 street", 1, "atef1", "assiut", "Seed Factory", urlIMage, "id1", "room5", "اسيوط /مصنع سييد/شارع السبيل", new MyLatLong(5.0, 10.0), "male", 1, 2);
        ArrayList<Room> list = new ArrayList<>();
        list.add(room1);
        list.add(room2);
        list.add(room3);
        list.add(room4);
        list.add(room5);
        int c = 0;
        RoomDetail roomDetail = new RoomDetail(3, 3, 1,
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
