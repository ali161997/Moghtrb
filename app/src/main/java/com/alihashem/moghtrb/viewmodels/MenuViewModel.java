package com.alihashem.moghtrb.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MenuViewModel extends ViewModel {
    private MutableLiveData<String> Username;
    private MutableLiveData<String> userImage;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private MutableLiveData<List<String>> items;
    private ArrayList<String> choices;
    private static final String TAG = "MenuViewModel";

    public MenuViewModel() {
        Username = new MutableLiveData<>();
        userImage = new MutableLiveData<>();
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        choices = new ArrayList<>();
        items = new MutableLiveData<>();

    }

    public MutableLiveData<String> getUserImage() {
        setData();
        return userImage;
    }

    public void setImageReference(String ref) {
        userImage.setValue(ref);
        updateIMageReference();
    }


    public MutableLiveData<String> getUsername() {
        return Username;
    }

    public MutableLiveData<List<String>> getItems() {
        listViewData();
        items.setValue(choices);
        return items;
    }


    private void setData() {

        DocumentReference docRef = db.collection("users")
                .document(firebaseAuth.getUid());
        docRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    try {
                        userImage.setValue(documentSnapshot.get("photoUrl").toString());
                    } catch (Exception e) {
                        Log.i(TAG, "setData: " + e.getMessage());
                    }
                    Username.setValue(documentSnapshot.get("name").toString());
                }).addOnFailureListener(e -> {
            Log.i("Profile View Model", "Error");
        });

    }

    private void listViewData() {

        choices.add("Settings");
        choices.add("Refer Host");
        choices.add("Identify Host");
        choices.add("Help");
        choices.add("Give Us Feed back");
        choices.add("Log out");
    }

    private void updateIMageReference() {
        db.collection("users").document(firebaseAuth.getUid())
                .update(
                        "photoUrl", userImage.getValue()
                );


    }
}
