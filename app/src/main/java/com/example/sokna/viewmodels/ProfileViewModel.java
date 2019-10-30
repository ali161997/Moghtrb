package com.example.sokna.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sokna.models.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ProfileViewModel extends ViewModel {
    private MutableLiveData<String> Username;
    private MutableLiveData<String> userImage;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private MutableLiveData<List<String>> items;
    private ArrayList<String> choices;

    public ProfileViewModel() {
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

        DocumentReference docRef = db.collection("users").document(firebaseAuth.getUid());
        docRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    user user1 = documentSnapshot.toObject(user.class);
                    userImage.setValue(user1.getPhotoUrl());
                    Username.setValue(user1.getName());
                    Log.i("Profile View Model", "Correct" + user1.getName() + " ");
                    Log.i("Profile View Model", "Correct" + user1.getPhotoUrl() + " ");

                }).addOnFailureListener(e -> {
            Log.i("Profile View Model", "Error");
        });

    }

    private void listViewData() {

        choices.add("List of your space");
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
