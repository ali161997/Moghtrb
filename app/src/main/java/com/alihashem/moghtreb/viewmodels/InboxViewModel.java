package com.alihashem.moghtreb.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alihashem.moghtreb.models.Notification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class InboxViewModel extends ViewModel {
    private MutableLiveData<List<Notification>> notyList;
    private boolean notySeen;

    public InboxViewModel() {
        notyList = new MutableLiveData<>();
    }

    public MutableLiveData<List<Notification>> getNotyList() {
        preparedList();
        return notyList;
    }

    public void setNotyList(MutableLiveData<List<Notification>> notyList) {
        this.notyList = notyList;
    }

    private void preparedList() {
        List<Notification> list = new ArrayList<>();
        CollectionReference roomsRef = FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("Notifications");
        roomsRef.orderBy("time");
        roomsRef.get().addOnSuccessListener(
                queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> listNoty = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : listNoty) {
                            Notification noty = new Notification();
                            noty.setRequestId(d.getId());
                            noty.setApprove(d.get("approve").toString());
                            noty.setArtitle(d.get("artitle").toString());
                            noty.setEntitle(d.get("entitle").toString());
                            noty.setArbody(d.get("arbody").toString());
                            noty.setEnbody(d.get("enbody").toString());
                            noty.setTime(d.getTimestamp("time"));
                            list.add(noty);
                        }
                        notyList.setValue(list);
                    }

                }).addOnFailureListener(e -> {
        });

    }

}
