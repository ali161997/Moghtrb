package com.moghtrb.viewmodels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.moghtrb.R;
import com.moghtrb.models.MyLatLong;
import com.moghtrb.models.ServiceInfoModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class InfoViewModel extends ViewModel {
    private static final String TAG = "InfoViewModel";
    private MutableLiveData<List<ServiceInfoModel>> listInfo;
    private MutableLiveData<Integer> indexService;
    private FirebaseFirestore firebaseFirestore;
    private List<String> listServices;
    private List<String> listDocIDs;
    private MutableLiveData<Context> ctx;

    public InfoViewModel() {
        listInfo = new MutableLiveData<>(new ArrayList<>());
        indexService = new MutableLiveData<>();
        firebaseFirestore = FirebaseFirestore.getInstance();
        listServices = new ArrayList<>();
        listDocIDs = new ArrayList<>();
        ctx = new MutableLiveData<>();
        setListDocIDs();
    }

    public MutableLiveData<Context> getCtx() {
        return ctx;
    }

    public void setCtx(Context ctx) {
        this.ctx.setValue(ctx);

    }

    public List<String> getListServices() {
        listServices = Arrays.asList(ctx.getValue().getResources().getStringArray(R.array.services));
        return listServices;
    }

    public void setListServices(List<String> listServices) {
        this.listServices = listServices;
    }

    public MutableLiveData<Integer> getTypeData() {
        return indexService;
    }

    public void setTypeData(Integer indexService) {
        this.indexService.setValue(indexService);
        Log.i(TAG, "setTypeData: " + indexService);
    }

    public MutableLiveData<List<ServiceInfoModel>> getListInfo() {
        downloadInfo();
        return listInfo;
    }

    public void setListInfo(MutableLiveData<List<ServiceInfoModel>> listInfo) {
        this.listInfo = listInfo;
    }

    public void UpdateValues(HashMap<String, HashMap<String, Object>> updates) {
        Log.i(TAG, "UpdateValues: on function");
        List<String> keys = prepareUpdateDocIDs(updates);
        List<HashMap<String, Object>> values = prepareValuesWillUpdate(updates);
        Log.i(TAG, "UpdateValues: ");
        for (int k = 0; updates.size() > k; k++) {
            firebaseFirestore.collection("Services")
                    .document("Assiut")
                    .collection(listDocIDs.get(indexService.getValue()))
                    .document(keys.get(k))
                    .update(
                            values.get(k)

                    ).addOnCompleteListener(task -> {
                Log.i(TAG, "UpdateValues: completed");
            }).addOnFailureListener(e -> {
                Log.i(TAG, "UpdateValues: failure" + e.getMessage());
            });
        }
    }

    private List<String> prepareUpdateDocIDs(HashMap<String, HashMap<String, Object>> map) {
        return new ArrayList<>(map.keySet());
    }

    private List<HashMap<String, Object>> prepareValuesWillUpdate(HashMap<String, HashMap<String, Object>> map) {
        Collection<HashMap<String, Object>> values = map.values();
        return new ArrayList<>(values);
    }

    public void downloadInfo() {
        listInfo.setValue(new ArrayList<>());
        firebaseFirestore.collection("Services")
                .document("Assiut")
                .collection(listDocIDs.get(indexService.getValue()))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot d : task.getResult()) {
                            listInfo.getValue().add(setInfoModel(d));

                        }
                    }
                }).addOnFailureListener(e -> {
            Log.i(TAG, "failure in get info" + e);
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

    private void setListDocIDs() {
        listDocIDs.add("homeMade");
        listDocIDs.add("workSpace");
        listDocIDs.add("restaurants");
        listDocIDs.add("football");
        listDocIDs.add("gym");
        listDocIDs.add("laundry");
        listDocIDs.add("cleaners");
        listDocIDs.add("taxi");
        listDocIDs.add("coffee");


    }


}
