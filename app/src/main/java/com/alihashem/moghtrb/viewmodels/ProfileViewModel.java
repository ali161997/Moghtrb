package com.alihashem.moghtrb.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alihashem.moghtrb.repositories.explore_Repository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class ProfileViewModel extends ViewModel {

    private static final String TAG = "Editing_view_model";
    private MutableLiveData<HashMap<String, Object>> userData;
    private FirebaseFirestore db;
    private explore_Repository repository;
    private MutableLiveData<Boolean> updateCompleted;

    public ProfileViewModel() {
        repository = explore_Repository.getInstance();
        userData = new MutableLiveData<>();
        db = FirebaseFirestore.getInstance();
        updateCompleted = new MutableLiveData<>();
        downloadUserData();
    }

    public MutableLiveData<Boolean> getUpdateCompleted() {
        return updateCompleted;
    }

    public MutableLiveData<HashMap<String, Object>> getUserData() {

        return userData;

    }

    public void setUserData(HashMap<String, Object> user) {
        userData.setValue(user);
        UpdateUser();
    }


    private void downloadUserData() {
        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    HashMap<String, Object> h = (HashMap<String, Object>) document.getData();
                    userData.setValue(h);
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
        db.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .set(userData.getValue())
                .addOnSuccessListener(aVoid -> updateCompleted.setValue(true))
                .addOnFailureListener(e -> updateCompleted.setValue(false));
    }

    public MutableLiveData<List<String>> getColleges() {
        return repository.getFaculties();
    }


}
