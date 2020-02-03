package com.example.sokna.Repository;

import android.util.Log;

import com.example.sokna.models.Room;
import com.example.sokna.models.RoomDetail;
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
        ArrayList<String> imgs = new ArrayList<>();
        imgs.add("https://firebasestorage.googleapis.com/v0/b/sokna-281e7.appspot.com/o/asd.jpg?alt=media&token=a6cc3cd5-886f-484f-9c72-87b3b47fd32e");
        imgs.add("https://firebasestorage.googleapis.com/v0/b/sokna-281e7.appspot.com/o/pic4.jpg?alt=media&token=1acba683-5596-48b1-bfa9-83f8a5f4d6e8");
        imgs.add("https://firebasestorage.googleapis.com/v0/b/sokna-281e7.appspot.com/o/pic3.jpg?alt=media&token=e85692f3-e4d9-4b84-953f-e05d8678e022");
        imgs.add("https://firebasestorage.googleapis.com/v0/b/sokna-281e7.appspot.com/o/pic6.jpg?alt=media&token=dfdc2bd9-1f5d-486b-b486-f9cd5ab1f802");
        Room room1 = new Room(400.0, 3, 110, "assiut-asyut-seed factoty-shehab street", 2, "firstLink",
                imgs, "assiut", "seed");
        Room room2 = new Room(500.0, 2, 120, "assiut-asyut-seed factoty-shehab street", 1, "firstLink",
                imgs, "assiut", "city");
        Room room3 = new Room(200.0, 3, 130, "assiut-asyut-seed factoty-shehab street", 1, "firstLink",
                imgs, "assiut", "seed");
        Room room4 = new Room(100.0, 4, 140, "assiut-asyut-seed factoty-shehab street", 3, "firstLink",
                imgs, "assiut", "seed1");
        Room room5 = new Room(1000.0, 5, 150, "assiut-asyut-seed factoty-shehab street", 2, "firstLink",
                imgs, "assiut", "seed2");
        Room room6 = new Room(50.0, 1, 160, "assiut-asyut-seed factoty-shehab street", 5, "firstLink",
                imgs, "assiut", "seed3");
        Room room7 = new Room(1500.0, 5, 180, "assiut-asyut-seed factoty-shehab street", 5, "firstLink",
                imgs, "assiut", "seed4");
        roomSet.add(room1);
        roomSet.add(room2);
        roomSet.add(room3);
        roomSet.add(room4);
        roomSet.add(room5);
        roomSet.add(room6);
        roomSet.add(room7);
        roomSet.add(room1);
        RoomDetail roomDetail1 = new RoomDetail(400.0, 2, 4, 2, 3, "assiut", true, 4, true, true, true, "assd");
        int c = 0;
        for (Room room : roomSet)
        {
            db.collection("Rooms").document("host:" + c).set(room).addOnSuccessListener(aVoid -> {
                Log.d(TAG, "Success Adding");

            }).addOnFailureListener(avoid->
            {
                Log.d(TAG, "fail Adding");
            });
            c++;
        }
        c = 0;
        for (Room i : roomSet)
        {
            db.collection("Rooms").document("host:" + c).collection("Detail").document().set(roomDetail1).addOnSuccessListener(aVoid -> {
                Log.d(TAG, "Success Adding");

            }).addOnFailureListener(avoid->
            {
                Log.d(TAG, "fail Adding");
            });
            c++;
        }


    }


}
