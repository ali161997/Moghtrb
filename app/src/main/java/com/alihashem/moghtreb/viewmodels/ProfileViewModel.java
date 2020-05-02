package com.alihashem.moghtreb.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alihashem.moghtreb.models.User;
import com.alihashem.moghtreb.repositories.explore_Repository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<User> userData;
    private static final String TAG = "Editing_view_model";
    private FirebaseFirestore db;
    private explore_Repository repository;
    public MutableLiveData<Boolean> getUpdateCompleted() {
        return updateCompleted;
    }

    private MutableLiveData<Boolean> updateCompleted;

    public ProfileViewModel() {
        repository = explore_Repository.getInstance();
        userData = new MutableLiveData<>();
        db = FirebaseFirestore.getInstance();
        updateCompleted = new MutableLiveData<>();
        downloadUserData();
    }

    public MutableLiveData<User> getUserData() {

        return userData;
    }

    public void setUserData(User user) {
        userData.setValue(user);
        UpdateUser();
    }


    private void downloadUserData() {
        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    userData.setValue(document.toObject(User.class));
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
        return repository.getFaculties();
    }


}
