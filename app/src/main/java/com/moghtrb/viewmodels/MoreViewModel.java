package com.moghtrb.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MoreViewModel extends ViewModel {
    private static final String TAG = "MenuViewModel";
    private MutableLiveData<String> Username;
    private MutableLiveData<String> userImage;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private MutableLiveData<List<String>> items;
    private ArrayList<String> choices;
    private MutableLiveData<Boolean> profileCompleted;

    public MoreViewModel() {
        Username = new MutableLiveData<>("name");
        userImage = new MutableLiveData<>();
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        choices = new ArrayList<>();
        items = new MutableLiveData<>();
        profileCompleted = new MutableLiveData<>();

    }


    public MutableLiveData<Boolean> getProfileCompleted() {
        return profileCompleted;
    }

    public void setProfileCompleted(MutableLiveData<Boolean> profileCompleted) {
        this.profileCompleted = profileCompleted;
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


    private void setData() {

        DocumentReference docRef = db.collection("users")
                .document(firebaseAuth.getUid());
        docRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    try {

                        Log.i(TAG, "setData try: " + FirebaseAuth.getInstance().getUid());
                        userImage.setValue(documentSnapshot.get("photoUrl").toString());
                        Username.setValue(documentSnapshot.get("name").toString());
                        if (profileCompletedFun(documentSnapshot))
                            profileCompleted.setValue(true);
                        else profileCompleted.setValue(false);
                    } catch (Exception e) {
                        Log.i(TAG, "setData: " + FirebaseAuth.getInstance().getUid());
                    }

                }).addOnFailureListener(e -> {
            Log.i("Profile View Model", "Error");
        });

    }

    private boolean profileCompletedFun(DocumentSnapshot doc) {
        return doc.contains("completed") && doc.getBoolean("completed");

    }


    private void updateIMageReference() {
        db.collection("users").document(firebaseAuth.getUid())
                .update(
                        "photoUrl", userImage.getValue()
                );


    }
}
