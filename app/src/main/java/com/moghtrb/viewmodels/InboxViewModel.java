package com.moghtrb.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Source;
import com.moghtrb.models.Notification;

import java.util.ArrayList;
import java.util.List;

public class InboxViewModel extends ViewModel {
    private static final String TAG = "InboxViewModel";
    private MutableLiveData<List<Notification>> notyList;
    private MutableLiveData<Boolean> stayRefreshing;

    public InboxViewModel() {

        notyList = new MutableLiveData<>(new ArrayList<>());
        stayRefreshing = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getStayRefreshing() {
        return stayRefreshing;
    }

    public MutableLiveData<List<Notification>> getNotyList() {

        return notyList;
    }

    public void preparedList() {
        notyList.setValue(new ArrayList<>());
        Log.i(TAG, "preparedList: in ");
        FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("Notifications")
                .orderBy("time", Query.Direction.DESCENDING).
                limit(30).
                get(Source.SERVER)
                .addOnCanceledListener(() -> {
                    stayRefreshing.setValue(false);
                    Log.i(TAG, "preparedList: in canceled ");
                })
                .addOnCompleteListener(task -> {
                    Log.i(TAG, "preparedList: in complete ");
                    if (task.isSuccessful()) {
                        Log.i(TAG, "preparedList: in successful ");
                        for (DocumentSnapshot d : task.getResult()) {
                            Notification noty = new Notification();
                            noty.setRequestId(d.getId());
                            noty.setApprove(((Long) d.get("approve")).intValue());
                            noty.setArtitle(d.get("artitle").toString());
                            noty.setEntitle(d.get("entitle").toString());
                            noty.setArbody(d.get("arbody").toString());
                            noty.setEnbody(d.get("enbody").toString());
                            noty.setTime(d.getTimestamp("time"));
                            notyList.getValue().add(noty);
                        }
                        stayRefreshing.setValue(false);


                    }


                })
                .addOnFailureListener(e -> {
                    Log.i(TAG, "preparedList: in failure ");
                });

    }

}
