package com.example.sokna.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sokna.models.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Editing_view_model extends ViewModel {

    private MutableLiveData<user> userData;
    private static final String TAG = "Editing_view_model";
    private FirebaseFirestore db;
    private MutableLiveData<List<String>> colleges;

    public MutableLiveData<Boolean> getUpdateCompleted() {
        return updateCompleted;
    }

    private MutableLiveData<Boolean> updateCompleted;

    public Editing_view_model() {
        userData = new MutableLiveData<>();
        db = FirebaseFirestore.getInstance();
        updateCompleted = new MutableLiveData<>();
        colleges = new MutableLiveData<>();
        downloadUserData();
    }

    public MutableLiveData<user> getUserData() {

        return userData;
    }

    public void setUserData(user user) {
        userData.setValue(user);
        UpdateUser();
    }


    private void downloadUserData() {
        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    userData.setValue(document.toObject(user.class));
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    private void UpdateUser() {
        db.collection("users").document(FirebaseAuth.getInstance().getUid())
                .set(userData.getValue())
                .addOnSuccessListener(aVoid -> updateCompleted.setValue(true))
                .addOnFailureListener(e -> updateCompleted.setValue(false));
    }

    public MutableLiveData<List<String>> getColleges() {

        List<String> college = new ArrayList<>();
        college.add("Faculty");
        college.add("Computers and Information");
        college.add("Agriculture");
        college.add("Law");
        college.add("Sciences");
        college.add("Commerce");
        colleges.setValue(college);


        return colleges;
    }


}
