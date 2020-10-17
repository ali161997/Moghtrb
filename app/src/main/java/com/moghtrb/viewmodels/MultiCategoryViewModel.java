package com.moghtrb.viewmodels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.moghtrb.R;
import com.moghtrb.models.MyLatLong;
import com.moghtrb.models.ServiceInfoModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiCategoryViewModel extends ViewModel {
    private static final String TAG = "MultiCategoryViewModel";
    MutableLiveData<List<String>> listCategories;
    MutableLiveData<Context> ctx;
    MutableLiveData<Integer> indexCat;
    private FirebaseFirestore firebaseFirestore;
    private List<String> listFields;
    private MutableLiveData<List<ServiceInfoModel>> listInfo;

    public MultiCategoryViewModel() {
        listCategories = new MutableLiveData<>();
        ctx = new MutableLiveData<>();
        firebaseFirestore = FirebaseFirestore.getInstance();
        indexCat = new MutableLiveData<>();
        listFields = new ArrayList<>();
        listInfo = new MutableLiveData<>(new ArrayList<>());
        setListFields();
    }

    public MutableLiveData<Integer> getIndexCat() {
        return indexCat;
    }

    public void setIndexCat(Integer indexCat) {
        this.indexCat.setValue(indexCat);
    }

    public MutableLiveData<List<ServiceInfoModel>> getListInfo() {
        return listInfo;
    }

    public void setListInfo(MutableLiveData<List<ServiceInfoModel>> listInfo) {
        this.listInfo = listInfo;
    }

    public MutableLiveData<List<String>> getListCategories() {
        List<String> s = Arrays.asList(ctx.getValue().getResources().getStringArray(R.array.restaurantCategory));
        listCategories.setValue(s);
        return listCategories;
    }

    public void setListCategories(List<String> listCategories) {
        this.listCategories.setValue(listCategories);
    }


    public MutableLiveData<Context> getCtx() {
        return ctx;
    }

    public void setCtx(MutableLiveData<Context> ctx) {
        this.ctx = ctx;
    }

    public void downloadInfo() {
        listInfo.setValue(new ArrayList<>());
        Log.i(TAG, "downloadInfo: in download info");
        Query query;
        if (indexCat.getValue() != null && listFields.get(indexCat.getValue()) != null) {
            query = firebaseFirestore.collection("Services")
                    .document("Assiut")
                    .collection("restaurants")
                    .whereEqualTo("type", listFields.get(indexCat.getValue()));
        } else {
            query = firebaseFirestore.collection("Services")
                    .document("Assiut")
                    .collection("restaurants");
        }

        query.get().addOnFailureListener(e -> {
            Log.i(TAG, "failure in get info" + e);
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot d : task.getResult()) {
                    listInfo.getValue().add(setInfoModel(d));
                    Log.i(TAG, "downloadInfo: completed");
                    Log.i(TAG, "downloadInfo: " + d.getData());

                }
            }

        });
    }

    private ServiceInfoModel setInfoModel(DocumentSnapshot doc) {
        ServiceInfoModel model = new ServiceInfoModel();
        model.setLikes(doc.getLong("likes").intValue());
        model.setDisLikes(doc.getLong("disLikes").intValue());
        model.setDocID(doc.getId());
        model.setInfoImage(doc.getString("infoImage"));
        model.setLocation(doc.get("latLng", MyLatLong.class));
        model.setName(doc.getString("name"));
        model.setPhone(doc.getString("phone"));
        return model;

    }

    private void setListFields() {
        listFields.add("crepe");
        listFields.add("seafood");
        listFields.add("pizza");
        listFields.add("burger");
        listFields.add("sandwich");
        listFields.add("foul");
        listFields.add("chicken");


    }

}
