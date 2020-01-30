package com.example.sokna.Repository;

import android.util.Log;

import com.example.sokna.models.Room;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Uploading_explore {
    private static final String TAG = "Upload";
    private List<Room> roomSet = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Uploading_explore() {

    }



    private void perform_opload() {
        db.collection("users")
                .document("5XCsYesOlm959Nksslgc")
                .collection("search_history")
                .document("search1").set("sa")
                .addOnSuccessListener(aVoid -> Log.i("if secces uplading", "Success uploading"))
                .addOnFailureListener(e -> Log.i("if failed uplading", "failed uploading  " + e.getMessage()));

    }

    public void settoUpload() {
       /* ArrayList<String>imgs=new ArrayList<>();
        imgs.add("https://firebasestorage.googleapis.com/v0/b/sokna-281e7.appspot.com/o/asd.jpg?alt=media&token=a6cc3cd5-886f-484f-9c72-87b3b47fd32e");
        imgs.add("https://firebasestorage.googleapis.com/v0/b/sokna-281e7.appspot.com/o/pic4.jpg?alt=media&token=1acba683-5596-48b1-bfa9-83f8a5f4d6e8");
        imgs.add("https://firebasestorage.googleapis.com/v0/b/sokna-281e7.appspot.com/o/pic3.jpg?alt=media&token=e85692f3-e4d9-4b84-953f-e05d8678e022");
        imgs.add("https://firebasestorage.googleapis.com/v0/b/sokna-281e7.appspot.com/o/pic6.jpg?alt=media&token=dfdc2bd9-1f5d-486b-b486-f9cd5ab1f802");
        Room room1=new Room(400.0,3,100,"assiut-asyut-seed factoty-shehab street",2,"firstLink",
                imgs);
        roomSet.add(room1);
        roomSet.add(room1);
        roomSet.add(room1);
        roomSet.add(room1);
        roomSet.add(room1);
        roomSet.add(room1);
        roomSet.add(room1);
        roomSet.add(room1);
        RoomDetail roomDetail=new RoomDetail(400.0, 2, 4, 2, 3, "assiut", true, 4, true, true, true, "assd");

        for(int i=0;roomSet.size()>i;i++)
        {
            db.collection("Rooms").document("host:"+i).set(room1).addOnSuccessListener(aVoid -> {
                Log.d(TAG, "Success Adding");

            }).addOnFailureListener(avoid->
            {
                Log.d(TAG, "fail Adding");
            });
        }
        for(int i=0;roomSet.size()>i;i++)
        {
            db.collection("Rooms").document("host:"+i).collection("Detail"+i).document().set(roomDetail).addOnSuccessListener(aVoid -> {
                Log.d(TAG, "Success Adding");

            }).addOnFailureListener(avoid->
            {
                Log.d(TAG, "fail Adding");
            });
        }*/


    }


}
